package store.ckin.api.eureka.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 17.
 */

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final ApplicationStatus applicationStatus;


    @Override
    public Health health() {
        if (!applicationStatus.getStatus()) {
            return Health.down().build();
        }

        return Health.up().withDetail("service", "start").build();
    }
}
