package entities;

import usecases.dto.OperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BULL_Registration {

    public static final String STATE_ACTIVA     = "ACTIVA";
    public static final String STATE_CANCELADA  = "CANCELADA";
    public static final String STATE_FINALIZADA = "FINALIZADA";

    private String idRegistration;
    private String state;
    private BULL_Student student;
    private BULL_Group group;
    private final List<BULL_Grade> grades = new ArrayList<>();

    public BULL_Registration(BULL_Student student, BULL_Group group) {
        if (student == null) throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        if (group == null)   throw new IllegalArgumentException("El grupo no puede ser nulo.");
        this.idRegistration = generarId(student.getUniversityCode(), group.getIdGroup());
        this.student = student;
        this.group   = group;
        this.state   = STATE_ACTIVA;
    }
    public String getIdRegistration() { return idRegistration; }
    public String getState() { return state; }
    public BULL_Student getStudent() { return student; }
    public BULL_Group getGroup() { return group; }
    public List<BULL_Grade> getGrades() { return Collections.unmodifiableList(grades); }

    private String generarId(String universityCode, int idGrupo) {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "REG-" + universityCode + "-G" + idGrupo + "-" + uuid;
    }

    public OperationResult addGrade(BULL_Grade grade) {
        if(grade == null) {
            throw new IllegalArgumentException("El grade no puede ser nulo.");
        }
        for (BULL_Grade g : grades) {
            if (g.getType() == grade.getType()) {
                throw new IllegalArgumentException("Ya existe una nota para " + grade.getType().name() + ".");
            }
        }
        grades.add(grade);
        return null;
    }

    public void cancelar() {
        this.state = STATE_CANCELADA;
    }

    public void finalizar() {
        this.state = STATE_FINALIZADA;
    }

    public boolean estaActiva() {
        return STATE_ACTIVA.equals(state);
    }

    public boolean tieneTodasLasNotas() {
        return grades.size() == GradeType.values().length;
    }

    public double NoteFinal(){
        double total = 0.0;
        for(BULL_Grade g : grades){
            total += g.getWeightedValue();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Registration{id='" + idRegistration +
                "', state='" + state +
                "', student=" + student.getUniversityCode() + "}";
    }
}
