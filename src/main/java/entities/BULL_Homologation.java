package entities;

public class BULL_Homologation {

    private final BULL_Student    student;
    private final BULL_Certificate certificate;
    private HomologationStatus    status;
    private int                   approvedModule;
    private String                message;

    public BULL_Homologation(BULL_Student student, BULL_Certificate certificate) {

        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        if (certificate == null) {
            throw new IllegalArgumentException("El certificado no puede ser nulo.");
        }

        this.student        = student;
        this.certificate    = certificate;
        this.status         = HomologationStatus.PENDIENTE;
        this.approvedModule = 0;
        this.message        = null;
    }

    public void approve(int moduleNumber, String observation) {

        if (this.status != HomologationStatus.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden aprobar solicitudes en estado PENDIENTE. Estado actual: " + this.status + "."
            );
        }
        if (moduleNumber <= 0) {
            throw new IllegalArgumentException(
                    "El numero de modulo homologado debe ser mayor a 0."
            );
        }

        this.status         = HomologationStatus.APROVADO;
        this.approvedModule = moduleNumber;

        if (observation == null || observation.trim().isEmpty()) {
            this.message = "Homologacion aprobada hasta el modulo " + moduleNumber + ".";
        } else {
            this.message = observation.trim();
        }
    }

    public void reject(String reason) {

        if (this.status != HomologationStatus.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden rechazar solicitudes en estado PENDIENTE. Estado actual: " + this.status + "."
            );
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la razon del rechazo.");
        }

        this.status  = HomologationStatus.RECHAZADO;
        this.message = reason.trim();
    }

    public boolean isPending() {
        return this.status == HomologationStatus.PENDIENTE;
    }

    public boolean isApproved() {
        return this.status == HomologationStatus.APROVADO;
    }

    public BULL_Student     getStudent()        { return student; }
    public BULL_Certificate getCertificate()    { return certificate; }
    public HomologationStatus getStatus()       { return status; }
    public int              getApprovedModule() { return approvedModule; }
    public String           getMessage()        { return message; }

    @Override
    public String toString() {
        return "Homologation{student='" + student.getUniversityCode() +
                "', status=" + status +
                ", approvedModule=" + approvedModule + "}";
    }

    public Object getIdHomologation() {
        return null;
    }
}