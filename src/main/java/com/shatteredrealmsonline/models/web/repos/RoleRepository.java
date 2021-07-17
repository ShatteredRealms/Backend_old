package com.shatteredrealmsonline.models.web.repos;


import com.shatteredrealmsonline.models.web.ERole;
import com.shatteredrealmsonline.models.web.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByeRole(ERole role);
}
