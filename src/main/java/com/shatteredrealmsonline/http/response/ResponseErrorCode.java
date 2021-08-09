package com.shatteredrealmsonline.http.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

public enum ResponseErrorCode
{
    ACCESS_DENIED(1, "Access denied"),
    NOT_FOUND(5, "Could not find"),
    MISSING_REQUEST_CONTENT(6, "Could not find all required request content"),
    ALREADY_EXISTS(7, "Already exists"),
    BAD_NAME(8, "Bad name"),

    ;

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
