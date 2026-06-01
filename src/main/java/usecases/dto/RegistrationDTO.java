package usecases.dto;

public class RegistrationDTO {
    private final String idRegistration;
    private final String studentFullName;
    private final int    idGroup;
    private final String state;

    public RegistrationDTO(String idRegistration, String studentFullName, int idGroup, String state) {
        this.idRegistration  = idRegistration;
        this.studentFullName = studentFullName;
        this.idGroup         = idGroup;
        this.state           = state;
    }

    public String getIdRegistration()  { return idRegistration; }
    public String getStudentFullName() { return studentFullName; }
    public int    getIdGroup()         { return idGroup; }
    public String getState()           { return state; }
}
