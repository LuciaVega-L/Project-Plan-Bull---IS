package main.java.entities;

import main.java.usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BULL_Modality {

    private String mode;

    private BULL_Semester semester;
    private List<BULL_Group> groups;

    public BULL_Modality(String mode) {
        if (mode == null || mode.trim().isEmpty()) {
            throw new IllegalArgumentException("El modo de la modalidad no puede estar vacío.");
        }
        this.mode = mode;
        this.groups = new ArrayList<>();
    }

    // --- Business Logic ---

    public void assignSemester(BULL_Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("El semestre asignado no puede ser nulo.");
        }
        this.semester = semester;
    }

    public void addGroup(BULL_Group group) {
        OperationResult result = validarNuevoGrupo(group);
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        groups.add(group);
    }

    public boolean tieneGruposDisponibles() {
        return validarDisponibilidadGrupos().isSuccess();
    }

    public OperationResult validarDisponibilidadGrupos() {
        if (groups.isEmpty()) {
            return OperationResult.fail("La modalidad '" + mode + "' no tiene grupos disponibles.");
        }
        int gruposConCupo = 0;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).tieneCupoDisponible()) {
                gruposConCupo++;
            }
        }
        if (gruposConCupo == 0) {
            return OperationResult.fail("Todos los grupos de la modalidad '" + mode + "' están llenos.");
        }
        return OperationResult.ok("La modalidad '" + mode + "' tiene " + gruposConCupo + " grupo(s) con cupo disponible.");
    }

    public OperationResult validarNuevoGrupo(BULL_Group group) {
        if (group == null) {
            return OperationResult.fail("El grupo no puede ser nulo.");
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getIdGroup() == group.getIdGroup()) {
                return OperationResult.fail("Ya existe un grupo con id " + group.getIdGroup() + " en esta modalidad.");
            }
        }
        return OperationResult.ok("Grupo válido para agregar.");
    }

    // --- Getters ---

    public String getMode() { return mode; }
    public BULL_Semester getSemester() { return semester; }
    public List<BULL_Group> getGroups() { return Collections.unmodifiableList(groups); }

    @Override
    public String toString() {
        return "BULL_Modality{mode='" + mode + "', groups=" + groups.size() + "}";
    }
}
