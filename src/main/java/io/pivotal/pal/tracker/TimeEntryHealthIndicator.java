package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    private final int MAX_TIME_ENTRIES=5;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        if(timeEntryRepository.list().size() < 5 ){
            builder.up();
        } else
            builder.down();
        return builder.build();
    }
}
