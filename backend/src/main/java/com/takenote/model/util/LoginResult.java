package com.takenote.model.util;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class LoginResult {
    @NotNull
    LoginResponse loginResponse;

    String token;
}
