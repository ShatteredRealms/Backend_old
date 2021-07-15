package me.wilsimpson.testmmo.models.game;

import lombok.Getter;
import lombok.Setter;
import me.wilsimpson.testmmo.models.web.User;

import javax.persistence.*;
import java.util.List;

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
}
