package com.shatteredrealmsonline.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

/**
 * Items are things that may be able to be used, equipped, placed or stored by a Character. Items are used in game by
 * storing in a Slot. An iIem may have requirements that are needed in-order to equip, used or placed. Items may also
 * have modifiers that change a Character depending on if it ise equipped. An Item may also be equipped if Slots are
 * defined. Many fields ad nullable or empty and which case they are not used for a particular item.
 */
@Entity
@Table(name="items")
public class Item
{
    /**
     * Unique ID for the Item
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    /**
     * Name of the Item. Cannot be null.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    /**
     * Requirements to use or equip the Item if possible. If this is empty then there are no requirements.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_skill_requirements",
            joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_modifier_id", referencedColumnName = "id"))
    private Set<SkillModifier> requirements;


    /**
     * Modifiers that the Item has to a Character when equipped. If an item has no valid equipableSlots then these
     * values will never be used. A modifier can have a positive or negative amount.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_skill_modifiers",
            joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_modifier_id", referencedColumnName = "id"))
    private Set<SkillModifier> modifiers;


    /**
     * Quality level of an item. It may be a number of a string. Cannot be null.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String quality;

    /**
     * Max stack size of an Item. If less than or equal to zero then the item cannot be stacked.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer stackSize;

    /**
     * Description of the item. Cannot be null
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private String description;

    /**
     * Slots that the Item can be equipped to. The SlotType should always be related to something equipable. If none are
     * defined then the item cannot be equipped.
     */
    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_equipable_slots", joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "slot_id", referencedColumnName = "id"))
    private Set<Slot> equipableSlots;


    /**
     * Minimum base damage the item can do when equipped as a weapon. If the equipableSlots does not have a valid entry
     * for a Slot relating to an equipable weapon then this field will not be used. Minimum damage should always be
     * greater than 0.
     */
    @Getter
    @Setter
    @Min(1)
    private Integer minDamage;

    /**
     * Maximum base damage the item can do when equipped as a weapon. If the equipableSlots does not have a valid entry
     * for a Slot relating to an equipable weapon then this field will not be used. Maximum damage should always be
     * greater than 0 and greater than the minDamage.
     */
    @Getter
    @Setter
    @Min(1)
    private Integer maxDamage;

    /**
     * Base critical bonus damage the item can do when equipped as a weapon. When a critical hit is performed, this value
     * will be used in calculations of damage. If the equipableSlots does not have a valid entry for a Slot relating to
     * an equipable weapon then this field will not be used. Crit bonuse damage should always be greater than 0.
     */
    @Getter
    @Setter
    @Min(1)
    private Integer critBonus;

    /**
     * Checks whether the Item can be stacked
     *
     * @return true if the item can be stacked, otherwise false.
     */
    public boolean isStackable()
    {
        return stackSize > 0;
    }

    /**
     * Checks whether an Item is equipable
     *
     * @return true if the item can be equipped, otherwise false.
     */
    public boolean isEquipable()
    {
        return equipableSlots != null && equipableSlots.size() > 0;
    }

    private String serializedRequirements;
}
