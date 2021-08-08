package com.shatteredrealmsonline.controllers.admin.players;

import com.shatteredrealmsonline.models.game.Character;
import com.shatteredrealmsonline.models.game.repos.CharacterRepository;
import com.shatteredrealmsonline.models.web.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/admin/players")
public class AdminCharacterController
{
    private final Logger LOGGER = LoggerFactory.getLogger(AdminCharacterController.class);

    private final UserRepository userRepository;

    private final CharacterRepository characterRepository;

    public AdminCharacterController(UserRepository userRepository, CharacterRepository characterRepository)
    {
        this.userRepository = userRepository;
        this.characterRepository = characterRepository;
    }

    @GetMapping(path="/characters/{character}/map")
    public @ResponseBody
    ResponseEntity<String> getCharactersMap(@PathVariable(name="character") String characterName)
    {
        if (characterName == null)
        {
            return ResponseEntity.badRequest().body("Error: character name can not be null");
        }

        Optional<Character> oCharacter = characterRepository.findByName(characterName);

        if (oCharacter.isEmpty())
        {
            return ResponseEntity.unprocessableEntity().body("Error: character not found with given name");
        }

        return ResponseEntity.ok(oCharacter.get().getPosition().getMap());
    }
}
