package main.java.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Student {

    private String universityCode;
    private Boolean specialCondition;
    private String phoneNumber;
    private String mail;
    private int semester;
    private String programs;
    private String surnames;
    private String name;

    private List<BULL_Registration> registrations;

    public BULL_Student(String universityCode, Boolean specialCondition, String phoneNumber,
                        String mail, int semester, String programs, String surnames, String name) {
        this.registrations = new ArrayList<>();
        OperationResult validation = validateCreation(universityCode, mail, semester, name, surnames);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.universityCode = universityCode;
        this.specialCondition = specialCondition;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.semester = semester;
        this.programs = programs;
        this.surnames = surnames;
        this.name = name;
    }

    // --- Business Logic ---

    public void addRegistration(BULL_Registration registration) {
        OperationResult result = validarNuevaInscripcion(registration);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        registrations.add(registration);
    }

    public boolean tieneInscripcionActiva() {
        return validarInscripcionActiva().isSuccess();
    }

    public OperationResult validarInscripcionActiva() {
        for (int i = 0; i < registrations.size(); i++) {
            if ("ACTIVA".equalsIgnoreCase(registrations.get(i).getState())) {
                return OperationResult.ok("El estudiante tiene una inscripción activa.");
            }
        }
        return OperationResult.fail("El estudiante no tiene inscripciones activas.");
    }

    public OperationResult validarNuevaInscripcion(BULL_Registration registration) {
        if (registration == null) {
            return OperationResult.fail("La inscripción no puede ser nula.");
        }
        if (tieneInscripcionActiva()) {
            return OperationResult.fail("El estudiante '" + name + "' ya tiene una inscripción activa. " +
                    "No puede registrarse en otro grupo simultáneamente.");
        }
        return OperationResult.ok("El estudiante puede inscribirse.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(String universityCode, String mail, int semester,
                                              String name, String surnames) {
        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El código universitario no puede estar vacío.");
        }
        if (mail == null || !mail.contains("@")) {
            return OperationResult.fail("El correo electrónico no tiene un formato válido.");
        }
        if (semester < 1) {
            return OperationResult.fail("El semestre debe ser mayor o igual a 1.");
        }
        if (name == null || name.trim().isEmpty()) {
            return OperationResult.fail("El nombre del estudiante no puede estar vacío.");
        }
        if (surnames == null || surnames.trim().isEmpty()) {
            return OperationResult.fail("Los apellidos del estudiante no pueden estar vacíos.");
        }
        return OperationResult.ok("Estudiante válido.");
    }

    // --- Getters ---

    public String getUniversityCode() { return universityCode; }
    public Boolean getSpecialCondition() { return specialCondition; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getMail() { return mail; }
    public int getSemester() { return semester; }
    public String getPrograms() { return programs; }
    public String getSurnames() { return surnames; }
    public String getName() { return name; }
    public List<BULL_Registration> getRegistrations() { return Collections.unmodifiableList(registrations); }

    // --- Setters (only mutable fields) ---

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
        this.mail = mail;
    }
    public void setSpecialCondition(Boolean specialCondition) { this.specialCondition = specialCondition; }

    @Override
    public String toString() {
        return "BULL_Student{universityCode='" + universityCode + "', name='" + name + " " + surnames + "', semester=" + semester + "}";
    }
}
