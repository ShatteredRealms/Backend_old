package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long>
{
    Optional<Breed> findByName(String name);
}
