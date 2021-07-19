package com.shatteredrealmsonline.http.requests;

import lombok.Getter;
import lombok.Setter;

public class ConnectRequest
{
    @Getter
    @Setter
    private String authToken;

    @Getter
    @Setter
    private String characterName;

    @Getter
    @Setter
    private String uniqueId;
}
