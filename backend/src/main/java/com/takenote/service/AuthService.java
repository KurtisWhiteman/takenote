package com.takenote.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.takenote.model.User;
import com.takenote.model.util.*;
import com.takenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final UserService userService;
    private final int tokenExpiry;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    AuthService(@Value("${jwt.secret}") String secret,
                @Value("${jwt.expiry}") int tokenExpiry,
                UserRepository userRepository, UserService userService) {
        this.tokenExpiry = tokenExpiry;
        this.algorithm = Algorithm.HMAC512(secret);
        this.userService = userService;
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("Takenote")
                .build();
    }

    public LoginResult attemptLogin(LoginRequest request) {
        // 1. Get the user details
        var user = this.userService.loadUserByEmail(request.getEmail());

        if (Objects.isNull(user)) {
            return LoginResult
                    .builder()
                    .loginResponse(LoginResponse.WRONG_USER_PASSWORD)
                    .build();
        }

        // 3. check user status
        switch (user.getStatus()) {
            case LOCKED:
                return getFailedLoginResult(LoginResponse.LOCKED);
            case CREATED:
                return getFailedLoginResult(LoginResponse.CREATED);
            case PENDING:
                return getFailedLoginResult(LoginResponse.PENDING);
            case DISABLED:
                return getFailedLoginResult(LoginResponse.DISABLED);
            case ACTIVE:
                // We don't return a failure here because we continue with the password & role verification
                break;
        }

        // 4. verify user's password
        if (!verifyPassword(request, user)) {
            return getFailedLoginResult(LoginResponse.WRONG_USER_PASSWORD);
        }

        // 6. When everything is ok then generate token
        String token = generateJWT(user);

        return LoginResult.builder()
                .loginResponse(LoginResponse.OK)
                .token(token)
                .build();
    }

    /**
     * When we have a non-successful login, we create the failed login attempt and return the correct response.
     *
     * @param loginResponse The login response to return to the LoginResult
     * @return LoginResult
     */
    private LoginResult getFailedLoginResult(LoginResponse loginResponse) {
        return LoginResult
                .builder()
                .loginResponse(loginResponse)
                .build();
    }

    /**
     * This was being called from verifyPassword() method which doubles up the time taken for new logins.
     * Given we are not moving any old users to new system, this method likely can be removed later once confirmed.
     * This is a remnant of the last system. Everything is SHA512'd then bcrypted.
     * This will revert the double hashing and then log you in.
     *
     * @param request Login request
     * @param user    The user in the system
     * @return True if successfully authenticated using hashed password
     */
    private boolean verifyPassword(LoginRequest request, User user) {

        // Always run a comparison against a failing password if the user doesn't exist, so we still take the same time
        // Regardless of if the user exists or not, it's harder to brute force if time is constant whether the user exists or not
        // For a valid bcrypt string it must have $2y$10$ followed by 53 chars
        String dummyPassword = "$2y$10$.THISW.ILLNOT.WORKASITISNoT.ABCRYPTHASH.NOBCRYPT.HERE";
        String existingHashedPassword = Objects.isNull(user) ? dummyPassword : user.getPassword();

        return this.bCryptEncoder.matches(request.getPassword(), existingHashedPassword);
    }

    //generate token for user
    private String generateJWT(User user) {
        // add some relevant user details to the JWT
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (tokenExpiry * 1000L)))
                .withIssuer("Takenote")
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        // remove the bearer prefix on the token
        return this.verifier.verify(token.replace("Bearer ", ""));
    }
}
