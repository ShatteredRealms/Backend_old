package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long>
{
    Optional<Skill> findByName(String name);
}

