package com.shatteredrealmsonline.models.web;

import com.shatteredrealmsonline.models.game.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

@Entity
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Getter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "reset_token_id", referencedColumnName = "id")
    private ResetToken resetToken;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @Getter
    @Setter
    @OneToMany
    @JoinTable(name = "users_characters")
    private List<Character> characters;

    @Getter
    @Setter
    private String billingAddress;

    @Getter
    @Setter
    private String creditCardNumber;

    @Getter
    @Setter
    private Date creditCardExpirationDate;

    @Getter
    @Setter
    private String creditCardSecurityCode;

    @Getter
    @Setter
    private int maxNumCharacters = 10;

    public User() { }

    public User(String username, String password, String email, Set<Role> roles)
    {
        notNull(username, "Method called with null parameter (username)");
        notNull(password, "Method called with null parameter (password)");
        notNull(email, "Method called with null parameter (email)");

        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public boolean hasRole(Role givenRole)
    {
        for(Role currentRole : roles)
            if(currentRole.getId().equals(givenRole.getId()))
                return true;

        return false;
    }

    public boolean addRole(Role role)
    {
        return roles.add(role);
    }

    public boolean hasResetToken()
    {
        return resetToken != null;
    }
}
