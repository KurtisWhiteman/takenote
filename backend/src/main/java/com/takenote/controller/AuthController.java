package com.takenote.controller;

import com.takenote.model.util.LoginRequest;
import com.takenote.model.util.LoginResult;
import com.takenote.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * This is the login controller to validate the user's credential
     *
     * @param request        The JSON request body that will be passed into the controller that contains the user's
     *                       credentials
     * @param servletRequest This is required to retrieve the IP address of the user to log the attempt. Not required
     *                       to be passed in via the API
     * @return This controller will return a JWT if the credential is successfully validated, or it will return an error
     * message
     */
    @PostMapping(path = "/login")
    public ResponseEntity<String> createJWT(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {

        // Try to generate a token using the provided credentials
        LoginResult result = this.authService.attemptLogin(request);

        return switch (result.getLoginResponse()) {
            case OK -> ResponseEntity
                    .ok(result.getToken());
            case WRONG_USER_PASSWORD -> ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Bearer realm=\"Takenote\"")
                    .body("Could not log in with the supplied credentials.");
            case LOCKED -> ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("User is locked due to too many failed login attempts. Please contact your account manager.");
            case DISABLED -> ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("User is disabled. Please contact your account manager.");
            default -> ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("User requires email activation. Please contact your account manager.");
        };
    }
}
