package com.takenote.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.takenote.model.util.AuthToken;
import com.takenote.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Apply Application Authorization on each request.
 * Decode the JWT if provided, extract the user and role then continue with the execution
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthService authService;
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  AuthService authService) {
        super(authenticationManager);
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        // if we don't have an auth header with a bearer don't waste time trying to decode
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            res.addHeader("WWW-Authenticate", "Bearer realm=\"Takenote\"");
            chain.doFilter(req, res);
            return;
        }

        // Check if the provided JWT is valid, and if so give spring the user context
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    /**
     * Return the Spring Authorisation Token, this allows us to apply RBAC
     * Assuming the JWT is valid, extract the subject (user) and role and grant them that permission
     * @param request The inbound request containing a JWT
     * @return UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            try {
                AuthToken authToken = new AuthToken(authService.decodeToken(token));

                // Give the user the roles in the token
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                // Give Spring the context that this subject has the role defined above
                return new UsernamePasswordAuthenticationToken(
                        authToken,
                        null,
                        authorities
                );
            } catch (JWTVerificationException e) {
            }
        }
        return null;
    }
}
