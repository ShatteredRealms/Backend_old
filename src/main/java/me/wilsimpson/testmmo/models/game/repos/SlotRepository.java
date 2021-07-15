package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Slot;
import me.wilsimpson.testmmo.models.game.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long>
{
    Optional<Slot> findByNameAndType(String name, SlotType type);
}
