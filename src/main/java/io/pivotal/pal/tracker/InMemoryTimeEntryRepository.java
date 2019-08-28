package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements  TimeEntryRepository{
    private long id=0;
    private TimeEntry timeEntry;
    List<TimeEntry> timeEntryList = new ArrayList<>();
    Map<Long,TimeEntry> timeEntryMap =new HashMap<>();;

    public InMemoryTimeEntryRepository(TimeEntry timeEntry) {
        this.timeEntry = timeEntry;
    }

    public TimeEntry find(long id){
        TimeEntry timeEntry = timeEntryMap.get(id);
        return timeEntry;
    }

    public TimeEntry create(TimeEntry timeEntry) {
        id++;
        timeEntry.setId(id);
        timeEntryMap.put(id,timeEntry);
        timeEntryList.add(timeEntry);
       return timeEntry;

    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = null;
        updatedTimeEntry = timeEntryMap.get(id);
        if(updatedTimeEntry !=null) {
            updatedTimeEntry.setProjectId(timeEntry.getProjectId());
            updatedTimeEntry.setDate(timeEntry.getDate());
            updatedTimeEntry.setUserId(timeEntry.getUserId());
            updatedTimeEntry.setHours(timeEntry.getHours());
        }
        return  updatedTimeEntry;
    }
    public InMemoryTimeEntryRepository(){

    }

    public List<TimeEntry> list() {
        return timeEntryList;
    }

    public void delete(long id) {
        TimeEntry timeEntry = timeEntryMap.get(id);
        timeEntryMap.remove(id);
        timeEntryList.remove(timeEntry);
    }
}
