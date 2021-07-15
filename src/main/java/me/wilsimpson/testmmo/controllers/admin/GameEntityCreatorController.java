package me.wilsimpson.testmmo.controllers.admin;

import me.wilsimpson.testmmo.http.response.MessageResponse;
import me.wilsimpson.testmmo.models.game.*;
import me.wilsimpson.testmmo.models.game.repos.*;
import me.wilsimpson.testmmo.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle creation of game entities.
 */
@Controller
@RequestMapping(path = "/api/v1/admin/game/creator")
public class GameEntityCreatorController
{
    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotTypeRepository slotTypeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private NanoRepository nanoRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillModifierRepository skillModifierRepository;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Gender API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/gender", "/gender/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Gender>> getGender(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(genderRepository, id);
    }

    @PostMapping(path = "/gender")
    public @ResponseBody
    ResponseEntity<MessageResponse> createGender(@RequestBody Gender requestedGender)
    {
        return ControllerUtils.genericCreate(
                genderRepository,
                requestedGender,
                Example.of(
                        requestedGender,
                        ExampleMatcher.matchingAny()
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                                .withIgnorePaths("description")
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/gender")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchGender(@RequestBody Gender gender)
    {
        return ControllerUtils.genericPatch(genderRepository, gender, gender.getId());
    }

    @DeleteMapping(path = "/gender/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteGender(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(genderRepository, id,"Gender");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Breed API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/breed", "/breed/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Breed>> getBreed(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(breedRepository, id);
    }

    @PostMapping(path = "/breed")
    public @ResponseBody
    ResponseEntity<MessageResponse> createBreed(@RequestBody Breed requestedBreed)
    {
        return ControllerUtils.genericCreate(
                breedRepository,
                requestedBreed,
                Example.of(
                        requestedBreed,
                        ExampleMatcher.matchingAny()
                                .withIgnorePaths("description")
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/breed")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchBreed(@RequestBody Breed breed)
    {
        return ControllerUtils.genericPatch(breedRepository, breed, breed.getId());
    }

    @DeleteMapping(path = "/breed/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteBreed(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(breedRepository, id, "Breed");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Slot API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/slot", "/slot/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Slot>> getSlot(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(slotRepository, id);
    }

    /**
     * Create a new slot. Two slots cannot have the same name and type, regardless of the id.
     *
     * @param requestedSlot requested slot to create
     * @return result of create operation
     */
    @PostMapping(path = "/slot")
    public @ResponseBody
    ResponseEntity<MessageResponse> createSlot(@RequestBody Slot requestedSlot)
    {
        return ControllerUtils.genericCreate(
                slotRepository,
                requestedSlot,
                Example.of(
                        requestedSlot,
                        ExampleMatcher.matchingAll()
                                .withIgnorePaths("id")
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name and type"
        );
    }

    @PatchMapping(path = "/slot")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchSlot(@RequestBody Slot slot)
    {
        return ControllerUtils.genericPatch(slotRepository, slot, slot.getId());
    }

    @DeleteMapping(path = "/slot/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteSlot(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(slotRepository, id, "Slot");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SlotType API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/slotType", "/slotType/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<SlotType>> getSlotTypeType(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(slotTypeRepository, id);
    }

    @PostMapping(path = "/slotType")
    public @ResponseBody
    ResponseEntity<MessageResponse> createSlotTypeType(@RequestBody SlotType requestedSlotType)
    {
        return ControllerUtils.genericCreate(
                slotTypeRepository,
                requestedSlotType,
                Example.of(
                        requestedSlotType,
                        ExampleMatcher.matchingAny()
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/slotType")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchSlotType(@RequestBody SlotType slotType)
    {
        return ControllerUtils.genericPatch(slotTypeRepository, slotType, slotType.getId());
    }

    @DeleteMapping(path = "/slotType/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteSlotType(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(slotTypeRepository, id, "Slot");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Item API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/item", "/item/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Item>> getItem(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(itemRepository, id);
    }

    @PostMapping(path = "/item")
    public @ResponseBody
    ResponseEntity<MessageResponse> createItem(@RequestBody Item requestedItem)
    {
        return ControllerUtils.genericCreate(
                itemRepository,
                requestedItem,
                Example.of(
                        requestedItem,
                        ExampleMatcher.matchingAny()
                                .withIgnorePaths("requirements",
                                        "quality",
                                        "stacksize",
                                        "description",
                                        "equipableSlots",
                                        "itemModifiers",
                                        "minDamage",
                                        "maxDamage",
                                        "critBonus")
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/item")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchItem(@RequestBody Item nano)
    {
        return ControllerUtils.genericPatch(itemRepository, nano, nano.getId());
    }

    @DeleteMapping(path = "/item/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteItem(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(itemRepository, id, "Item");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nano API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/nano", "/nano/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Nano>> getNano(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(nanoRepository, id);
    }

    @PostMapping(path = "/nano")
    public @ResponseBody
    ResponseEntity<MessageResponse> createItem(@RequestBody Nano requestedNano)
    {
        return ControllerUtils.genericCreate(
                nanoRepository,
                requestedNano,
                Example.of(
                        requestedNano,
                        ExampleMatcher.matchingAny()
                                .withIgnorePaths("skillRequirements",
                                        "skillModifiers",
                                        "duration",
                                        "damage")
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/nano")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchItem(@RequestBody Nano nano)
    {
        return ControllerUtils.genericPatch(nanoRepository, nano, nano.getId());
    }

    @DeleteMapping(path = "/nano/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteNano(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(nanoRepository, id, "Nano");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ItemInventory API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/inventoryItem", "/inventoryItem/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<InventoryItem>> getItemInventory(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(inventoryItemRepository, id);
    }

    @PostMapping(path = "/inventoryItem")
    public @ResponseBody
    ResponseEntity<MessageResponse> createItem(@RequestBody InventoryItem inventoryItem)
    {
        return ControllerUtils.genericCreate(
                inventoryItemRepository,
                inventoryItem,
                Example.of(
                        inventoryItem,
                        ExampleMatcher.matchingAny()
                                .withIgnorePaths("item",
                                        "owner")
                ),
                "slot or id"
        );
    }

    @PatchMapping(path = "/inventoryItem")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchInventoryItem(@RequestBody InventoryItem inventoryItem)
    {
        return ControllerUtils.genericPatch(inventoryItemRepository, inventoryItem, inventoryItem.getId());
    }

    @DeleteMapping(path = "/inventoryItem/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteInventoryItem(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(inventoryItemRepository, id, "InventoryItem");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Skill API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/skill", "/skill/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<Skill>> getSkill(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(skillRepository, id);
    }

    @PostMapping(path = "/skill")
    public @ResponseBody
    ResponseEntity<MessageResponse> createSkill(@RequestBody Skill requestedSkill)
    {
        return ControllerUtils.genericCreate(
                skillRepository,
                requestedSkill,
                Example.of(
                        requestedSkill,
                        ExampleMatcher.matchingAny()
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/skill")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchSkill(@RequestBody Skill skill)
    {
        return ControllerUtils.genericPatch(skillRepository, skill, skill.getId());
    }

    @DeleteMapping(path = "/skill/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteSkill(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(skillRepository, id, "Skill");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SkillModifier API
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = { "/skillModifier", "/skillModifier/{id}" })
    public @ResponseBody
    ResponseEntity<Iterable<SkillModifier>> getSkillModifier(@PathVariable(name = "id", required = false) Long id)
    {
        return ControllerUtils.genericGet(skillModifierRepository, id);
    }

    @PostMapping(path = "/skillModifier")
    public @ResponseBody
    ResponseEntity<MessageResponse> createSkillModifier(@RequestBody SkillModifier requestedSkillModifier)
    {
        return ControllerUtils.genericCreate(
                skillModifierRepository,
                requestedSkillModifier,
                Example.of(
                        requestedSkillModifier,
                        ExampleMatcher.matchingAny()
                                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
                ),
                "name or id"
        );
    }

    @PatchMapping(path = "/skillModifier")
    public @ResponseBody
    ResponseEntity<MessageResponse> patchSkillModifier(@RequestBody SkillModifier skillModifier)
    {
        return ControllerUtils.genericPatch(skillModifierRepository, skillModifier, skillModifier.getId());
    }

    @DeleteMapping(path = "/skillModifier/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteSkillModifier(@PathVariable(name = "id") Long id)
    {
        return ControllerUtils.genericDelete(skillModifierRepository, id, "SkillModifier");
    }
}
