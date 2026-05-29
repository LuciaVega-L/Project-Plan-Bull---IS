package entities;

public class BULL_MaxCapacity {

    private int maxCapacity;
    private int currentEnrollment;

    public BULL_MaxCapacity(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0.");
        }
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = 0;
    }

    public void incrementEnrollment() {
        currentEnrollment++;
    }

    public void decrementEnrollment() {
        currentEnrollment--;
    }

    public boolean tieneCupoDisponible() {
        return currentEnrollment < maxCapacity;
    }

    public int getCuposRestantes() {
        return maxCapacity - currentEnrollment;
    }

    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentEnrollment() { return currentEnrollment; }

    @Override
    public String toString() {
        return "MaxCapacity{max=" + maxCapacity + ", inscritos=" + currentEnrollment + "}";
    }
}
