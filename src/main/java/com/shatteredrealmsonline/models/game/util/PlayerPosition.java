package com.shatteredrealmsonline.models.game.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Represents the position of a player consisting of a location and position. Efficiently serializable for storage in
 * a database as a string
 */
public class PlayerPosition implements Serializable
{
    /**
     * String deliminator used to split elements of the PlayerPosition
     */
    private static final String DELIMINATOR = ";";

    /**
     * The x, y, and z location for the position
     */
    @Getter
    @Setter
    public Vector3D location;

    /**
     * The pitch, roll, and yaw for the position
     */
    @Getter
    @Setter
    public Vector3D rotation;

    /**
     * Name of the map for the position
     */
    @Getter
    @Setter
    public String map;

    /**
     * Creates an empty PlayerPosition
     */
    public PlayerPosition()
    {
        location = new Vector3D();
        rotation = new Vector3D();
        map = "StarterMap";
    }

    /**
     * Copy constructor
     * @param position PlayerPosition to copy
     */
    public PlayerPosition(PlayerPosition position)
    {
        this.location = position.getLocation();
        this.rotation = position.getRotation();
    }

    /**
     * Creates a PlayerPosition from a PlayerPosition#toString()
     *
     * @param fromString PlayerPosition#toString() of a PlayerPosition
     */
    public PlayerPosition(String fromString)
    {
        String[] split = fromString.split(DELIMINATOR);

        try
        {
            this.location = new Vector3D(split[0]);
            this.rotation = new Vector3D(split[1]);
            this.map = split[2];
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Invalid PlayerPosition string");
        }
    }

    /**
     * Serializable string for storing in the database in the form x:y:z;pitch:roll:yaw
     *
     * @return serialized string
     */
    @Override
    public String toString()
    {
        return location +DELIMINATOR+ rotation +DELIMINATOR+ map;
    }
}
