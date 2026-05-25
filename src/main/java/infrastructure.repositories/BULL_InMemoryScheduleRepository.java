package main.java.infrastructure.repositories;

import main.java.entities.BULL_Schedule;
import main.java.usecases.ports.BULL_ScheduleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryScheduleRepository implements BULL_ScheduleRepository {

    // BULL_Schedule does not have a natural ID; we use an external scheduleId key
    private final Map<String, BULL_Schedule> storage = new HashMap<>();

    @Override
    public Optional<BULL_Schedule> findByDay(String day) {
        for (BULL_Schedule schedule : storage.values()) {
            if (schedule.getHourlay().containsKey(day)) {
                return Optional.of(schedule);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<BULL_Schedule> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(String scheduleId, BULL_Schedule schedule) {
        storage.put(scheduleId, schedule);
    }

    @Override
    public void deleteByScheduleId(String scheduleId) {
        storage.remove(scheduleId);
    }
}
