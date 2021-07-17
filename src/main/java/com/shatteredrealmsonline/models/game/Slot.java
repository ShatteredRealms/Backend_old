package com.shatteredrealmsonline.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Slots are interfaces to allow Items to be placed in them. Control of whether an item can be placed in a Slot is
 * defined in the Item. Every Slot must have SlotType.
 */
@Entity
@Table(name="slots")
public class Slot
{
    /**
     * UniqueID for the Slot.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    /**
     * SlotType for the Slot.
     */
    @Getter
    @Setter
    @ManyToOne
    private SlotType type;

    /**
     * Name of a slot. Can be null.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Used internally to create a Slot form a database result.
     */
    protected Slot() { }

    /**
     * Create a Slot with a given SlotType and generate a unique ID.
     *
     * @param type SlotType for the Slot
     */
    public Slot(SlotType type)
    {
        this.type = type;
    }

    /**
     * Create a slot name with a given ID and SlotType.
     *
     * @param id Slot ID for the slot
     * @param type SlotType for the slot
     */
    public Slot(Long id, SlotType type)
    {
        this.id = id;
        this.type = type;
    }

    /**
     * Create a slot with a given SlotType and name then generate a unique ID.
     *
     * @param type SlotType for the slot
     * @param name name for the slot
     */
    public Slot(SlotType type, String name)
    {
        this.type = type;
        this.name = name;
    }

    /**
     * Create a slot with a name and ID.
     *
     * @param id
     * @param type
     * @param name
     */
    public Slot(Long id, SlotType type, String name)
    {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
