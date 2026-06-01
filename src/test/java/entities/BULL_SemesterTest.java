package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

class BULL_SemesterTest {

    @Test
    void constructor_creaSemestreCorrectamente() {
        Date inicio = new Date(1700000000000L);
        Date fin = new Date(1800000000000L);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        assertEquals(2025, semester.getYear());
        assertEquals(1, semester.getPeriod());
        assertEquals(inicio, semester.getStartDate());
        assertEquals(fin, semester.getEndDate());
        assertTrue(semester.getModalities().isEmpty());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoYearEsMenorA2000() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        1999,
                        1,
                        new Date(),
                        new Date(System.currentTimeMillis() + 1000)
                )
        );

        assertEquals(
                "El año debe ser mayor o igual a 2000.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoPeriodoEsMenorA1() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        2025,
                        0,
                        new Date(),
                        new Date(System.currentTimeMillis() + 1000)
                )
        );

        assertEquals(
                "El periodo debe ser 1 o 2.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoPeriodoEsMayorA2() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        2025,
                        3,
                        new Date(),
                        new Date(System.currentTimeMillis() + 1000)
                )
        );

        assertEquals(
                "El periodo debe ser 1 o 2.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoFechaInicioEsNula() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        2025,
                        1,
                        null,
                        new Date()
                )
        );

        assertEquals(
                "Las fechas no pueden ser nulas.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoFechaFinEsNula() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        2025,
                        1,
                        new Date(),
                        null
                )
        );

        assertEquals(
                "Las fechas no pueden ser nulas.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoInicioNoEsAnteriorAFin() {
        Date fecha = new Date();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Semester(
                        2025,
                        1,
                        fecha,
                        fecha
                )
        );

        assertEquals(
                "La fecha de inicio debe ser anterior a la fecha de fin.",
                exception.getMessage()
        );
    }

    @Test
    void addModality_agregaModalidadCorrectamente() {
        Date inicio = new Date(1700000000000L);
        Date fin = new Date(1800000000000L);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        BULL_Modality modality =
                new BULL_OnSitePresencial();

        semester.addModality(modality);

        assertEquals(
                1,
                semester.getModalities().size()
        );

        assertTrue(
                semester.getModalities().contains(modality)
        );
    }

    @Test
    void getModalities_retornaListaInmodificable() {
        Date inicio = new Date(1700000000000L);
        Date fin = new Date(1800000000000L);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        List<BULL_Modality> modalities =
                semester.getModalities();

        assertThrows(
                UnsupportedOperationException.class,
                () -> modalities.add(
                        new BULL_OnSitePresencial()
                )
        );
    }

    @Test
    void estaVigente_retornaTrue_cuandoLaFechaActualEstaDentroDelPeriodo() {
        Date inicio =
                new Date(System.currentTimeMillis() - 10000);

        Date fin =
                new Date(System.currentTimeMillis() + 10000);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        assertTrue(
                semester.estaVigente()
        );
    }

    @Test
    void estaVigente_retornaFalse_cuandoElSemestreYaFinalizo() {
        Date inicio =
                new Date(System.currentTimeMillis() - 20000);

        Date fin =
                new Date(System.currentTimeMillis() - 10000);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        assertFalse(
                semester.estaVigente()
        );
    }

    @Test
    void estaVigente_retornaFalse_cuandoElSemestreAunNoInicia() {
        Date inicio =
                new Date(System.currentTimeMillis() + 10000);

        Date fin =
                new Date(System.currentTimeMillis() + 20000);

        BULL_Semester semester =
                new BULL_Semester(2025, 1, inicio, fin);

        assertFalse(
                semester.estaVigente()
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Semester semester =
                new BULL_Semester(
                        2025,
                        2,
                        new Date(1700000000000L),
                        new Date(1800000000000L)
                );

        assertEquals(
                "Semester{year=2025, period=2}",
                semester.toString()
        );
    }
}