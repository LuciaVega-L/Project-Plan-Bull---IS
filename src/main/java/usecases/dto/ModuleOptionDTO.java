package main.java.usecases.dto;
public class ModuleOptionDTO {

    private final int    idModule;
    private final int    courseNumber;
    private final String modalidad;
    private final int    idGrupo;
    private final int    cuposRestantes;
    private final String horario;
    private final boolean esPresencial;
    private final String  ubicacion;
    private final String  numAula;


    public ModuleOptionDTO(int idModule,
                           int courseNumber,
                           String modalidad,
                           int idGrupo,
                           int cuposRestantes,
                           String horario,
                           boolean esPresencial,
                           String ubicacion,
                           String numAula) {
        this.idModule       = idModule;
        this.courseNumber   = courseNumber;
        this.modalidad      = modalidad;
        this.idGrupo        = idGrupo;
        this.cuposRestantes = cuposRestantes;
        this.horario        = horario;
        this.esPresencial   = esPresencial;
        this.ubicacion      = ubicacion;
        this.numAula        = numAula;
    }
    
    public int     getIdModule()       { return idModule; }
    public int     getCourseNumber()   { return courseNumber; }
    public String  getModalidad()      { return modalidad; }
    public int     getIdGrupo()        { return idGrupo; }
    public int     getCuposRestantes() { return cuposRestantes; }
    public String  getHorario()        { return horario; }
    public boolean isEsPresencial()    { return esPresencial; }
    public String  getUbicacion()      { return ubicacion; }
    public String  getNumAula()        { return numAula; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ModuleOptionDTO{");
        sb.append("modulo=").append(idModule);
        sb.append(", curso=").append(courseNumber);
        sb.append(", modalidad='").append(modalidad).append("'");
        sb.append(", grupo=").append(idGrupo);
        sb.append(", cupos=").append(cuposRestantes);
        sb.append(", horario=").append(horario);
        if (esPresencial) {
            sb.append(", ubicacion='").append(ubicacion).append("'");
            sb.append(", aula='").append(numAula).append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}