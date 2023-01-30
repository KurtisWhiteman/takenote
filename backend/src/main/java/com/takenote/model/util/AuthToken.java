package com.takenote.model.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Value;

import java.util.Map;

@Value
public class AuthToken {
    long userId;
    String email;

    public AuthToken(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        this.userId = claims.get("id").asLong();
        this.email = decodedJWT.getSubject();
    }
}
