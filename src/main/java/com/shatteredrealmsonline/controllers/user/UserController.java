package com.shatteredrealmsonline.controllers.user;


import com.shatteredrealmsonline.controllers.admin.AdminController;
import com.shatteredrealmsonline.http.requests.CreateCharacterRequest;
import com.shatteredrealmsonline.http.response.ErrorResponse;
import com.shatteredrealmsonline.http.response.GenericResponse;
import com.shatteredrealmsonline.http.response.ResponseErrorCode;
import com.shatteredrealmsonline.models.game.Breed;
import com.shatteredrealmsonline.models.game.Character;
import com.shatteredrealmsonline.models.game.CharacterClass;
import com.shatteredrealmsonline.models.game.Gender;
import com.shatteredrealmsonline.models.game.repos.BreedRepository;
import com.shatteredrealmsonline.models.game.repos.CharacterClassRepository;
import com.shatteredrealmsonline.models.game.repos.CharacterRepository;
import com.shatteredrealmsonline.models.game.repos.GenderRepository;
import com.shatteredrealmsonline.models.game.util.PlayerPosition;
import com.shatteredrealmsonline.models.web.User;
import com.shatteredrealmsonline.models.web.repos.UserRepository;
import com.shatteredrealmsonline.security.UserDetailsImpl;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path="/api/v1/user")
public class UserController
{
    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private static Pattern namePattern = Pattern.compile("^[a-zA-Z0-9]*$");

    private final UserRepository userRepository;

    private final BreedRepository breedRepository;

    private final GenderRepository genderRepository;

    private final CharacterRepository characterRepository;

    private final CharacterClassRepository characterClassRepository;

    @Value("${shatteredrealmsonline.name.minLength}")
    private int nameMinLength;

    @Value("${shatteredrealmsonline.name.maxLength}")
    private int nameMaxLength;

    public UserController(UserRepository userRepository, BreedRepository breedRepository, GenderRepository genderRepository, CharacterRepository characterRepository, CharacterClassRepository characterClassRepository) {
        this.userRepository = userRepository;
        this.breedRepository = breedRepository;
        this.genderRepository = genderRepository;
        this.characterRepository = characterRepository;
        this.characterClassRepository = characterClassRepository;
    }

    @GetMapping(path="/game/characters")
    public @ResponseBody
    ResponseEntity<Iterable<Character>> getCharacters()
    {
        return ResponseEntity.ok(getCurrentUser().getCharacters());
    }

    @PostMapping(path="/game/characters")
    public @ResponseBody
    ResponseEntity<GenericResponse> createCharacter(@RequestBody CreateCharacterRequest request)
    {
        GenericResponse response = new GenericResponse();

        // Validate all request data is present
        if (request == null)
        {
            response.setError(new ErrorResponse(ResponseErrorCode.MISSING_REQUEST_CONTENT, "Body cannot be null"));
            return ResponseEntity.ok().body(response);
        }

        if (request.getName() == null || request.getName().equals(""))
        {
            response.setError(new ErrorResponse(ResponseErrorCode.MISSING_REQUEST_CONTENT, "Missing name"));
            return ResponseEntity.ok().body(response);
        }

        if (request.getBreedName() == null || request.getBreedName().equals(""))
        {
            response.setError(new ErrorResponse(ResponseErrorCode.MISSING_REQUEST_CONTENT, "Missing breed"));
            return ResponseEntity.ok().body(response);
        }

        if (request.getGenderName() == null || request.getGenderName().equals(""))
        {
            response.setError(new ErrorResponse(ResponseErrorCode.MISSING_REQUEST_CONTENT, "Missing gender"));
            return ResponseEntity.ok().body(response);
        }

        if (request.getClassName() == null || request.getClassName().equals(""))
        {
            response.setError(new ErrorResponse(ResponseErrorCode.MISSING_REQUEST_CONTENT, "Missing class"));
            return ResponseEntity.ok().body(response);
        }

        // Validate breed and gender are valid
        Optional<Breed> breed = breedRepository.findByName(request.getBreedName());
        if (breed.isEmpty())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.NOT_FOUND, "Could not find breed: "+request.getBreedName()));
            return ResponseEntity.ok().body(response);
        }

        Optional<Gender> gender = genderRepository.findByName(request.getGenderName());
        if (gender.isEmpty())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.NOT_FOUND, "Could not find gender: "+request.getGenderName()));
            return ResponseEntity.ok().body(response);
        }

        Optional<CharacterClass> characterClass = characterClassRepository.findByName(request.getClassName());
        if (characterClass.isEmpty())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.NOT_FOUND, "Could not find class: "+request.getClassName()));
            return ResponseEntity.ok().body(response);
        }

        // Validate name is not taken
        if (characterRepository.findByName(request.getName()).isPresent())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.ALREADY_EXISTS, "Name is taken: "+request.getName()));
            return ResponseEntity.ok().body(response);
        }

        // Validate name
        if (java.lang.Character.isDigit(request.getName().charAt(0)))
        {
            response.setError(new ErrorResponse(ResponseErrorCode.BAD_NAME, "Name cannot start with number: "+request.getName()));
            return ResponseEntity.ok().body(response);
        }

        if (!namePattern.matcher(request.getName()).find())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.BAD_NAME, "Name contains invalid characters: "+request.getName()));
            return ResponseEntity.ok().body(response);
        }

        if (request.getName().length() > nameMaxLength)
        {
            response.setError(new ErrorResponse(ResponseErrorCode.BAD_NAME, "Name length exceeds "+nameMaxLength+" characters"));
            return ResponseEntity.ok().body(response);
        }

        if (request.getName().length() < nameMinLength)
        {
            response.setError(new ErrorResponse(ResponseErrorCode.BAD_NAME, "Name must be at least "+nameMinLength+" characters"));
            return ResponseEntity.ok().body(response);
        }

        // Validate user can have more characters
        User user = getCurrentUser();
        if (user.getCharacters().size() >= user.getMaxNumCharacters())
        {
            response.setError(new ErrorResponse(ResponseErrorCode.BAD_NAME, "Not enough remaining character slots"));
            return ResponseEntity.ok().body(response);
        }

        // Character creation is valid, create new character
        Character c = new Character();
        c.setName(request.getName());
        c.setBreed(breed.get());
        c.setGender(gender.get());
        c.setCharacterClass(characterClass.get());
        c.setPosition(new PlayerPosition());
        c.setOwner(user);
        characterRepository.saveAndFlush(c);
        user.addCharacter(c);
        userRepository.saveAndFlush(user);

        response.setSuccess("Character created!");
        return ResponseEntity.ok(response);
    }

    private User getCurrentUser()
    {
        return userRepository.getById(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }


}
