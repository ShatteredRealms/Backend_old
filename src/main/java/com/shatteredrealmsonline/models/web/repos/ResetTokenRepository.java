package com.shatteredrealmsonline.models.web.repos;

import com.shatteredrealmsonline.models.web.ResetToken;
import com.shatteredrealmsonline.models.web.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long>
{
    Optional<ResetToken> findByUserEquals(User user);
}

