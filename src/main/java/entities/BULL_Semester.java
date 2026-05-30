package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BULL_Semester {

    private int year;
    private int period;
    private Date startDate;
    private Date endDate;
    private List<BULL_Modality> modalities;

    public BULL_Semester(int year, int period, Date startDate, Date endDate) {
        if (year < 2000) {
            throw new IllegalArgumentException("El año debe ser mayor o igual a 2000.");
        }
        if (period < 1 || period > 2) {
            throw new IllegalArgumentException("El periodo debe ser 1 o 2.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (!startDate.before(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        this.year = year;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.modalities = new ArrayList<>();
    }
    public int getYear() { return year; }
    public int getPeriod() { return period; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public List<BULL_Modality> getModalities() { return Collections.unmodifiableList(modalities); }

    public void addModality(BULL_Modality modality) {
        modalities.add(modality);
    }

    public boolean estaVigente() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    @Override
    public String toString() {
        return "Semester{year=" + year + ", period=" + period + "}";
    }
}
