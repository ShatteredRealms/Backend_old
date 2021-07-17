package com.shatteredrealmsonline.controllers.admin;

import com.shatteredrealmsonline.http.response.MessageResponse;
import com.shatteredrealmsonline.models.web.User;
import com.shatteredrealmsonline.models.web.repos.ResetTokenRepository;
import com.shatteredrealmsonline.models.web.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @TODO: Remove and create sub controllers that handle more specific tasks
 */
@Controller
@RequestMapping(path = "/api/v1/admin")
public class AdminController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping(path="/users")
    public @ResponseBody
    Iterable<User> getUsers(@RequestParam(name = "id", required = false) Long id)
    {
        if(id == null)
            return userRepository.findAll();

        return userRepository.findById(id).map(List::of).orElseGet(List::of);
    }

    @DeleteMapping(path="/users/{id}")
    public @ResponseBody
    ResponseEntity<MessageResponse> deleteUser(@PathVariable @NotNull long id)
    {
        Optional<User> oUser = userRepository.findById(id);

        if(oUser.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("No user for given id"));

        User user = oUser.get();
        resetTokenRepository.findByUserEquals(user).ifPresent(rt -> resetTokenRepository.delete(rt));
        userRepository.delete(user);

        return ResponseEntity.ok(new MessageResponse("Deleted user!"));
    }
}
