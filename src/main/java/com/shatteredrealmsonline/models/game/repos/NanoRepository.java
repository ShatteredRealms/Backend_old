package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.Nano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NanoRepository extends JpaRepository<Nano, Long>
{
    Optional<Nano> findByName(String name);
}
