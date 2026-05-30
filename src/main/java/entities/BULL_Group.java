package entities;

import usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Group {

    private int idGroup;
    private BULL_Professor professor;
    private BULL_Schedule schedule;
    private BULL_Ubication ubication;   // 0..1 según diagrama (solo presencial lo usa)
    private BULL_MaxCapacity maxCapacity;
    private List<BULL_Registration> registrations;

    public BULL_Group(int idGroup) {
        if (idGroup <= 0) {
            throw new IllegalArgumentException("El id del grupo debe ser mayor a 0.");
        }
        this.idGroup = idGroup;
        this.registrations = new ArrayList<>();
    }
    public int getIdGroup() { return idGroup; }
    public BULL_Professor getProfessor() { return professor; }
    public BULL_Schedule getSchedule() { return schedule; }
    public BULL_Ubication getUbication() { return ubication; }
    public BULL_MaxCapacity getMaxCapacity() { return maxCapacity; }
    public List<BULL_Registration> getRegistrations() { return Collections.unmodifiableList(registrations); }

    public void setProfessor(BULL_Professor professor) { this.professor = professor; }
    public void setSchedule(BULL_Schedule schedule) { this.schedule = schedule; }
    public OperationResult setUbication(BULL_Ubication ubication, BULL_Modality modality) {
        if (modality instanceof BULL_OnSitePresencial && ubication == null) {
            return OperationResult.fail("Un grupo presencial debe tener ubicación.");
        }
        if (!(modality instanceof BULL_OnSitePresencial) && ubication != null) {
            return OperationResult.fail("Solo grupos presenciales pueden tener ubicación.");
        }
        this.ubication = ubication;
        return OperationResult.ok("Ubicación asignada correctamente.");
    }    public void setMaxCapacity(BULL_MaxCapacity maxCapacity) { this.maxCapacity = maxCapacity; }

    public OperationResult addRegistration(BULL_Registration registration) {
        if (registration == null) {
            return OperationResult.fail("La inscripción no puede ser nula.");
        }
        if (maxCapacity == null) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene cupo máximo definido.");
        }
        if (!maxCapacity.tieneCupoDisponible()) {
            return OperationResult.fail("El grupo " + idGroup + " no tiene cupo disponible.");
        }
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getIdRegistration().equals(registration.getIdRegistration())) {
                return OperationResult.fail("Ya existe la inscripción " + registration.getIdRegistration() + " en este grupo.");
            }
        }
        registrations.add(registration);
        maxCapacity.incrementEnrollment();
        return OperationResult.ok("Inscripción agregada al grupo.");
    }

    public OperationResult removeRegistration(String idRegistration) {
        for (int i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getIdRegistration().equals(idRegistration)) {
                registrations.remove(i);
                maxCapacity.decrementEnrollment();
                return OperationResult.ok("Inscripción removida del grupo.");
            }
        }
        return OperationResult.fail("No se encontró la inscripción con id: " + idRegistration);
    }

    public boolean tieneCupoDisponible() {
        return maxCapacity != null && maxCapacity.tieneCupoDisponible();
    }

    @Override
    public String toString() {
        return "Group{idGroup=" + idGroup + ", inscritos=" + registrations.size() + "}";
    }
}
