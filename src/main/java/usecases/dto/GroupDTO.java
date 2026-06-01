package usecases.dto;

public class GroupDTO {
    private final int    idGroup;
    private final String professorName;
    private final String cupos;
    private final String ubicacion;

    public GroupDTO(int idGroup, String professorName, String cupos, String ubicacion) {
        this.idGroup       = idGroup;
        this.professorName = professorName;
        this.cupos         = cupos;
        this.ubicacion     = ubicacion;
    }

    public int    getIdGroup()       { return idGroup; }
    public String getProfessorName() { return professorName; }
    public String getCupos()         { return cupos; }
    public String getUbicacion()     { return ubicacion; }
}
