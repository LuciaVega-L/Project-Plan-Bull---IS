package main.java.entities;


import main.java.usecases.dto.OperationResult;

public class BULL_OnSitePresencial extends BULL_Modality {

    private BULL_Ubication ubication;

    public BULL_OnSitePresencial() {
        super("Presencial");
    }

    public BULL_OnSitePresencial(BULL_Ubication ubication) {
        super("Presencial");
        OperationResult result = validarUbicacion(ubication);
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
        this.ubication = ubication;
    }

    // --- Business Logic ---

    public void assignUbication(BULL_Ubication ubication) {
        OperationResult result = validarUbicacion(ubication);
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
        this.ubication = ubication;
    }

    public boolean tieneUbicacionAsignada() {
        return ubication != null;
    }

    public OperationResult validarListaParaClase() {
        if (ubication == null) {
            return OperationResult.fail("La modalidad presencial no tiene una ubicación (aula) asignada.");
        }
        if (!tieneGruposDisponibles()) {
            return OperationResult.fail("No hay grupos con cupo en esta modalidad presencial.");
        }
        return OperationResult.ok("Modalidad presencial lista. Aula: " + ubication.getClassroomNum());
    }

    private OperationResult validarUbicacion(BULL_Ubication ubication) {
        if (ubication == null) {
            return OperationResult.fail("La ubicación para modalidad presencial no puede ser nula.");
        }
        return OperationResult.ok("Ubicación válida.");
    }

    // --- Getters ---

    public BULL_Ubication getUbication() { return ubication; }

    @Override
    public String toString() {
        return "BULL_OnSitePresencial{ubication=" + ubication + ", groups=" + getGroups().size() + "}";
    }
}
