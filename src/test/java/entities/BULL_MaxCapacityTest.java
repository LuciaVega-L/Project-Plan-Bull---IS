package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BULL_MaxCapacityTest {

    @Test
    void constructor_creaObjetoCorrectamente() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(30);

        assertEquals(30, capacity.getMaxCapacity());
        assertEquals(0, capacity.getCurrentEnrollment());
        assertEquals(30, capacity.getCuposRestantes());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCapacidadEsCero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_MaxCapacity(0)
        );

        assertEquals(
                "El cupo máximo debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCapacidadEsNegativa() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_MaxCapacity(-1)
        );

        assertEquals(
                "El cupo máximo debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void incrementEnrollment_aumentaInscritos() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(10);

        capacity.incrementEnrollment();

        assertEquals(1, capacity.getCurrentEnrollment());
        assertEquals(9, capacity.getCuposRestantes());
    }

    @Test
    void decrementEnrollment_disminuyeInscritos() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(10);

        capacity.incrementEnrollment();
        capacity.incrementEnrollment();

        capacity.decrementEnrollment();

        assertEquals(1, capacity.getCurrentEnrollment());
        assertEquals(9, capacity.getCuposRestantes());
    }

    @Test
    void tieneCupoDisponible_retornaTrue_cuandoHayCupos() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(2);

        capacity.incrementEnrollment();

        assertTrue(capacity.tieneCupoDisponible());
    }

    @Test
    void tieneCupoDisponible_retornaFalse_cuandoNoHayCupos() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(2);

        capacity.incrementEnrollment();
        capacity.incrementEnrollment();

        assertFalse(capacity.tieneCupoDisponible());
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_MaxCapacity capacity = new BULL_MaxCapacity(20);

        capacity.incrementEnrollment();

        assertEquals(
                "MaxCapacity{max=20, inscritos=1}",
                capacity.toString()
        );
    }
}