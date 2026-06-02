package usecases.dto;

public class RegistrationDTO {
    private final String idRegistration;
    private final String universityCode;
    private final String studentFullName;
    private final int idGroup;
    private final int courseNumber;
    private final String state;

    public RegistrationDTO(String idRegistration, String universityCode,
                           String studentFullName,
                           int idGroup, int courseNumber, String state) {
        this.idRegistration = idRegistration;
        this.universityCode = universityCode;
        this.studentFullName = studentFullName;
        this.idGroup = idGroup;
        this.courseNumber = courseNumber;
        this.state = state;
    }

    public String getIdRegistration() { return idRegistration; }
    public String getUniversityCode() { return universityCode; }
    public String getStudentFullName() { return studentFullName; }
    public int getIdGroup() { return idGroup; }
    public int getCourseNumber() { return courseNumber; }
    public String getState() { return state; }
}