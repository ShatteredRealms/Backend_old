package com.shatteredrealmsonline.models.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
@Table(name="roles")
public class Role
{
    public Role() { }

    public Role(ERole eRole)
    {
        this.eRole = eRole;
    }

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
    private Long id;

    @Getter
    @Setter
    private ERole eRole;

    @Getter
    @ManyToMany
    @JsonIgnore
	@JoinTable(
		name="users_roles", 
		joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id")
	)
    private Set<User> users;

    @Getter
	@Setter
    @ManyToMany
	@JoinTable(
		name="roles_privileges", 
		joinColumns = @JoinColumn(name="role_id", nullable=false, referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="privilege_id", nullable=false, referencedColumnName="id")
	)
    private Collection<Privilege> privileges;
}
