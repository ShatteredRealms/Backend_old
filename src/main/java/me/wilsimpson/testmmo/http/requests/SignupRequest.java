package me.wilsimpson.testmmo.http.requests;

import lombok.Getter;
import lombok.Setter;

public class SignupRequest
{
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;
}
