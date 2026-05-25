package main.java.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain entity representing a Schedule (Horario) in the BULL system.
 * Stores time slots as a Map of day -> time range (e.g., "Lunes" -> "08:00-10:00").
 * One Schedule belongs to exactly one Group.
 *
 * Business rules:
 * - hourlay map must not be null or empty.
 * - A valid time slot key must not be blank.
 * - Cannot add a duplicate day entry.
 */
public class BULL_Schedule {

    private Map<String, String> hourlay;

    public BULL_Schedule() {
        this.hourlay = new HashMap<>();
    }

    public BULL_Schedule(Map<String, String> hourlay) {
        OperationResult validation = validateHourlay(hourlay);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.hourlay = new HashMap<>(hourlay);
    }

    // --- Business Logic ---

    public void addTimeSlot(String day, String timeRange) {
        OperationResult result = validarNuevoSlot(day, timeRange);
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
        hourlay.put(day, timeRange);
    }

    public void removeTimeSlot(String day) {
        if (!hourlay.containsKey(day)) {
            throw new IllegalArgumentException("No existe un horario registrado para el día: " + day);
        }
        hourlay.remove(day);
    }

    public boolean tieneHorarioDefined() {
        return validarHorarioCompleto().isSuccess();
    }

    public OperationResult validarHorarioCompleto() {
        if (hourlay == null || hourlay.isEmpty()) {
            return OperationResult.fail("El horario no tiene franjas horarias definidas.");
        }
        return OperationResult.ok("El horario tiene " + hourlay.size() + " franja(s) definida(s).");
    }

    public OperationResult validarNuevoSlot(String day, String timeRange) {
        if (day == null || day.trim().isEmpty()) {
            return OperationResult.fail("El día no puede estar vacío.");
        }
        if (timeRange == null || timeRange.trim().isEmpty()) {
            return OperationResult.fail("El rango de hora no puede estar vacío.");
        }
        if (hourlay.containsKey(day)) {
            return OperationResult.fail("Ya existe una franja horaria para el día '" + day + "': " + hourlay.get(day));
        }
        return OperationResult.ok("Franja horaria válida.");
    }

    // --- Internal Validation ---

    private OperationResult validateHourlay(Map<String, String> hourlay) {
        if (hourlay == null || hourlay.isEmpty()) {
            return OperationResult.fail("El mapa de horario no puede estar vacío.");
        }
        return OperationResult.ok("Horario válido.");
    }

    // --- Getters ---

    public Map<String, String> getHourlay() { return Collections.unmodifiableMap(hourlay); }

    @Override
    public String toString() {
        return "BULL_Schedule{hourlay=" + hourlay + "}";
    }
}
