package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Map;

class BULL_ScheduleTest {

    @Test
    void constructor_creaHorarioVacio() {
        BULL_Schedule schedule = new BULL_Schedule();

        assertTrue(
                schedule.getHourlay().isEmpty(),
                "El horario debería iniciar vacío"
        );
    }

    @Test
    void addTimeSlot_agregaFranjaCorrectamente() {
        BULL_Schedule schedule = new BULL_Schedule();

        schedule.addTimeSlot(
                "Lunes",
                "08:00-10:00"
        );

        assertEquals(
                1,
                schedule.getHourlay().size()
        );

        assertEquals(
                "08:00-10:00",
                schedule.getHourlay().get("Lunes")
        );
    }

    @Test
    void removeTimeSlot_eliminaFranjaCorrectamente() {
        BULL_Schedule schedule = new BULL_Schedule();

        schedule.addTimeSlot(
                "Lunes",
                "08:00-10:00"
        );

        schedule.removeTimeSlot("Lunes");

        assertTrue(
                schedule.getHourlay().isEmpty()
        );
    }

    @Test
    void tieneHorario_retornaFalse_cuandoNoHayFranjas() {
        BULL_Schedule schedule = new BULL_Schedule();

        assertFalse(
                schedule.tieneHorario()
        );
    }

    @Test
    void tieneHorario_retornaTrue_cuandoExisteAlMenosUnaFranja() {
        BULL_Schedule schedule = new BULL_Schedule();

        schedule.addTimeSlot(
                "Martes",
                "10:00-12:00"
        );

        assertTrue(
                schedule.tieneHorario()
        );
    }

    @Test
    void getHourlay_retornaMapaInmodificable() {
        BULL_Schedule schedule = new BULL_Schedule();

        Map<String, String> hourlay =
                schedule.getHourlay();

        assertThrows(
                UnsupportedOperationException.class,
                () -> hourlay.put(
                        "Viernes",
                        "14:00-16:00"
                )
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Schedule schedule = new BULL_Schedule();

        schedule.addTimeSlot(
                "Lunes",
                "08:00-10:00"
        );

        schedule.addTimeSlot(
                "Martes",
                "10:00-12:00"
        );

        assertEquals(
                "Schedule{franjas=2}",
                schedule.toString()
        );
    }
}