package com.shatteredrealmsonline.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic response from an http response.
 */
public class GenericResponse
{
    @Getter
    @Setter
    @JsonInclude(Include.NON_NULL)
    private ErrorResponse error;

    public GenericResponse(ErrorResponse error)
    {
        this.error = error;
    }

    public GenericResponse()
    {

    }
}
