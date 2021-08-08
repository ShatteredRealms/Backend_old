package com.shatteredrealmsonline.models.game.util;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple 3D Vector
 */
public class Vector3D
{
    /**
     * String deliminator used to split elements of the Vector3D
     */
    private static final String DELIMINATOR = ":";

    /**
     * First element of the vector
     */
    @Getter
    @Setter
    private float x;

    /**
     * Second element of the vector
     */
    @Getter
    @Setter
    private float y;


    /**
     * Third element of the vector
     */
    @Getter
    @Setter
    private float z;

    /**
     * Creates an empty Vector3D
     */
    public Vector3D()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Copy constructor
     * @param old Vector3D to copy
     */
    public Vector3D(Vector3D old)
    {
        this.x = old.getX();
        this.y = old.getY();
        this.z = old.getZ();
    }

    /**
     * Creates a Vector3D from a Vector3D#toString()
     *
     * @param fromString Vector3D#toString() of a Vector3D
     */
    public Vector3D(String fromString)
    {
        String[] split = fromString.split(":");

        try
        {
            x = Float.parseFloat(split[0]);
            y = Float.parseFloat(split[1]);
            z = Float.parseFloat(split[2]);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Invalid Vector3D string");
        }
    }

    /**
     * Serializable string for storing in the database in the form x:y:z
     *
     * @return serialized string
     */
    @Override
    public String toString()
    {
        return x +DELIMINATOR+ y +DELIMINATOR+ z;
    }
}
