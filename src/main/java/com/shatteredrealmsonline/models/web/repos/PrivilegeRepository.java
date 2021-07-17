package com.shatteredrealmsonline.models.web.repos;

import com.shatteredrealmsonline.models.web.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository  extends JpaRepository<Privilege, Long>
{
    Optional<Privilege> findByName(String name);
}
