package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Nano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NanoRepository extends JpaRepository<Nano, Long>
{
    Optional<Nano> findByName(String name);
}
