package main.java.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Domain entity representing a Course (Curso) in the BULL system.
 * Course number is strictly between 1 and 4 inclusive.
 *
 * Business rules:
 * - courseNumber must be between 1 and 4.
 * - A course must have at least one semester to be considered active.
 * - Cannot add duplicate semesters (same year + period).
 */
public class BULL_Course {

    private int idModule;
    private int courseNumber;

    private List<BULL_Semester> semesters;

    public BULL_Course(int idModule, int courseNumber) {
        this.semesters = new ArrayList<>();
        OperationResult validation = validateCreation(idModule, courseNumber);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.idModule = idModule;
        this.courseNumber = courseNumber;
    }

    // --- Business Logic ---

    public void addSemester(BULL_Semester semester) {
        OperationResult result = validarNuevoSemestre(semester);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        semesters.add(semester);
    }

    public boolean estaActivo() {
        return validarCursoActivo().isSuccess();
    }

    public OperationResult validarCursoActivo() {
        if (semesters.isEmpty()) {
            return OperationResult.fail("El curso " + courseNumber + " no tiene semestres asignados.");
        }
        return OperationResult.ok("El curso " + courseNumber + " tiene " + semesters.size() + " semestre(s) asignado(s).");
    }

    public OperationResult validarNuevoSemestre(BULL_Semester semester) {
        if (semester == null) {
            return OperationResult.fail("El semestre no puede ser nulo.");
        }
        for (int i = 0; i < semesters.size(); i++) {
            BULL_Semester existing = semesters.get(i);
            if (existing.getYear() == semester.getYear() && existing.getPeriod() == semester.getPeriod()) {
                return OperationResult.fail("Ya existe un semestre para el año " + semester.getYear() +
                        " y periodo " + semester.getPeriod() + " en este curso.");
            }
        }
        return OperationResult.ok("Semestre válido para agregar.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(int idModule, int courseNumber) {
        if (idModule <= 0) {
            return OperationResult.fail("El ID del módulo debe ser mayor a 0.");
        }
        if (courseNumber < 1 || courseNumber > 4) {
            return OperationResult.fail("El número de curso debe estar entre 1 y 4. Valor recibido: " + courseNumber);
        }
        return OperationResult.ok("Curso válido.");
    }

    // --- Getters ---

    public int getIdModule() { return idModule; }
    public int getCourseNumber() { return courseNumber; }
    public List<BULL_Semester> getSemesters() { return Collections.unmodifiableList(semesters); }

    @Override
    public String toString() {
        return "BULL_Course{idModule=" + idModule + ", courseNumber=" + courseNumber + ", semesters=" + semesters.size() + "}";
    }
}
