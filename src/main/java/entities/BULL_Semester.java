package main.java.entities;

import main.java.usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class BULL_Semester {

    private int year;
    private int period;
    private Date startDate;
    private Date endDate;

    private BULL_Course course;
    private List<BULL_Modality> modalities;

    public BULL_Semester(int year, int period, Date startDate, Date endDate) {
        this.modalities = new ArrayList<>();
        OperationResult validation = validateCreation(year, period, startDate, endDate);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.year = year;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // --- Business Logic ---

    public void assignCourse(BULL_Course course) {
        if (course == null) {
            throw new IllegalArgumentException("El curso asignado no puede ser nulo.");
        }
        this.course = course;
    }

    public void addModality(BULL_Modality modality) {
        OperationResult result = validarNuevaModalidad(modality);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        modalities.add(modality);
    }

    public boolean estaEnCurso() {
        return validarVigencia().isSuccess();
    }

    public OperationResult validarVigencia() {
        Date now = new Date();
        if (now.before(startDate)) {
            return OperationResult.fail("El semestre aún no ha iniciado. Inicio: " + startDate);
        }
        if (now.after(endDate)) {
            return OperationResult.fail("El semestre ya finalizó. Fin: " + endDate);
        }
        return OperationResult.ok("El semestre está vigente.");
    }

    public OperationResult validarNuevaModalidad(BULL_Modality modality) {
        if (modality == null) {
            return OperationResult.fail("La modalidad no puede ser nula.");
        }
        for (int i = 0; i < modalities.size(); i++) {
            if (modalities.get(i).getClass().equals(modality.getClass())) {
                return OperationResult.fail("Ya existe una modalidad de tipo '" +
                        modality.getClass().getSimpleName() + "' en este semestre.");
            }
        }
        return OperationResult.ok("Modalidad válida para agregar.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(int year, int period, Date startDate, Date endDate) {
        if (year < 2000) {
            return OperationResult.fail("El año académico debe ser mayor o igual a 2000.");
        }
        if (period < 1 || period > 2) {
            return OperationResult.fail("El periodo debe ser 1 o 2. Valor recibido: " + period);
        }
        if (startDate == null || endDate == null) {
            return OperationResult.fail("Las fechas de inicio y fin no pueden ser nulas.");
        }
        if (!startDate.before(endDate)) {
            return OperationResult.fail("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        return OperationResult.ok("Semestre válido.");
    }

    // --- Getters ---

    public int getYear() { return year; }
    public int getPeriod() { return period; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public BULL_Course getCourse() { return course; }
    public List<BULL_Modality> getModalities() { return Collections.unmodifiableList(modalities); }

    @Override
    public String toString() {
        return "BULL_Semester{year=" + year + ", period=" + period + "}";
    }
}
