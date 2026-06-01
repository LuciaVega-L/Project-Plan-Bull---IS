package usecases.dto;

public class HomologationDTO {
    private final String universityCode;
    private final String studentFullName;
    private final String fileName;
    private final String fileType;
    private final String status;

    public HomologationDTO(String universityCode, String studentFullName,
                           String fileName, String fileType, String status) {
        this.universityCode  = universityCode;
        this.studentFullName = studentFullName;
        this.fileName        = fileName;
        this.fileType        = fileType;
        this.status          = status;
    }

    public String getUniversityCode()  { return universityCode; }
    public String getStudentFullName() { return studentFullName; }
    public String getFileName()        { return fileName; }
    public String getFileType()        { return fileType; }
    public String getStatus()          { return status; }
}
