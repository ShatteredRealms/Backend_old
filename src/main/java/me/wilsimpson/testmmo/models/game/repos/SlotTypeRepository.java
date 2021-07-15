package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotTypeRepository extends JpaRepository<SlotType, Long>
{
    Optional<SlotType> findByName(String name);
}
