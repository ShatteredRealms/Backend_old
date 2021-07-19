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

    @Getter
    @Setter
    private String uniqueId;
}
