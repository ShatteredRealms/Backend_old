package me.wilsimpson.testmmo.models.web.repos;

import me.wilsimpson.testmmo.models.web.ResetToken;
import me.wilsimpson.testmmo.models.web.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long>
{
    Optional<ResetToken> findByUserEquals(User user);
}

