package com.shatteredrealmsonline.http.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

public class ResetPasswordRequest
{
    @Getter
    @Setter
    @Min(3)
    private String username;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String password;
}
