package com.shatteredrealmsonline.security;

import com.shatteredrealmsonline.models.web.repos.RoleRepository;
import lombok.Getter;
import lombok.Setter;
import com.shatteredrealmsonline.models.web.Privilege;
import com.shatteredrealmsonline.models.web.Role;
import com.shatteredrealmsonline.models.web.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails
{
    @Getter
    @Setter
    private Long id;

    private final String username;
    private final String password;

    @Getter
    @Setter
    private String email;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String username,
            String password,
            String email,
            Collection<Role> roles)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = getGrantedAuthorities(getPrivileges(roles));
    }

    public static UserDetailsImpl createUser(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles());
    }

    public static UserDetailsImpl createAdmin(String name)
    {
        return  new UserDetailsImpl(-1L,
                name,
                UUID.randomUUID().toString(),
                "admin@"+name,
                new ArrayList<>());
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());

        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
