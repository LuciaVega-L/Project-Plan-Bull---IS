package main.java.entities;

import main.java.usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BULL_Professor {

    private String idTeaching;
    private String mail;
    private String phoneNumber;
    private String name;

    private List<BULL_Group> groups;

    public BULL_Professor(String idTeaching, String mail, String phoneNumber, String name) {
        this.groups = new ArrayList<>();
        OperationResult validation = validateCreation(idTeaching, mail, name);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.idTeaching = idTeaching;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    // --- Business Logic ---

    public void assignGroup(BULL_Group group) {
        OperationResult result = validarAsignacionGrupo(group);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        groups.add(group);
    }

    public void removeGroup(int idGroup) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getIdGroup() == idGroup) {
                groups.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("El profesor no tiene asignado el grupo con id: " + idGroup);
    }

    public boolean tieneGruposAsignados() {
        return validarTieneGrupos().isSuccess();
    }

    public OperationResult validarTieneGrupos() {
        if (groups.isEmpty()) {
            return OperationResult.fail("El profesor '" + name + "' no tiene grupos asignados.");
        }
        return OperationResult.ok("El profesor '" + name + "' tiene " + groups.size() + " grupo(s) asignado(s).");
    }

    public OperationResult validarAsignacionGrupo(BULL_Group group) {
        if (group == null) {
            return OperationResult.fail("El grupo no puede ser nulo.");
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getIdGroup() == group.getIdGroup()) {
                return OperationResult.fail("El profesor '" + name + "' ya está asignado al grupo " + group.getIdGroup() + ".");
            }
        }
        return OperationResult.ok("Asignación de grupo válida.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(String idTeaching, String mail, String name) {
        if (idTeaching == null || idTeaching.trim().isEmpty()) {
            return OperationResult.fail("El ID docente no puede estar vacío.");
        }
        if (mail == null || !mail.contains("@")) {
            return OperationResult.fail("El correo electrónico del profesor no tiene un formato válido.");
        }
        if (name == null || name.trim().isEmpty()) {
            return OperationResult.fail("El nombre del profesor no puede estar vacío.");
        }
        return OperationResult.ok("Profesor válido.");
    }

    // --- Getters ---

    public String getIdTeaching() { return idTeaching; }
    public String getMail() { return mail; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getName() { return name; }
    public List<BULL_Group> getGroups() { return Collections.unmodifiableList(groups); }

    // --- Setters (only mutable fields) ---

    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
        this.mail = mail;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "BULL_Professor{idTeaching='" + idTeaching + "', name='" + name + "', groups=" + groups.size() + "}";
    }
}
