package com.shatteredrealmsonline.http.requests;

import lombok.Getter;
import lombok.Setter;

public class CreateCharacterRequest
{
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String breedName;

    @Getter
    @Setter
    private String genderName;

    @Getter
    @Setter
    private String className;
}
