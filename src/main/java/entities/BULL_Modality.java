package entities;

import usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public abstract class BULL_Modality {

    private List<BULL_Group> groups;

    protected BULL_Modality() {
        this.groups = new ArrayList<>();
    }

    public abstract String getMode(); // cada subclase define su tipo

    public OperationResult addGroup(BULL_Group group) {
        if (group == null) {
            return OperationResult.fail("El grupo no puede ser nulo.");
        }
        for (BULL_Group g : groups) {
            if (g.getIdGroup() == group.getIdGroup()) {
                return OperationResult.fail("El grupo " + group.getIdGroup() + " ya existe en esta modalidad.");
            }
        }
        groups.add(group);
        return OperationResult.ok("Grupo agregado a la modalidad.");
    }

    public boolean tieneGruposDisponibles() {
        for (BULL_Group g : groups) {
            if (g.tieneCupoDisponible()) return true;
        }
        return false;
    }

    public List<BULL_Group> getGroups() { return Collections.unmodifiableList(groups); }

    @Override
    public String toString() {
        return "Modality{mode='" + getMode() + "', groups=" + groups.size() + "}"; // ✅ usa el método
    }
}