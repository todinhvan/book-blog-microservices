package vn.van.identity_service.cronjob;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.repository.BlacklistTokenRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanBlacklistTokenExpired {
    private final BlacklistTokenRepository blacklistTokenRepository;

    @Scheduled(cron = "${app.default.cron.clean-blacklist-token-expired}")
    public void cleanBlacklistTokenExpired() {
        log.info("Cleaning BlacklistTokenExpired...");
        blacklistTokenRepository.findAll().forEach(blacklistToken -> {
            if (blacklistToken.getExpirationTime().before(new Date())) {
                blacklistTokenRepository.delete(blacklistToken);
            }
        });
    }
}
