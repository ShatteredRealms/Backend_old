package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.Character;
import com.shatteredrealmsonline.models.web.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long>
{
    Optional<Character> findByName(String name);

    Optional<Character> findByOwner(User owner);

    Optional<Character> findByOwnerUsername(String username);
}
