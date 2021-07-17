package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.Slot;
import com.shatteredrealmsonline.models.game.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long>
{
    Optional<Slot> findByNameAndType(String name, SlotType type);
}
