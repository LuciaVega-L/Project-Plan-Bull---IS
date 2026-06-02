package usecases.dto;

public class RegistrationDTO {
    private final String idRegistration;
    private final String studentFullName;
    private final int    idGroup;
    private final int    courseNumber;      // ← nuevo
    private final String state;

    public RegistrationDTO(String idRegistration, String studentFullName,
                           int idGroup, int courseNumber, String state) {
        this.idRegistration  = idRegistration;
        this.studentFullName = studentFullName;
        this.idGroup         = idGroup;
        this.courseNumber    = courseNumber; // ← nuevo
        this.state           = state;
    }

    public String getIdRegistration()  { return idRegistration; }
    public String getStudentFullName() { return studentFullName; }
    public int    getIdGroup()         { return idGroup; }
    public int    getCourseNumber()    { return courseNumber; } // ← nuevo
    public String getState()           { return state; }
}