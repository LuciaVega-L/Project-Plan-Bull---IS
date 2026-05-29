package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BULL_Modality {

    private String mode;
    private List<BULL_Group> groups;

    public BULL_Modality(String mode) {
        if (mode == null || mode.trim().isEmpty()) {
            throw new IllegalArgumentException("El modo de la modalidad no puede estar vacío.");
        }
        this.mode = mode;
        this.groups = new ArrayList<>();
    }

    public void addGroup(BULL_Group group) {
        groups.add(group);
    }

    public boolean tieneGruposDisponibles() {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).tieneCupoDisponible()) {
                return true;
            }
        }
        return false;
    }

    public String getMode() { return mode; }
    public List<BULL_Group> getGroups() { return Collections.unmodifiableList(groups); }

    @Override
    public String toString() {
        return "Modality{mode='" + mode + "', groups=" + groups.size() + "}";
    }
}
