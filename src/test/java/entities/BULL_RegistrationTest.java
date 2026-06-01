package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class BULL_RegistrationTest {

    private BULL_Student createStudent() {
        return new BULL_Student(
                "20201234",
                "David",
                "Beltran",
                "david@correo.com",
                5,
                "Sistemas",
                false
        );
    }

    private BULL_Group createGroup() {
        return new BULL_Group(1);
    }

    @Test
    void constructor_creaInscripcionCorrectamente() {
        BULL_Student student = createStudent();
        BULL_Group group = createGroup();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        assertNotNull(registration.getIdRegistration());
        assertEquals(BULL_Registration.STATE_ACTIVA,
                registration.getState());
        assertEquals(student, registration.getStudent());
        assertEquals(group, registration.getGroup());
        assertTrue(registration.getGrades().isEmpty());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoStudentEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Registration(
                        null,
                        createGroup()
                )
        );

        assertEquals(
                "El estudiante no puede ser nulo.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoGroupEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Registration(
                        createStudent(),
                        null
                )
        );

        assertEquals(
                "El grupo no puede ser nulo.",
                exception.getMessage()
        );
    }

    @Test
    void addGrade_agregaNotaCorrectamente() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        BULL_Grade grade =
                new BULL_Grade(
                        GradeType.PRIMER_CORTE,
                        4.5
                );

        registration.addGrade(grade);

        assertEquals(
                1,
                registration.getGrades().size()
        );

        assertTrue(
                registration.getGrades().contains(grade)
        );
    }

    @Test
    void addGrade_lanzaExcepcion_cuandoNotaEsNula() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registration.addGrade(null)
        );

        assertEquals(
                "El grade no puede ser nulo.",
                exception.getMessage()
        );
    }

    @Test
    void addGrade_lanzaExcepcion_cuandoYaExisteEseTipoDeNota() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.PRIMER_CORTE,
                        4.0
                )
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registration.addGrade(
                        new BULL_Grade(
                                GradeType.PRIMER_CORTE,
                                3.0
                        )
                )
        );

        assertEquals(
                "Ya existe una nota para PRIMER_CORTE.",
                exception.getMessage()
        );
    }

    @Test
    void getGrades_retornaListaInmodificable() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        List<BULL_Grade> grades =
                registration.getGrades();

        assertThrows(
                UnsupportedOperationException.class,
                () -> grades.add(
                        new BULL_Grade(
                                GradeType.PRIMER_CORTE,
                                4.0
                        )
                )
        );
    }

    @Test
    void cancelar_cambiaEstadoACancelada() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.cancelar();

        assertEquals(
                BULL_Registration.STATE_CANCELADA,
                registration.getState()
        );
    }

    @Test
    void finalizar_cambiaEstadoAFinalizada() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.finalizar();

        assertEquals(
                BULL_Registration.STATE_FINALIZADA,
                registration.getState()
        );
    }

    @Test
    void estaActiva_retornaTrue_cuandoEstadoEsActivo() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        assertTrue(
                registration.estaActiva()
        );
    }

    @Test
    void estaActiva_retornaFalse_cuandoEstadoNoEsActivo() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.cancelar();

        assertFalse(
                registration.estaActiva()
        );
    }

    @Test
    void tieneTodasLasNotas_retornaFalse_cuandoFaltanNotas() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.PRIMER_CORTE,
                        4.0
                )
        );

        assertFalse(
                registration.tieneTodasLasNotas()
        );
    }

    @Test
    void tieneTodasLasNotas_retornaTrue_cuandoTieneTodosLosCortes() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.PRIMER_CORTE,
                        4.0
                )
        );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.SEGUNDO_CORTE,
                        3.5
                )
        );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.TERCER_CORTE,
                        4.8
                )
        );

        assertTrue(
                registration.tieneTodasLasNotas()
        );
    }

    @Test
    void noteFinal_calculaCorrectamenteLaNotaFinal() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.PRIMER_CORTE,
                        4.0
                )
        );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.SEGUNDO_CORTE,
                        3.0
                )
        );

        registration.addGrade(
                new BULL_Grade(
                        GradeType.TERCER_CORTE,
                        5.0
                )
        );

        double esperado =
                (4.0 * 0.30) +
                        (3.0 * 0.30) +
                        (5.0 * 0.40);

        assertEquals(
                esperado,
                registration.NoteFinal(),
                0.0001
        );
    }

    @Test
    void toString_contieneInformacionEsperada() {
        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        createGroup()
                );

        String result = registration.toString();

        assertTrue(result.contains("20201234"));
        assertTrue(result.contains("ACTIVA"));
        assertTrue(result.contains("REG-"));
    }
}