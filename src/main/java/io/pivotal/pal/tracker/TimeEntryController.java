package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    private TimeEntry timeEntry;
    private List<TimeEntry> timeEntryList;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping(path = "/time-entries", consumes = "application/json", produces = "application/json")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping(path = "/time-entries/{id}", produces = "application/json")
    public ResponseEntity<TimeEntry> read(@PathVariable  long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if (timeEntry == null)
            return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.NOT_FOUND);
        actionCounter.increment();
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        List<TimeEntry> timeEntryList = timeEntryRepository.list();
        return new ResponseEntity<List<TimeEntry>>(timeEntryList, HttpStatus.OK);
    }

    @PutMapping(path = "/time-entries/{id}", produces = "application/json")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = timeEntryRepository.update(id,expected);
        if (timeEntry == null)
            return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.NOT_FOUND);
        actionCounter.increment();
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }

    @DeleteMapping(path = "/time-entries/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable  long id) {
        timeEntryRepository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
