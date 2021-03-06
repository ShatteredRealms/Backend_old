package com.shatteredrealmsonline.controllers.admin.game;

import com.shatteredrealmsonline.http.requests.ConnectRequest;
import com.shatteredrealmsonline.http.response.ConnectResponse;
import com.shatteredrealmsonline.models.game.Character;
import com.shatteredrealmsonline.models.web.User;
import com.shatteredrealmsonline.models.web.repos.UserRepository;
import com.shatteredrealmsonline.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param connectRequest authentication token and character name for the user trying to connect
     * @return character requested given valid information, otherwise error message.
     */
    @GetMapping(path="/connect")
    public @ResponseBody
    ResponseEntity<ConnectResponse> connect(@RequestBody ConnectRequest connectRequest)
    {
        ConnectResponse response = new ConnectResponse();
        response.setUniqueId(connectRequest.getUniqueId());

        if (connectRequest.getAuthToken() == null)
        {
            response.setError("No token sent");
            return ResponseEntity.badRequest().body(response);
        }

        String username = jwtUtils.getUserNameFromJwtToken(connectRequest.getAuthToken());

        if (username == null || username.equals(""))
        {
            response.setError("Invalid token.");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> oUser = userRepository.findByUsername(username);

        if (oUser.isEmpty())
        {
            response.setError("Could not find user with valid token.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        for (Character character : oUser.get().getCharacters())
        {
            if (character.getName().equals(connectRequest.getCharacterName())) {
                return ResponseEntity.ok(response);
            }
        }

        response.setError("No character with given name.");
        return ResponseEntity.badRequest().body(response);
    }
}


