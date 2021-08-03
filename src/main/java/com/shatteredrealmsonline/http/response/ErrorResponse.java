package com.shatteredrealmsonline.http.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents and error code sent from an HTTP request.
 */
public class ErrorResponse
{
    /**
     * Error code for the response
     */
    @Getter
    @Setter
    private ResponseErrorCode code;

    /**
     * Reason or explanation of error code
     */
    @Getter
    @Setter
    private String message;

    /**
     * Creates an error code from code and message
     * @param code error code for the error response
     * @param message custom message for the error response
     */
    public ErrorResponse(ResponseErrorCode code, String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * Creates an error code from a code and uses it's default message if requested.
     * @param code error code for the error response
     * @param useDefaultMessage whether or not to use the default message from the error code
     */
    public ErrorResponse(ResponseErrorCode code, boolean useDefaultMessage)
    {
        this.code = code;
        if (useDefaultMessage) this.message = code.getDefaultMessage();
    }
}
