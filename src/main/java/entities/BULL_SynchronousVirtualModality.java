package main.java.entities;

/**
 * Domain entity representing a Synchronous Virtual (Virtual Sincrónica) Modality.
 * Subclass of BULL_Modality.
 *
 * Business rules:
 * - No physical location required.
 * - All groups under this modality must have a defined schedule (real-time sessions).
 */
public class BULL_SynchronousVirtualModality extends BULL_Modality {

    public BULL_SynchronousVirtualModality() {
        super("Virtual Sincrónica");
    }

    // --- Business Logic ---

    public OperationResult validarGruposTienenHorario() {
        if (getGroups().isEmpty()) {
            return OperationResult.fail("La modalidad virtual sincrónica no tiene grupos asignados.");
        }
        for (int i = 0; i < getGroups().size(); i++) {
            BULL_Group group = getGroups().get(i);
            if (group.getSchedule() == null || group.getSchedule().getHourlay() == null
                    || group.getSchedule().getHourlay().isEmpty()) {
                return OperationResult.fail("El grupo " + group.getIdGroup() +
                        " no tiene horario definido. La modalidad sincrónica requiere horario.");
            }
        }
        return OperationResult.ok("Todos los grupos de la modalidad sincrónica tienen horario definido.");
    }

    @Override
    public String toString() {
        return "BULL_SynchronousVirtualModality{groups=" + getGroups().size() + "}";
    }
}
