package main.java.entities;

/**
 * Domain entity representing the Maximum Capacity (Cupo Máximo) of a Group.
 * Tracks total allowed students and current enrollment count.
 *
 * Business rules:
 * - maxCapacity must be greater than 0.
 * - currentEnrollment cannot exceed maxCapacity.
 * - currentEnrollment cannot go below 0.
 */
public class BULL_MaxCapacity {

    private int maxCapacity;
    private int currentEnrollment;

    public BULL_MaxCapacity(int maxCapacity) {
        OperationResult validation = validateCreation(maxCapacity);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = 0;
    }

    // --- Business Logic ---

    public void incrementEnrollment() {
        OperationResult result = validarInscripcion();
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        currentEnrollment++;
    }

    public void decrementEnrollment() {
        OperationResult result = validarDecremento();
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        currentEnrollment--;
    }

    public boolean tieneCupoDisponible() {
        return validarInscripcion().isSuccess();
    }

    public int getCuposRestantes() {
        return maxCapacity - currentEnrollment;
    }

    public OperationResult validarInscripcion() {
        if (currentEnrollment >= maxCapacity) {
            return OperationResult.fail("Cupo lleno. Máximo: " + maxCapacity + ", inscritos: " + currentEnrollment + ".");
        }
        return OperationResult.ok("Cupo disponible. Restantes: " + getCuposRestantes() + " de " + maxCapacity + ".");
    }

    public OperationResult validarDecremento() {
        if (currentEnrollment <= 0) {
            return OperationResult.fail("No hay inscritos que remover. Enrollment actual: " + currentEnrollment);
        }
        return OperationResult.ok("Decremento válido.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(int maxCapacity) {
        if (maxCapacity <= 0) {
            return OperationResult.fail("El cupo máximo debe ser mayor a 0. Valor recibido: " + maxCapacity);
        }
        return OperationResult.ok("Cupo máximo válido.");
    }

    // --- Getters ---

    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentEnrollment() { return currentEnrollment; }

    @Override
    public String toString() {
        return "BULL_MaxCapacity{maxCapacity=" + maxCapacity + ", currentEnrollment=" + currentEnrollment + ", restantes=" + getCuposRestantes() + "}";
    }
}
