package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class BULL_StudentTest {

    private BULL_Student createStudent() {
        return new BULL_Student(
                "20201234",
                "David",
                "Beltran",
                "david@correo.com",
                5,
                "Ingenieria de Sistemas",
                false
        );
    }

    private BULL_Registration createRegistration() {
        BULL_Group group = new BULL_Group(1);

        return new BULL_Registration(
                createStudent(),
                group
        );
    }

    @Test
    void constructor_creaEstudianteCorrectamente() {
        BULL_Student student = createStudent();

        assertEquals("20201234", student.getUniversityCode());
        assertEquals("David", student.getName());
        assertEquals("Beltran", student.getSurnames());
        assertEquals("david@correo.com", student.getMail());
        assertEquals(5, student.getSemester());
        assertEquals("Ingenieria de Sistemas", student.getProgram());

        assertFalse(student.isSpecialCondition());
        assertNull(student.getPhoneNumber());
        assertTrue(student.getRegistrations().isEmpty());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCodigoEsNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        null,
                        "David",
                        "Beltran",
                        "correo@correo.com",
                        1,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCodigoEsVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        " ",
                        "David",
                        "Beltran",
                        "correo@correo.com",
                        1,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoNombreEsNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        "2020",
                        null,
                        "Beltran",
                        "correo@correo.com",
                        1,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoApellidosSonNulos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        "2020",
                        "David",
                        null,
                        "correo@correo.com",
                        1,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCorreoEsInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        "2020",
                        "David",
                        "Beltran",
                        "correo.com",
                        1,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoSemestreEsMenorAUno() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Student(
                        "2020",
                        "David",
                        "Beltran",
                        "correo@correo.com",
                        0,
                        "Sistemas",
                        false
                )
        );
    }

    @Test
    void setMail_actualizaCorreoCorrectamente() {
        BULL_Student student = createStudent();

        student.setMail("nuevo@correo.com");

        assertEquals(
                "nuevo@correo.com",
                student.getMail()
        );
    }

    @Test
    void setMail_lanzaExcepcion_cuandoCorreoEsInvalido() {
        BULL_Student student = createStudent();

        assertThrows(
                IllegalArgumentException.class,
                () -> student.setMail("correo.com")
        );
    }

    @Test
    void setPhoneNumber_actualizaTelefonoCorrectamente() {
        BULL_Student student = createStudent();

        student.setPhoneNumber("3001234567");

        assertEquals(
                "3001234567",
                student.getPhoneNumber()
        );
    }

    @Test
    void setSpecialCondition_actualizaCondicionEspecial() {
        BULL_Student student = createStudent();

        student.setSpecialCondition(true);

        assertTrue(
                student.isSpecialCondition()
        );
    }

    @Test
    void addRegistration_agregaInscripcionCorrectamente() {
        BULL_Student student = createStudent();

        BULL_Registration registration =
                createRegistration();

        student.addRegistration(registration);

        assertEquals(
                1,
                student.getRegistrations().size()
        );
    }

    @Test
    void getRegistrations_retornaListaInmodificable() {
        BULL_Student student = createStudent();

        List<BULL_Registration> registrations =
                student.getRegistrations();

        assertThrows(
                UnsupportedOperationException.class,
                () -> registrations.add(
                        createRegistration()
                )
        );
    }

    @Test
    void tieneInscripcionActiva_retornaFalse_cuandoNoTieneInscripciones() {
        BULL_Student student = createStudent();

        assertFalse(
                student.tieneInscripcionActiva()
        );
    }

    @Test
    void tieneInscripcionActiva_retornaTrue_cuandoExisteUnaActiva() {
        BULL_Student student = createStudent();

        BULL_Registration registration =
                createRegistration();

        student.addRegistration(registration);

        assertTrue(
                student.tieneInscripcionActiva()
        );
    }

    @Test
    void tieneInscripcionActiva_retornaFalse_cuandoTodasEstanCanceladas() {
        BULL_Student student = createStudent();

        BULL_Registration registration =
                createRegistration();

        registration.cancelar();

        student.addRegistration(registration);

        assertFalse(
                student.tieneInscripcionActiva()
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Student student = createStudent();

        assertEquals(
                "Student{code='20201234', name='David Beltran'}",
                student.toString()
        );
    }
}