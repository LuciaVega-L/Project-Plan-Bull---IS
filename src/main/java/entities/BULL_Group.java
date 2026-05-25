package main.java.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Group {

    private int idGroup;
    private int quota;

    private BULL_Modality modality;
    private BULL_Professor professor;
    private BULL_Schedule schedule;
    private BULL_Ubication ubication;
    private BULL_MaxCapacity maxCapacity;

    private List<BULL_Registration> registrations;

    public BULL_Group(int idGroup, int quota) {
        OperationResult validation = validateCreation(idGroup, quota);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.idGroup = idGroup;
        this.quota = quota;
        this.registrations = new ArrayList<>();
    }

    // --- Business Logic ---

    public void addRegistration(BULL_Registration registration) {
        OperationResult result = validarNuevaInscripcion(registration);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        registrations.add(registration);
        maxCapacity.incrementEnrollment();
    }

    public void removeRegistration(String idRegistration) {
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getIdRegistration().equals(idRegistration)) {
                registrations.remove(i);
                maxCapacity.decrementEnrollment();
                return;
            }
        }
        throw new IllegalArgumentException("No se encontró la inscripción con id: " + idRegistration);
    }

    public boolean tieneCupoDisponible() {
        return validarCupoDisponible().isSuccess();
    }

    public boolean estaListoParaOperar() {
        return validarConfiguracionCompleta().isSuccess();
    }

    public OperationResult validarConfiguracionCompleta() {
        if (professor == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene profesor asignado.");
        }
        if (schedule == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene horario asignado.");
        }
        if (maxCapacity == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene cupo máximo definido.");
        }
        if (modality == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene modalidad asignada.");
        }
        return OperationResult.ok("El grupo " + idGroup + " está completamente configurado.");
    }

    public OperationResult validarCupoDisponible() {
        if (maxCapacity == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene cupo máximo definido.");
        }
        return maxCapacity.validarInscripcion();
    }

    public OperationResult validarNuevaInscripcion(BULL_Registration registration) {
        if (registration == null) {
            return OperationResult.fail("La inscripción no puede ser nula.");
        }
        OperationResult configResult = validarConfiguracionCompleta();
        if (!configResult.isSuccess()) {
            return configResult;
        }
        OperationResult cupoResult = validarCupoDisponible();
        if (!cupoResult.isSuccess()) {
            return cupoResult;
        }
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getIdRegistration().equals(registration.getIdRegistration())) {
                return OperationResult.fail("Ya existe una inscripción con id " + registration.getIdRegistration() + " en este grupo.");
            }
        }
        return OperationResult.ok("Inscripción válida para el grupo " + idGroup + ".");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(int idGroup, int quota) {
        if (idGroup <= 0) {
            return OperationResult.fail("El id del grupo debe ser mayor a 0.");
        }
        if (quota <= 0) {
            return OperationResult.fail("El cupo del grupo debe ser mayor a 0.");
        }
        return OperationResult.ok("Grupo válido.");
    }

    // --- Getters ---

    public int getIdGroup() { return idGroup; }
    public int getQuota() { return quota; }
    public BULL_Modality getModality() { return modality; }
    public BULL_Professor getProfessor() { return professor; }
    public BULL_Schedule getSchedule() { return schedule; }
    public BULL_Ubication getUbication() { return ubication; }
    public BULL_MaxCapacity getMaxCapacity() { return maxCapacity; }
    public List<BULL_Registration> getRegistrations() { return Collections.unmodifiableList(registrations); }

    // --- Setters (assigned after construction) ---

    public void setModality(BULL_Modality modality) { this.modality = modality; }
    public void setProfessor(BULL_Professor professor) { this.professor = professor; }
    public void setSchedule(BULL_Schedule schedule) { this.schedule = schedule; }
    public void setUbication(BULL_Ubication ubication) { this.ubication = ubication; }
    public void setMaxCapacity(BULL_MaxCapacity maxCapacity) { this.maxCapacity = maxCapacity; }

    @Override
    public String toString() {
        return "BULL_Group{idGroup=" + idGroup + ", quota=" + quota + ", inscritos=" +
                registrations.size() + ", profesor=" + (professor != null ? professor.getName() : "N/A") + "}";
    }
}
