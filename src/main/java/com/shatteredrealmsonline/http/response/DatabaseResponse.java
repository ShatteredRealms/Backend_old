package com.shatteredrealmsonline.http.response;

import com.shatteredrealmsonline.models.game.*;
import lombok.Getter;
import lombok.Setter;

public class DatabaseResponse
{
    @Getter
    @Setter
    private String version = "0.0.1";

    @Getter
    @Setter
    private Iterable<Breed> breeds;

    @Getter
    @Setter
    private Iterable<Gender> genders;

    @Getter
    @Setter
    private Iterable<Item> items;

    @Getter
    @Setter
    private Iterable<Nano> nanos;

    @Getter
    @Setter
    private Iterable<Skill> skills;

    @Getter
    @Setter
    private Iterable<SkillModifier> skillModifiers;

    @Getter
    @Setter
    private Iterable<Slot> slots;

    @Getter
    @Setter
    private Iterable<SlotType> slotTypes;
}
