package entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BULL_Schedule {

    private Map<String, String> hourlay;

    public BULL_Schedule() {
        this.hourlay = new HashMap<>();
    }
    public Map<String, String> getHourlay() { return Collections.unmodifiableMap(hourlay); }

    public void addTimeSlot(String day, String timeRange) {
        hourlay.put(day, timeRange);
    }

    public void removeTimeSlot(String day) {
        hourlay.remove(day);
    }

    public boolean tieneHorario() {
        return !hourlay.isEmpty();
    }

    @Override
    public String toString() {
        return "Schedule{franjas=" + hourlay.size() + "}";
    }
}
