package com.shatteredrealmsonline.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Gender for a Character.
 */
@Entity
@Table(name="genders", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class Gender
{
    /**
     * Unique ID for the Gender.
     */
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false)
	@Getter
    private Long id;

    /**
     * Name of the Gender. Cannot be null.
     */
    @Getter
	@Setter
	@Column(nullable=false)
    private String name;

    /**
     * Description of the Gender
     */
    @Getter
	@Setter
    private String description;
}

