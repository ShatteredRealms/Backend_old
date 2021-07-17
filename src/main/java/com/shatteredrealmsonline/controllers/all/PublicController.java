package com.shatteredrealmsonline.controllers.all;

import com.shatteredrealmsonline.http.response.DatabaseResponse;
import com.shatteredrealmsonline.models.game.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/v1/pub")
public class PublicController {

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private NanoRepository nanoRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillModifierRepository skillModifierRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    SlotTypeRepository slotTypeRepository;

    @GetMapping(path = "/db")
    public @ResponseBody
    DatabaseResponse getDatabase()
    {
        DatabaseResponse response = new DatabaseResponse();
        response.setBreeds(breedRepository.findAll());
        response.setGenders(genderRepository.findAll());
        response.setItems(itemRepository.findAll());
        response.setNanos(nanoRepository.findAll());
        response.setSkills(skillRepository.findAll());
        response.setSkillModifiers(skillModifierRepository.findAll());
        response.setSlots(slotRepository.findAll());
        response.setSlotTypes(slotTypeRepository.findAll());

        return response;
    }
}
