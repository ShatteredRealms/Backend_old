package me.wilsimpson.testmmo.utils;

import me.wilsimpson.testmmo.models.game.*;
import me.wilsimpson.testmmo.models.game.repos.*;
import me.wilsimpson.testmmo.models.web.ERole;
import me.wilsimpson.testmmo.models.web.Privilege;
import me.wilsimpson.testmmo.models.web.Role;
import me.wilsimpson.testmmo.models.web.User;
import me.wilsimpson.testmmo.models.web.repos.PrivilegeRepository;
import me.wilsimpson.testmmo.models.web.repos.RoleRepository;
import me.wilsimpson.testmmo.models.web.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DatabaseLoader implements CommandLineRunner
{
    private final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final PrivilegeRepository privilegeRepository;

    private final ItemRepository itemRepository;

    private final SkillModifierRepository skillModifierRepository;

    private final SkillRepository skillRepository;

    private final SlotRepository slotRepository;

    private final SlotTypeRepository slotTypeRepository;

    private final GenderRepository genderRepository;

    private final BreedRepository breedRepository;

    @Autowired
    public DatabaseLoader(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PrivilegeRepository privilegeRepository,
            ItemRepository itemRepository,
            SkillModifierRepository skillModifierRepository,
            SkillRepository skillRepository,
            SlotRepository slotRepository,
            SlotTypeRepository slotTypeRepository,
            BreedRepository breedRepository,
            GenderRepository genderRepository,
            PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.privilegeRepository = privilegeRepository;
        this.itemRepository = itemRepository;
        this.skillModifierRepository = skillModifierRepository;
        this.skillRepository = skillRepository;
        this.slotRepository = slotRepository;
        this.breedRepository = breedRepository;
        this.genderRepository = genderRepository;
        this.slotTypeRepository = slotTypeRepository;

    }

    @Override
    public void run(String... strings)
    {
        log.info("Starting game database initialization");
        initGame();
        log.info("Finished game database initialization");

        log.info("Starting web database initialization");
        initWeb();
        log.info("Finished web database initialization");
    }

    private void initGame()
    {
        createSlotTypeIfNotFound("Inventory");
        createSlotTypeIfNotFound("Equipable");
        createSlotTypeIfNotFound("Corpse");
        createSlotTypeIfNotFound("Bank");
        createSlotTypeIfNotFound("Mail");
        createSlotTypeIfNotFound("Bag");

        SlotType invSlotType = slotTypeRepository.findByName("Inventory").orElseThrow();
        SlotType equipSlotType = slotTypeRepository.findByName("Equipable").orElseThrow();

        // Add 27 inv slots
        for (long i=0; i<27; i++)
            createSlotIfNotFound(i, invSlotType, null);

        createSlotIfNotFound(27L, equipSlotType, "Right Hand");
        createSlotIfNotFound(28L, equipSlotType, "Left Hand");
        createSlotIfNotFound(29L, equipSlotType, "Head");
        createSlotIfNotFound(30L, equipSlotType, "Neck");
        createSlotIfNotFound(31L, equipSlotType, "Back");
        createSlotIfNotFound(32L, equipSlotType, "Right Shoulder");
        createSlotIfNotFound(33L, equipSlotType, "Chest");
        createSlotIfNotFound(34L, equipSlotType, "Left Shoulder");
        createSlotIfNotFound(35L, equipSlotType, "Right Arm");
        createSlotIfNotFound(36L, equipSlotType, "Hands");
        createSlotIfNotFound(37L, equipSlotType, "Left Arm");
        createSlotIfNotFound(38L, equipSlotType, "Right Wrist");
        createSlotIfNotFound(39L, equipSlotType, "Legs");
        createSlotIfNotFound(40L, equipSlotType, "Left Wrist");
        createSlotIfNotFound(41L, equipSlotType, "Right Ring");
        createSlotIfNotFound(42L, equipSlotType, "Feet");
        createSlotIfNotFound(43L, equipSlotType, "Left Ring");

        createSkillIfNotFound("Strength");
        createSkillIfNotFound("Stamina");
        createSkillIfNotFound("Agility");
        createSkillIfNotFound("Sense");
        createSkillIfNotFound("Intelligence");
        createSkillIfNotFound("Psychic");
        createSkillIfNotFound("Pistol");
        createSkillIfNotFound("Rifle");
        createSkillIfNotFound("1-Handed");
        createSkillIfNotFound("Move Speed");
        createSkillIfNotFound("Matter Creation");

        createSkillModifierIfNotFound(skillRepository.findByName("Strength").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Stamina").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Agility").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Sense").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Intelligence").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Psychic").orElseThrow(), 5);
        createSkillModifierIfNotFound(skillRepository.findByName("Move Speed").orElseThrow(), 100);
        createSkillModifierIfNotFound(skillRepository.findByName("Matter Creation").orElseThrow(), 10);

        createGenderIfNotFound("Male", "Sample description");
        createGenderIfNotFound("Female", "Sample description");
        createGenderIfNotFound("Neut", "Sample description");

        createBreedIfNotFound("Solitus", "Sample description");
        createBreedIfNotFound("Opifex", "Sample description");
        createBreedIfNotFound("Nanomage", "Sample description");
        createBreedIfNotFound("Atrox", "Sample description");

        if (itemRepository.findByName("The testor").isEmpty())
        {
            Item i = new Item();
            i.setCritBonus(100);
            i.setDescription("The best description you've ever seen.");
            i.setName("The testor");
            i.setMinDamage(1);
            i.setMaxDamage(10);
            i.setEquipableSlots(Set.of(slotRepository.findByNameAndType("Right Hand", equipSlotType).orElseThrow()));
            i.setEquipableSlots(Set.of(slotRepository.findByNameAndType("Left Hand", equipSlotType).orElseThrow()));
            i.setModifiers(Set.of(skillModifierRepository.findAll().get(6), skillModifierRepository.findAll().get(7)));
            i.setStackSize(0);
            i.setModifiers(Set.of(
                    skillModifierRepository.findAll().get(0),
                    skillModifierRepository.findAll().get(1),
                    skillModifierRepository.findAll().get(2),
                    skillModifierRepository.findAll().get(3),
                    skillModifierRepository.findAll().get(4),
                    skillModifierRepository.findAll().get(5)
            ));
            i.setQuality("2");
            itemRepository.saveAndFlush(i);
        }
    }

    private void initWeb()
    {
        Privilege privilege1 = createPrivilegeIfNotFound("ADMIN_PRIVILEGES_ALL_RW");
        Privilege privilege2 = createPrivilegeIfNotFound("MOD_PRIVILEGES_ALL_RW");
        Privilege privilege3 = createPrivilegeIfNotFound("USER_PRIVILEGES_ALL_RW");

        Role adminRole = createRoleIfNotFound(ERole.ROLE_ADMIN, Collections.singleton(privilege1));
        Role modRole = createRoleIfNotFound(ERole.ROLE_MODERATOR, Collections.singleton(privilege2));
        Role userRole = createRoleIfNotFound(ERole.ROLE_USER, Collections.singleton(privilege3));
        roleRepository.flush();

        String password = "password";
        List<User> defaultUsers = List.of(
                new User("wil", passwordEncoder.encode(password), "wil@wildev.me", Set.of(adminRole, modRole, userRole)),
                new User("bob", passwordEncoder.encode(password), "bob@wildev.me", Set.of(modRole, userRole)),
                new User("tom", passwordEncoder.encode(password), "tom@wildev.me", Set.of(userRole))
        );


        for(User user : defaultUsers)
        {
            if(userRepository.findByUsername(user.getUsername()).isEmpty())
                this.userRepository.save(user);
        }
        this.userRepository.flush();
    }

    private void createGenderIfNotFound(String name, String description)
    {
        Optional<Gender> oGender = genderRepository.findByName(name);
        if (oGender.isEmpty())
        {
            Gender gender = new Gender();
            gender.setName(name);
            gender.setDescription(description);
            genderRepository.saveAndFlush(gender);
        }
    }

    private void createBreedIfNotFound(String name, String description)
    {
        Optional<Breed> oBreed = breedRepository.findByName(name);
        if (oBreed.isEmpty())
        {
            Breed breed = new Breed();
            breed.setName(name);
            breed.setDescription(description);
            breedRepository.saveAndFlush(breed);
        }
    }

    private void createSlotTypeIfNotFound(String name)
    {
        Optional<SlotType> oSlotType = slotTypeRepository.findByName(name);
        if (oSlotType.isEmpty())
        {
            slotTypeRepository.saveAndFlush(new SlotType(name));
        }
    }

    private void createSlotIfNotFound(Long id, SlotType type, String name)
    {
        Optional<Slot> oSlot = slotRepository.findById(id);
        if (oSlot.isEmpty())
        {
            Slot slot = new Slot(id, type);

            if (name != null)
                slot.setName(name);

            slotRepository.saveAndFlush(slot);
        }
    }

    private void createSkillIfNotFound(String name)
    {
        Optional<Skill> oSkill = skillRepository.findByName(name);

        if (oSkill.isEmpty())
        {
           skillRepository.saveAndFlush(new Skill(name));
        }
    }

    private void createSkillModifierIfNotFound(Skill skill, int amount)
    {
        Optional<SkillModifier> oSkillModifier = skillModifierRepository.findBySkillAndAmount(skill, amount);

        if(oSkillModifier.isEmpty())
        {
            skillModifierRepository.saveAndFlush(new SkillModifier(skill, amount));
        }
    }

    private Privilege createPrivilegeIfNotFound(String name)
    {
        Privilege privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null)
        {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(ERole erole, Collection<Privilege> privileges)
    {
        Role role = roleRepository.findByeRole(erole).orElse(null);
        if (role == null) {
            role = new Role(erole);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
