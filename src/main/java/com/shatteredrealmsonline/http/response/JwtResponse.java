package com.shatteredrealmsonline.http.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponse
{
    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String type = "Bearer";

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private List<String> roles;

    @Getter
    @Setter
    private String message;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public JwtResponse(String message)
    {
        this.message = message;
    }
}
