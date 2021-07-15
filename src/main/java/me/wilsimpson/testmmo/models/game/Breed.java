package me.wilsimpson.testmmo.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Breed of Character
 */
@Entity
@Table(name="breeds", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class Breed
{
    /**
     * Unique ID for the Breed
     */
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false)
	@Getter
    private Long id;

    /**
     * Name of the Breed. Cannot be null.
     */
    @Getter
	@Setter
	@Column(nullable=false)
    private String name;

    /**
     * Description of the Breed
     */
    @Getter
	@Setter
    private String description;
}
