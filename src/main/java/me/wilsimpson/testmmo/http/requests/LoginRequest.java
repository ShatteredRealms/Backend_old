package me.wilsimpson.testmmo.http.requests;

import lombok.Getter;
import lombok.Setter;

public class LoginRequest
{
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    public LoginRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
}
