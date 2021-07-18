package com.shatteredrealmsonline.http.response;

import com.shatteredrealmsonline.models.game.Character;
import lombok.Getter;
import lombok.Setter;

public class ConnectResponse
{
    @Getter
    @Setter
    private String error;

    @Getter
    @Setter
    private Character character;

    public ConnectResponse(String error)
    {
        this.error = error;
    }

    public ConnectResponse(Character character)
    {
        this.character = character;
    }
}
