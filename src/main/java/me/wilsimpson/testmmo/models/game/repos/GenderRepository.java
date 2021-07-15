package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long>
{
    Optional<Gender> findByName(String name);
}
