package entities;

import java.util.List;

public class BULL_Grade {

    private double note;
    private final GradeType type;

    public BULL_Grade(GradeType type,double note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 0 e 5.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Debe definir un corte para la nota.");
        }
        this.note = note;
        this.type = type;
    }

    public double getNote() {
        return note;
    }
    public GradeType getType() {
        return type;
    }
    public double getPorcentaje() {
        return type.getPorcentaje();
    }

    public double getWeightedValue() {
        double weightednote = note * (type.getPorcentaje()/100);
        return weightednote;
    }

    @Override
    public String toString() {
        return type.name() + " - " + note + "(" + type.getPorcentaje() + "%)";
    }
}