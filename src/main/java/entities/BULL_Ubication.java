package main.java.entities;


import main.java.usecases.dto.OperationResult;

public class BULL_Ubication {

    private String ubication;
    private String classroomNum;

    public BULL_Ubication(String ubication, String classroomNum) {
        OperationResult validation = validateCreation(ubication, classroomNum);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.ubication = ubication;
        this.classroomNum = classroomNum;
    }

    // --- Business Logic ---

    public OperationResult validarDisponibilidadAula() {
        if (ubication == null || ubication.trim().isEmpty()) {
            return OperationResult.fail("La ubicación del aula no está definida.");
        }
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            return OperationResult.fail("El número de aula no está definido.");
        }
        return OperationResult.ok("Aula " + classroomNum + " en " + ubication + " disponible.");
    }

    public boolean esAulaValida() {
        return validarDisponibilidadAula().isSuccess();
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(String ubication, String classroomNum) {
        if (ubication == null || ubication.trim().isEmpty()) {
            return OperationResult.fail("La ubicación (edificio/campus) no puede estar vacía.");
        }
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            return OperationResult.fail("El número de aula no puede estar vacío.");
        }
        return OperationResult.ok("Ubicación válida.");
    }

    // --- Getters ---

    public String getUbication() { return ubication; }
    public String getClassroomNum() { return classroomNum; }

    // --- Setters (only mutable fields) ---

    public void setUbication(String ubication) {
        if (ubication == null || ubication.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        }
        this.ubication = ubication;
    }

    public void setClassroomNum(String classroomNum) {
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de aula no puede estar vacío.");
        }
        this.classroomNum = classroomNum;
    }

    @Override
    public String toString() {
        return "BULL_Ubication{ubication='" + ubication + "', classroomNum='" + classroomNum + "'}";
    }
}
