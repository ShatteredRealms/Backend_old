package com.shatteredrealmsonline.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name="nanos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Nano
{
    /**
     * Unique ID for the Nano
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    /**
     * Name of the Nano. Cannot be null.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    /**
     * Skill requirements to use the Nano. If this is empty then there are no requirements.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "nano_skill_requirements",
            joinColumns = @JoinColumn(name = "nano_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_modifier_id", referencedColumnName = "id"))
    private Set<SkillModifier> requirements;

    /**
     * Modifiers that the Nano has to a Character when casted. A modifier can have a positive or negative amount.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "nano_skill_modifiers",
            joinColumns = @JoinColumn(name = "nano_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_modifier_id", referencedColumnName = "id"))
    private Set<SkillModifier> modifiers;

    /**
     * How long the Nano lasts. If the value is zero then the Nano does not effect the NCU
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer duration;

    /**
     * How much damage the Nano does. If the value is positive it does damage, if it is negative it heals. If null then
     * it does not apply any damage.
     */
    @Getter
    @Setter
    private Integer damage;
}
