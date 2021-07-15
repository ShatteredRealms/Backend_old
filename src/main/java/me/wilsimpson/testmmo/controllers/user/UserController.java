package me.wilsimpson.testmmo.controllers.user;


import me.wilsimpson.testmmo.controllers.admin.AdminController;
import me.wilsimpson.testmmo.models.game.Character;
import me.wilsimpson.testmmo.models.web.User;
import me.wilsimpson.testmmo.models.web.repos.UserRepository;
import me.wilsimpson.testmmo.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/api/v1/user")
public class UserController
{
    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/game/characters")
    public @ResponseBody
    ResponseEntity<Iterable<Character>> getCharacters()
    {
        return ResponseEntity.ok(getCurrentUser().getCharacters());
    }

    private User getCurrentUser()
    {
        return userRepository.getById(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

}
