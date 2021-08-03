package com.shatteredrealmsonline.http.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

public enum ResponseErrorCode
{
    NOT_FOUND(5, "Could not find");

    @Getter
    int code;

    @Getter
    @JsonIgnore
    String defaultMessage;

    ResponseErrorCode(int code, String defaultMessage)
    {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
