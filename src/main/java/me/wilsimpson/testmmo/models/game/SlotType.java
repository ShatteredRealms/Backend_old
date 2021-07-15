package me.wilsimpson.testmmo.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * SlotTypes define the owner of a Slot and define a purpose with their name. For example they can define an Inventory
 * which can only be accessed by a local player or a Bank which can only be accessed by a local player AND by opening
 * a special interface. These properties are not defined here but are to be interpreted by the server.
 */
@Entity
@Table(name="slot_types", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class SlotType
{
    /**
     * Unique ID for the SlotType.
     */
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false)
	@Getter
	@Setter
    private Long id;

    /**
     * Name for the SlotType. Cannot be null.
     */
    @Getter
	@Setter
	@Column(nullable=false)
    private String name;

    /**
     * Used by the internal system to create a SlotType from a database entry
     */
    protected SlotType () { }

    /**
     * Create a SlotType with a given name.
     *
     * @param name name of the SlotType
     */
    public SlotType(String name)
    {
        this.name = name;
    }

    /**
     * Create a SlotType with a given name and unique ID
     *
     * @param id unique ID for the SlotType
     * @param name name of the SlotType
     */
    public SlotType(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
