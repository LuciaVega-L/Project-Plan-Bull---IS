package entities;

public enum GradeType {
    PRIMER_CORTE(30),
    SEGUNDO_CORTE(30),
    TERCER_CORTE(40);

    private final double porcentaje;
    GradeType(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    public double getPorcentaje() {
        return porcentaje;
    }
}
