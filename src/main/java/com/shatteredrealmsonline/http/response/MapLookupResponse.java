package com.shatteredrealmsonline.http.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Response for a map lookup http request for a character
 */
public class MapLookupResponse extends GenericResponse
{
    /**
     * Map requested for the lookup
     */
    @Getter
    @Setter
    private String map;
}
