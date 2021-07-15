package me.wilsimpson.testmmo;

import me.wilsimpson.testmmo.controllers.admin.AdminController;
import me.wilsimpson.testmmo.controllers.all.AuthController;
import me.wilsimpson.testmmo.controllers.user.UserController;
import me.wilsimpson.testmmo.http.requests.LoginRequest;
import me.wilsimpson.testmmo.http.requests.ResetPasswordRequest;
import me.wilsimpson.testmmo.http.requests.SignupRequest;
import me.wilsimpson.testmmo.http.response.JwtResponse;
import me.wilsimpson.testmmo.http.response.MessageResponse;
import me.wilsimpson.testmmo.models.game.repos.*;
import me.wilsimpson.testmmo.models.web.ERole;
import me.wilsimpson.testmmo.models.web.Role;
import me.wilsimpson.testmmo.models.web.User;
import me.wilsimpson.testmmo.models.web.repos.PrivilegeRepository;
import me.wilsimpson.testmmo.models.web.repos.ResetTokenRepository;
import me.wilsimpson.testmmo.models.web.repos.RoleRepository;
import me.wilsimpson.testmmo.models.web.repos.UserRepository;
import me.wilsimpson.testmmo.security.UserDetailsServiceImpl;
import me.wilsimpson.testmmo.utils.DatabaseLoader;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TestmmoApplication.class,
        H2JpaConfig.class})
@ActiveProfiles("test")
class InMemoryTests
{
    /**
     * @TODO: Test all game models and apis
     */

    private static class TestAccount
    {
        static String username = UUID.randomUUID().toString();
        static String password = UUID.randomUUID().toString();
        static String email = UUID.randomUUID().toString()+"@gmail.com";
        static String firstName = UUID.randomUUID().toString();
        static String lastName = UUID.randomUUID().toString();
    }

    // Normal JUNIT tests

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    ResetTokenRepository resetTokenRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SkillModifierRepository skillModifierRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    SlotTypeRepository slotTypeRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserController userController;

    @Autowired
    AdminController adminController;

    @Autowired
    AuthController authController;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    GenderRepository genderRepository;

    static LoginSessionInfo loginSessionInfo = new LoginSessionInfo();

    @Test
    @Order(0)
    void loadDatabase_dataSaved()
    {
        loadDatabase();
        assertTrue(userRepository.count() > 0);
        assertTrue(roleRepository.count() > 0);
        assertTrue(privilegeRepository.count() > 0);
    }

    @Test
    @Order(1)
    void createUser_whenSave_thenGetOk()
    {
        //Create new user and add to the database
        User user = new User("bob2", "bob2", "bob2@gmail.com", Set.of(getRole(ERole.ROLE_USER)));
        userRepository.save(user);
        long id = user.getId();

        //Get the new number of users
        long initialSize = userRepository.count();

        //Check if the information saved correctly
        User dbUser = userRepository.findById(id).orElseThrow();
        assertNotNull(dbUser);
        assertEquals("bob2", dbUser.getUsername());
        assertEquals("bob2@gmail.com", dbUser.getEmail());
        assertTrue(dbUser.hasRole(getRole(ERole.ROLE_USER)));

        //Check if we can update a user, save and retrieve successfully
        Role role = getRole(ERole.ROLE_ADMIN);
        dbUser.addRole(role);

        userRepository.save(dbUser);
        assertEquals(initialSize, userRepository.count());
        dbUser = userRepository.findById(id).orElseThrow();
        assertTrue(dbUser.hasRole(getRole(ERole.ROLE_ADMIN)));
    }

    @Test
    @Order(8)
    void adminController_testGetUsers()
    {
        Iterable<User> users = adminController.getUsers(null);
        User randomUser = getAnyUser();
        assertEquals(userRepository.count(), StreamSupport.stream(users.spliterator(), false).count());

        users = adminController.getUsers(randomUser.getId());
        assertEquals(1, StreamSupport.stream(users.spliterator(), false).count());
        assertSame(randomUser.getId(), users.iterator().next().getId());
    }

    @Test
    @Order(12)
    void adminController_testDeleteUser()
    {
        ResponseEntity<MessageResponse> re = adminController.deleteUser(-1);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());
        assertNotNull(re.getBody().getMessage());
        assertEquals("No user for given id", re.getBody().getMessage());

