package com.shatteredrealmsonline.models.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shatteredrealmsonline.models.game.util.PlayerPosition;
import lombok.Getter;
import lombok.Setter;
import com.shatteredrealmsonline.models.web.User;

import javax.persistence.*;
import java.util.List;
import java.util.Vector;

/**
 * Characters are what the Player controls in the game.
 */
@Entity
@Table(name = "characters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Character
{
    /**
     * Unique ID for the Character.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Getter
    private Long id;

    /**
     * Name of the Character. Cannot be null.
     */
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Name of the User who owns the Character. Cannot be null.
     */
    @Getter
    @Setter
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    @ManyToOne
    @JsonIgnore
    private User owner;

    /**
     * Breed of the Character. Cannot be null.
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    /**
     * Gender of the Character. Cannot be null.
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "character_class_id", nullable = false)
    private CharacterClass characterClass;

    /**
     * Currently running Nanos in the Characters NCU
     */
    @Getter
    @Setter
    @ManyToMany
    @JoinTable(name = "character_ncu",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nano_id", referencedColumnName = "id"))
    private List<Nano> ncu;

    /**
     * Currently learned nanos
     */
    @Getter
    @Setter
    @ManyToMany
    @JoinTable(name = "character_nanos",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nano_id", referencedColumnName = "id"))
    private List<Nano> nanos;

    /**
     * Every Item that the Character has attached to them.
     */
    @Getter
    @Setter
    @ManyToMany
    @JoinTable(name = "character_items",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "inv_item_id", referencedColumnName = "id"))
    private List<InventoryItem> characterItems;

    /**
     * String serialized player position
     */
    private String stringLocation = "";

    /**
     * Gets the position of the character from a serialized string
     *
     * @return position of the character
     */
    public PlayerPosition getPosition()
    {
        return new PlayerPosition(stringLocation);
    }

    /**
     * Sets the string serialized position of the character
     *
     * @param position new position to set to
     */
    public void setPosition(PlayerPosition position)
    {
        this.stringLocation = position.toString();
    }
}
