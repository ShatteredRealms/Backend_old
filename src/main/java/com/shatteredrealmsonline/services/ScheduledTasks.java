package com.shatteredrealmsonline.services;

import com.shatteredrealmsonline.models.web.ResetToken;
import com.shatteredrealmsonline.models.web.repos.ResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks
{
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private ResetTokenRepository tokenRepository;

    /**
     * Check the token database once every 24 hours and remove all expired tokens.
     */
    @Scheduled(fixedRate = 1440000)
    public void clearOldExpirationTokens()
    {
        log.info("Starting token cleaning process");
        int numDeleted = 0;
        for(ResetToken token : tokenRepository.findAll())
        {
            if(token.hasTokenExpired())
            {
                tokenRepository.delete(token);
                numDeleted++;
            }
        }
        log.info("{} expired {} been deleted", numDeleted, numDeleted == 1 ? "token has" : "tokens have");
    }

}
