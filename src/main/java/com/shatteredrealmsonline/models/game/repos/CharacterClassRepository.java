package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.CharacterClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterClassRepository extends JpaRepository<CharacterClass, Long>
{
    Optional<CharacterClass> findByName(String name);
}
