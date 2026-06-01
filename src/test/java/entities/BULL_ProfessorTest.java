package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class BULL_ProfessorTest {

    @Test
    void constructor_creaProfesorCorrectamente() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan Perez",
                        "juan@correo.com"
                );

        assertEquals("DOC001", professor.getIdTeaching());
        assertEquals("Juan Perez", professor.getName());
        assertEquals("juan@correo.com", professor.getMail());
        assertNull(professor.getPhoneNumber());
        assertTrue(professor.getGroups().isEmpty());
    }

    @Test
    void constructor_lanzaExcepcion_cuandoIdTeachingEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        null,
                        "Juan",
                        "juan@correo.com"
                )
        );

        assertEquals(
                "El ID docente no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoIdTeachingEsVacio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        "   ",
                        "Juan",
                        "juan@correo.com"
                )
        );

        assertEquals(
                "El ID docente no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoNombreEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        "DOC001",
                        null,
                        "juan@correo.com"
                )
        );

        assertEquals(
                "El nombre del profesor no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoNombreEsVacio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        "DOC001",
                        "   ",
                        "juan@correo.com"
                )
        );

        assertEquals(
                "El nombre del profesor no puede estar vacío.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCorreoEsNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        "DOC001",
                        "Juan",
                        null
                )
        );

        assertEquals(
                "El correo del profesor no tiene un formato válido.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCorreoNoTieneArroba() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "correo.com"
                )
        );

        assertEquals(
                "El correo del profesor no tiene un formato válido.",
                exception.getMessage()
        );
    }

    @Test
    void addGroup_agregaGrupoCorrectamente() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "juan@correo.com"
                );

        BULL_Group group = new BULL_Group(1);

        professor.addGroup(group);

        assertEquals(
                1,
                professor.getGroups().size()
        );

        assertTrue(
                professor.getGroups().contains(group)
        );
    }

    @Test
    void getGroups_retornaListaInmodificable() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "juan@correo.com"
                );

        List<BULL_Group> groups =
                professor.getGroups();

        assertThrows(
                UnsupportedOperationException.class,
                () -> groups.add(new BULL_Group(1))
        );
    }

    @Test
    void setMail_actualizaCorreoCorrectamente() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "juan@correo.com"
                );

        professor.setMail("nuevo@correo.com");

        assertEquals(
                "nuevo@correo.com",
                professor.getMail()
        );
    }

    @Test
    void setMail_lanzaExcepcion_cuandoCorreoEsInvalido() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "juan@correo.com"
                );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> professor.setMail("correo.com")
        );

        assertEquals(
                "El correo no tiene un formato válido.",
                exception.getMessage()
        );
    }

    @Test
    void setPhoneNumber_actualizaTelefonoCorrectamente() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan",
                        "juan@correo.com"
                );

        professor.setPhoneNumber("3001234567");

        assertEquals(
                "3001234567",
                professor.getPhoneNumber()
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Professor professor =
                new BULL_Professor(
                        "DOC001",
                        "Juan Perez",
                        "juan@correo.com"
                );

        assertEquals(
                "Professor{id='DOC001', name='Juan Perez'}",
                professor.toString()
        );
    }
}