package usecases.dto;

public class StudentDTO {
    private final String universityCode;
    private final String fullName;
    private final int    semester;
    private final boolean specialCondition;

    public StudentDTO(String universityCode, String fullName, int semester, boolean specialCondition) {
        this.universityCode   = universityCode;
        this.fullName         = fullName;
        this.semester         = semester;
        this.specialCondition = specialCondition;
    }

    public String  getUniversityCode()   { return universityCode; }
    public String  getFullName()         { return fullName; }
    public int     getSemester()         { return semester; }
    public boolean isSpecialCondition()  { return specialCondition; }
}
