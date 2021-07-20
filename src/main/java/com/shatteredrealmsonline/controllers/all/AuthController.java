package com.shatteredrealmsonline.controllers.all;

import com.shatteredrealmsonline.http.requests.LoginRequest;
import com.shatteredrealmsonline.http.requests.ResetPasswordRequest;
import com.shatteredrealmsonline.http.requests.SignupRequest;
import com.shatteredrealmsonline.http.response.JwtResponse;
import com.shatteredrealmsonline.http.response.MessageResponse;
import com.shatteredrealmsonline.models.web.ERole;
import com.shatteredrealmsonline.models.web.ResetToken;
import com.shatteredrealmsonline.models.web.User;
import com.shatteredrealmsonline.models.web.repos.ResetTokenRepository;
import com.shatteredrealmsonline.models.web.repos.RoleRepository;
import com.shatteredrealmsonline.models.web.repos.UserRepository;
import com.shatteredrealmsonline.security.UserDetailsImpl;
import com.shatteredrealmsonline.services.EmailServiceImpl;
import com.shatteredrealmsonline.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/api/v1/auth")
public class AuthController
{
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${shatteredrealmsonline.app.senderEmail}")
    private String senderEmail;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping(path="/register")
    public @ResponseBody
    ResponseEntity<MessageResponse> registerUser(@RequestBody SignupRequest request)
    {
        if(request == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Request cannot be empty"));

        if(request.getEmail() == null || request.getEmail().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Email cannot be empty"));

        if(request.getUsername() == null || request.getUsername().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Username cannot be empty"));

        if(request.getFirstName() == null || request.getFirstName().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Firstname cannot be empty"));

        if(request.getLastName() == null || request.getLastName().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Lastname cannot be empty"));

        if(request.getPassword() == null || request.getPassword().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Password cannot be empty"));

        if(userRepository.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Email is taken!"));

        if(userRepository.existsByUsername(request.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Username is taken!"));

        User newUser = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                Set.of(roleRepository.findByeRole(ERole.ROLE_USER).orElseThrow())
                );
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User registered!"));
    }

    @PostMapping(path="/login")
    public @ResponseBody
    ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest)
    {
        if(loginRequest == null)
            return ResponseEntity.badRequest().body(new JwtResponse("Request cannot be null"));

        if(loginRequest.getUsername() == null || loginRequest.getUsername().equals(""))
            return ResponseEntity.badRequest().body(new JwtResponse("Username cannot be empty"));

        if(loginRequest.getPassword() == null || loginRequest.getPassword().equals(""))
            return ResponseEntity.badRequest().body(new JwtResponse("Password cannot be empty"));

        //Set up the auth
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        //Get the user roles
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<String> userRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        //Send the response
        return ResponseEntity.ok(
                new JwtResponse(
                        jwtUtils.generateJwtToken(auth),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userRoles
                ));
    }


    @PostMapping(path="/resetPassword")
    public @ResponseBody
    ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request)
    {
        if(request == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Request cannot be null"));

        User user = null;
        if(request.getUserId() != null)
            user = userRepository.findById(request.getUserId()).orElse(null);

        if(user == null && request.getEmail() != null)
            user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null && request.getUsername() != null)
            user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if(user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("ERROR: User not found"));

        if(request.getToken() == null || request.getToken().equals(""))
        {
            ResetToken token;
            if(user.hasResetToken())
            {
                token = user.getResetToken();
                token.refreshToken();
            }
            else
            {
                token = new ResetToken(user);
                user.setResetToken(token);
                userRepository.saveAndFlush(user);
            }

            sendTokenEmail(user, token);
            return ResponseEntity.ok(new MessageResponse("Token has been generated."));
        }
        else
        {
            if(user.hasResetToken())
            {
                ResetToken resetToken = user.getResetToken();
                if(resetToken.getToken().equals(request.getToken()))
                {
                    if(resetToken.hasTokenExpired())
                    {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("ERROR: Token has expired. Request a new token."));
                    }
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    resetTokenRepository.delete(resetToken);
                    resetTokenRepository.flush();
                    userRepository.saveAndFlush(user);

                    return ResponseEntity.ok(new MessageResponse("Reset successful"));
                }
                return ResponseEntity.badRequest().body(new MessageResponse("ERROR: Incorrect reset token"));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("ERROR: No request password request for given user"));
        }
    }

    private void sendTokenEmail(User user, ResetToken token)
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("no-reply@shatteredrealmsonline.com");
        msg.setTo(user.getEmail());
        msg.setText(
                "Hello " + user.getUsername()
                        + ",\n\n You have requested a password reset. Please click the following link to reset your password. If you did not request to reset your password you can ignore this email."
                        + "\n\n <a href='"+resetPasswordLink(user.getEmail(), token.getToken())+"'>Reset Password</a>"
                        + "\n\nThanks,\nShattered Realms Online");

        try
        {
            this.emailService.send(msg);
        }
        catch (MailException ex)
        {
            log.error(ex.getMessage());
        }
    }

    private String resetPasswordLink(String email, String token)
    {
        return String.format("http://shatteredrealmsonline.com/resetPassword?email=%s&token=%s", email, token);
    }
}
