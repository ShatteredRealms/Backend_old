package me.wilsimpson.testmmo.models.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Explains how an Item is related to a Character and what slot the item is located.
 */
@Entity
@Table(name = "inventory_items")
public class InventoryItem
{
    /**
     * Unique ID for the InventoryItem
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    /**
     * Item that is being stored
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item")
    private Item item;

    /**
     * Slot the item is stored in
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "slot")
    private Slot slot;

    /**
     * Owner of the Item
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "owner")
    private Character owner;
}
