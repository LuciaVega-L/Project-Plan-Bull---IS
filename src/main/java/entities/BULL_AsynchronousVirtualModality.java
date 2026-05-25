package main.java.entities;

public class BULL_AsynchronousVirtualModality extends BULL_Modality {

    public BULL_AsynchronousVirtualModality() {
        super("Plataforma - Estudiante Autónomo");
    }

    // --- Business Logic ---

    public OperationResult validarAccesoEstudianteAutonomo() {
        if (getGroups().isEmpty()) {
            return OperationResult.fail("La modalidad de plataforma no tiene grupos disponibles.");
        }
        for (int i = 0; i < getGroups().size(); i++) {
            if (getGroups().get(i).tieneCupoDisponible()) {
                return OperationResult.ok("Hay cupo disponible en la modalidad de plataforma autónoma.");
            }
        }
        return OperationResult.fail("Todos los grupos de plataforma autónoma están llenos.");
    }

    @Override
    public String toString() {
        return "BULL_AsynchronousVirtualModality{groups=" + getGroups().size() + "}";
    }
}
