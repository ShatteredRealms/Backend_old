package com.shatteredrealmsonline.controllers.admin.game;

import com.shatteredrealmsonline.models.web.repos.UserRepository;
import com.shatteredrealmsonline.models.game.Character;
import com.shatteredrealmsonline.models.web.User;
import com.shatteredrealmsonline.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/v1/admin/gamemode/")
public class GameModeController
{
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public GameModeController(JwtUtils jwtUtils, UserRepository userRepository)
    {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    /**
     * Gets the character for the user when given a valid authToken and character name. Otherwise return with the
     * corresponding error.
     *
     * @TODO(wil): See if returning a ResponseEntity<Object> is good practice or if we should be more specific.
     *
     * @param authToken authentication token for the user trying to connect
     * @param characterName name of the character
     * @return character requested given valid information, otherwise error message.
     */
    @GetMapping(path="/connect")
    public @ResponseBody
    ResponseEntity<Object> connect(@RequestParam(name = "t") String authToken, @RequestParam(name = "c") String characterName)
    {
        if (authToken == null)
            return ResponseEntity.badRequest().body("No token sent");

        String username = jwtUtils.getUserNameFromJwtToken(authToken);

        if (username == null || username.equals(""))
            return ResponseEntity.badRequest().body("Invalid token.");

        Optional<User> oUser = userRepository.findByUsername(username);

        if (oUser.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not find user with valid token.");

        for (Character character : oUser.get().getCharacters())
            if (character.getName().equals(characterName))
                return ResponseEntity.ok(character);

        return ResponseEntity.badRequest().body("No character with given name.");
    }
}
