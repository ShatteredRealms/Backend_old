package me.wilsimpson.testmmo.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * SkillModifier defines how a Skill can be modified or required for or by an Item or Nano. The 'amount' can be used to
 * define the amount modified or the minimum requirement.
 */
@Entity
@Table(name = "skill_modifiers")
public class SkillModifier
{
    /**
     * Unique ID for the SKillModifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Getter
    private Long id;

    /**
     * Skill that is being modified or required. Cannot be null.
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill")
    private Skill skill;

    /**
     * Amount to modify or the minimum requirement. Cannot be null.
     */
    @Setter
    @Column(nullable = false)
    private int amount;

    /**
     * Used internally to create a SkillModifier from a database entry.
     */
    protected SkillModifier() { }

    /**
     * Create a SkillModifier for a given Skill by a given amount.
     *
     * @param skill Skill to modify or require
     * @param amount amount to modify by or the minimum requirement
     */
    public SkillModifier(Skill skill, int amount)
    {
        this.skill = skill;
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }
}
