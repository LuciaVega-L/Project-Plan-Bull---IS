package main.java.entities;

public class BULL_Registration {

    public static final String STATE_ACTIVA     = "ACTIVA";
    public static final String STATE_CANCELADA  = "CANCELADA";
    public static final String STATE_FINALIZADA = "FINALIZADA";

    private String idRegistration;
    private String state;

    private BULL_Student student;
    private BULL_Semester semester;
    private BULL_Group group;
    private BULL_MaxCapacity maxCapacity;

    public BULL_Registration(String idRegistration, BULL_Student student,
                              BULL_Semester semester, BULL_Group group) {
        OperationResult validation = validateCreation(idRegistration, student, semester, group);
        if (!validation.isSuccess()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        this.idRegistration = idRegistration;
        this.state = STATE_ACTIVA;
        this.student = student;
        this.semester = semester;
        this.group = group;
    }

    // --- Business Logic ---

    public void cancelar() {
        OperationResult result = validarCancelacion();
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        this.state = STATE_CANCELADA;
    }

    public void finalizar() {
        OperationResult result = validarFinalizacion();
        if (!result.isSuccess()) {
            throw new IllegalStateException(result.getMessage());
        }
        this.state = STATE_FINALIZADA;
    }

    public boolean estaActiva() {
        return STATE_ACTIVA.equalsIgnoreCase(state);
    }

    public OperationResult validarCancelacion() {
        if (!estaActiva()) {
            return OperationResult.fail("La inscripción '" + idRegistration + "' no puede cancelarse. " +
                    "Estado actual: " + state + ". Solo se pueden cancelar inscripciones ACTIVAS.");
        }
        return OperationResult.ok("La inscripción puede ser cancelada.");
    }

    public OperationResult validarFinalizacion() {
        if (!estaActiva()) {
            return OperationResult.fail("La inscripción '" + idRegistration + "' no puede finalizarse. " +
                    "Estado actual: " + state + ". Solo se pueden finalizar inscripciones ACTIVAS.");
        }
        return OperationResult.ok("La inscripción puede ser finalizada.");
    }

    public OperationResult validarIntegridad() {
        if (student == null) {
            return OperationResult.fail("La inscripción no tiene estudiante asociado.");
        }
        if (semester == null) {
            return OperationResult.fail("La inscripción no tiene semestre asociado.");
        }
        if (group == null) {
            return OperationResult.fail("La inscripción no tiene grupo asociado.");
        }
        return OperationResult.ok("La inscripción " + idRegistration + " es íntegra.");
    }

    // --- Internal Validation ---

    private OperationResult validateCreation(String idRegistration, BULL_Student student,
                                              BULL_Semester semester, BULL_Group group) {
        if (idRegistration == null || idRegistration.trim().isEmpty()) {
            return OperationResult.fail("El id de inscripción no puede estar vacío.");
        }
        if (student == null) {
            return OperationResult.fail("El estudiante no puede ser nulo.");
        }
        if (semester == null) {
            return OperationResult.fail("El semestre no puede ser nulo.");
        }
        if (group == null) {
            return OperationResult.fail("El grupo no puede ser nulo.");
        }
        return OperationResult.ok("Inscripción válida.");
    }

    // --- Getters ---

    public String getIdRegistration() { return idRegistration; }
    public String getState() { return state; }
    public BULL_Student getStudent() { return student; }
    public BULL_Semester getSemester() { return semester; }
    public BULL_Group getGroup() { return group; }
    public BULL_MaxCapacity getMaxCapacity() { return maxCapacity; }

    // --- Setters ---

    public void setMaxCapacity(BULL_MaxCapacity maxCapacity) { this.maxCapacity = maxCapacity; }

    @Override
    public String toString() {
        return "BULL_Registration{idRegistration='" + idRegistration + "', state='" + state + "', " +
                "student=" + (student != null ? student.getUniversityCode() : "N/A") + "}";
    }
}
