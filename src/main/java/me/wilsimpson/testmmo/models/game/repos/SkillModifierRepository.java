package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Skill;
import me.wilsimpson.testmmo.models.game.SkillModifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillModifierRepository extends JpaRepository<SkillModifier, Long>
{
    Optional<SkillModifier> findBySkillAndAmount(Skill skill, int amount);
}