        long countBefore = userRepository.count();
        re = adminController.deleteUser(getAnyUser().getId());

        assertSame(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals("Deleted user!", re.getBody().getMessage());
        assertEquals(countBefore-1, userRepository.count());
    }

    @Test
    @Order(17)
    void authController_testRegister()
    {
        ResponseEntity<MessageResponse> re = authController.registerUser(null);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        SignupRequest request = new SignupRequest();
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setEmail(getAnyUser().getEmail());
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername(getAnyUser().getUsername());
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setFirstName("firstname");
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setLastName("lastname");
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setPassword("password");
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername(UUID.randomUUID().toString());
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername(getAnyUser().getUsername());
        request.setEmail(UUID.randomUUID().toString()+"@gmail.com");
        re = authController.registerUser(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        long initialCount = userRepository.count();

        request.setUsername(UUID.randomUUID().toString());
        re = authController.registerUser(request);
        assertSame(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals("User registered!", re.getBody().getMessage());
        assertEquals(initialCount+1, userRepository.count());

        loginSessionInfo.username = request.getUsername();
        loginSessionInfo.password = request.getPassword();
    }

    @Test
    @Order(18)
    void authController_testLogin()
    {
        ResponseEntity<JwtResponse> re = authController.loginUser(null);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        LoginRequest loginRequest = new LoginRequest(loginSessionInfo.username, loginSessionInfo.password);
        re = authController.loginUser(loginRequest);
        assertSame(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertNotNull(re.getBody().getToken());
        assertTrue(re.getBody().getRoles().size() > 0);
        assertEquals(loginSessionInfo.username, re.getBody().getUsername());

        loginSessionInfo.jwtToken = re.getBody().getToken();
    }

    @Test
    @Order(19)
    void authController_testResetPassword()
    {
        ResponseEntity<MessageResponse> re = authController.resetPassword(null);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId(-1L);
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setEmail("");
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername("");
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername(UUID.randomUUID().toString());
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        User user = userRepository.findByUsername(loginSessionInfo.username).orElse(null);
        assertNotNull(user);
        String initialPasswordHash = user.getPassword();
        assertTrue(resetTokenRepository.findByUserEquals(user).isEmpty());
        assertFalse(user.hasResetToken());

        request.setToken(UUID.randomUUID().toString());
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());

        request.setUsername(loginSessionInfo.username);
        request.setToken(null);
        re = authController.resetPassword(request);
        assertSame(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals("Token has been generated.", re.getBody().getMessage());
        user = userRepository.findByUsername(loginSessionInfo.username).orElse(null);
        assertNotNull(user);
        assertTrue(user.hasResetToken());

        request.setPassword("newpassword");
        request.setToken(UUID.randomUUID().toString());
        re = authController.resetPassword(request);
        assertSame(HttpStatus.BAD_REQUEST, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals("ERROR: Incorrect reset token", re.getBody().getMessage());

        request.setToken(user.getResetToken().getToken());
        re = authController.resetPassword(request);
        assertSame(HttpStatus.OK, re.getStatusCode());
        assertNotNull(re.getBody());
        assertEquals("Reset successful", re.getBody().getMessage());

        user = userRepository.findByUsername(loginSessionInfo.username).orElse(null);
        assertNotNull(user);
        assertNotEquals(initialPasswordHash, user.getPassword());
        loginSessionInfo.password = "newpassword";
    }


    private Role getRole(ERole role)
    {
        return roleRepository.findByeRole(role).orElseThrow();
    }

    private User getAnyUser()
    {
        return userRepository.findAll().get(0);
    }

    private void loadDatabase()
    {
        DatabaseLoader loader = new DatabaseLoader(
                userRepository,
                roleRepository,
                privilegeRepository,
                itemRepository,
                skillModifierRepository,
                skillRepository,
                slotRepository,
                slotTypeRepository,
                breedRepository,
                genderRepository,
                passwordEncoder);
        loader.run();
    }

    public static class LoginSessionInfo
    {
        String username;
        String password;
        String jwtToken;
    }
}
