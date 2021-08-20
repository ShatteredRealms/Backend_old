package com.shatteredrealmsonline.models.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shatteredrealmsonline.models.game.repos.SkillRepository;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="character_classes", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class CharacterClass
{
    private static final String SKILL_COST_DELIMINATOR = ":";
    private static final String SKILL_COSTS_DELIMINATOR = ",";

    /**
     * Unique ID for the Breed
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", nullable=false)
    @Getter
    private Long id;

    /**
     * Name of the Breed. Cannot be null.
     */
    @Getter
    @Setter
    @Column(nullable=false)
    private String name;

    /**
     * Description of the Breed
     */
    @Getter
    @Setter
    private String description;

    /**
     * String serialized skill costs. The cost of increasing a skill by 1. A skill and a cost are split by a
     * SKILL_COST_DELIMINATOR and each skillCost will be split by a SKILL_COSTS_DELIMINATOR.
     */
    @JsonIgnore
    private String serializedSkillCosts;

    /**
     * Deserialize skillCosts to a map of the skill and it's cost.
     *
     * @param skillRepository repository of skills
     * @return deserialized map
     */
    public Map<Skill, Integer> getSkillCosts(SkillRepository skillRepository)
    {
        Map<Skill, Integer> skillCosts = new HashMap<>();
        String[] costs = serializedSkillCosts.split(SKILL_COSTS_DELIMINATOR);
        for (String cost : costs)
        {
            String[] skillCost = cost.split(SKILL_COST_DELIMINATOR);
            Skill skill = skillRepository.findByName(skillCost[0]).orElseThrow();
            skillCosts.put(skill, Integer.valueOf(skillCost[1]));
        }

        return skillCosts;
    }

    /**
     * Deserialize skillCosts to a map of the skill name and it's cost.
     *
     * @return deserialized map
     */
    public Map<String, Integer> getSkillCosts()
    {
        Map<String, Integer> result = new HashMap<>();
        String[] costs = serializedSkillCosts.split(SKILL_COSTS_DELIMINATOR);
        for (String cost : costs)
        {
            String[] skillCost = cost.split(SKILL_COST_DELIMINATOR);
            result.put(skillCost[0], Integer.valueOf(skillCost[1]));
        }

        return result;
    }

    public void addSkillCost(String skillName, Integer cost)
    {
        Map<String, Integer> skillCosts = getSkillCosts();
        skillCosts.put(skillName, cost);
        setSkillCostsFromString(skillCosts);
    }

    public void removeSkillCost(String skillName)
    {
        Map<String, Integer> skillCosts = getSkillCosts();
        skillCosts.remove(skillName);
        setSkillCostsFromString(skillCosts);
    }

    public void setSkillCostsFromString(Map<String, Integer> skillCosts)
    {
        StringBuilder builder = new StringBuilder();
        for (String skill : skillCosts.keySet())
        {
            if (!builder.isEmpty())
            {
                builder.append(SKILL_COSTS_DELIMINATOR);
            }

            builder.append(skill);
            builder.append(SKILL_COST_DELIMINATOR);
            builder.append(skillCosts.get(skill));
        }

        serializedSkillCosts = builder.toString();
    }

    public void setSkillCostsFromSkills(Map<Skill, Integer> skillCosts)
    {
        StringBuilder builder = new StringBuilder();
        for (Skill skill : skillCosts.keySet())
        {
            if (!builder.isEmpty())
            {
                builder.append(SKILL_COSTS_DELIMINATOR);
            }

            builder.append(skill.getName());
            builder.append(SKILL_COST_DELIMINATOR);
            builder.append(skillCosts.get(skill));
        }

        serializedSkillCosts = builder.toString();
    }
}
