package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BULL_GradeTest {

    @Test
    void constructor_creaNotaCorrectamente_cuandoDatosSonValidos() {
        BULL_Grade grade = new BULL_Grade(GradeType.PRIMER_CORTE, 4.5);

        assertEquals(4.5, grade.getNote());
        assertEquals(GradeType.PRIMER_CORTE, grade.getType());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoNotaEsMenorACero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Grade(GradeType.PRIMER_CORTE, -1)
        );

        assertEquals(
                "La nota debe estar entre 0 e 5.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoNotaEsMayorACinco() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Grade(GradeType.PRIMER_CORTE, 5.1)
        );

        assertEquals(
                "La nota debe estar entre 0 e 5.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoTipoEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Grade(null, 4.0)
        );

        assertEquals(
                "Debe definir un corte para la nota.",
                exception.getMessage()
        );
    }

    @Test
    void getPorcentaje_retornaPorcentajeDelTipo() {
        BULL_Grade grade = new BULL_Grade(GradeType.PRIMER_CORTE, 4.0);

        assertEquals(
                GradeType.PRIMER_CORTE.getPorcentaje(),
                grade.getPorcentaje()
        );
    }

    @Test
    void getWeightedValue_calculaValorPonderadoCorrectamente() {
        BULL_Grade grade = new BULL_Grade(GradeType.PRIMER_CORTE, 4.0);

        double esperado = 4.0 * (GradeType.PRIMER_CORTE.getPorcentaje() / 100.0);

        assertEquals(
                esperado,
                grade.getWeightedValue(),
                0.0001,
                "El valor ponderado debería calcularse correctamente"
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Grade grade = new BULL_Grade(GradeType.PRIMER_CORTE, 4.5);

        String esperado = "PRIMER_CORTE - 4.5(30.0%)";

        assertEquals(
                esperado,
                grade.toString(),
                "El formato del toString debería ser correcto"
        );
    }
}