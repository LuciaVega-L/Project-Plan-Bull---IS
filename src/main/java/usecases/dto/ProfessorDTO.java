package usecases.dto;

public class ProfessorDTO {
    private final String idTeaching;
    private final String name;
    private final String mail;

    public ProfessorDTO(String idTeaching, String name, String mail) {
        this.idTeaching = idTeaching;
        this.name       = name;
        this.mail       = mail;
    }

    public String getIdTeaching() { return idTeaching; }
    public String getName()       { return name; }
    public String getMail()       { return mail; }
}
