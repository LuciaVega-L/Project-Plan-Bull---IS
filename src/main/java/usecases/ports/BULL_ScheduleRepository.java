package usecases.ports;

import entities.BULL_Schedule;
import java.util.List;
import java.util.Optional;

public interface BULL_ScheduleRepository {
    Optional<BULL_Schedule> findByDay(String day);
    List<BULL_Schedule> findAll();
    void save(String scheduleId, BULL_Schedule schedule);
    void deleteByScheduleId(String scheduleId);
}
