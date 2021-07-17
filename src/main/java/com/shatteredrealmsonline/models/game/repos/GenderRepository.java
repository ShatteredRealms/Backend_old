package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long>
{
    Optional<Gender> findByName(String name);
}
