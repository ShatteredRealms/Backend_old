package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.Skill;
import com.shatteredrealmsonline.models.game.SkillModifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillModifierRepository extends JpaRepository<SkillModifier, Long>
{
    Optional<SkillModifier> findBySkillAndAmount(Skill skill, int amount);
}
