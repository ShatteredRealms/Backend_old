package com.shatteredrealmsonline.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Skills are attributes of all Characters. Each skill may have a different min value, max value depending on Level,
 * Breed, Profession or other definitions of a Character.
 */
@Entity
@Table(name="skills", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class Skill
{
    /**
     * Unique ID for the Skill
     */
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false)
	@Getter
    private Long id;


    /**
     * The name for the Skill
     */
    @Getter
	@Setter
	@Column(nullable=false)
    private String name;

    /**
     * Used interanally to create Skills from database entries
     */
    protected Skill() { }

    /**
     * Create a Skill with a given name and generate a unique ID
     *
     * @param name name of the Skill
     */
    public Skill(String name)
    {
        this.name = name;
    }

    /**
     * Create a Skill with a given name and unique ID
     *
     * @param id unique ID for the Skill
     * @param name name of the Skill
     */
    public Skill(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
