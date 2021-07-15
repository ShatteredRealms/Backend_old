package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Character;
import me.wilsimpson.testmmo.models.web.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long>
{
    Optional<Character> findByName(String name);

    Optional<Character> findByOwner(User owner);

    Optional<Character> findByOwnerUsername(String username);
}
