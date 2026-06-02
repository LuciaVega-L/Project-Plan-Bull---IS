package entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

class BULL_CourseTest {

    @Test
    void constructor_creaCursoCorrectamente_cuandoDatosSonValidos() {
        BULL_Course course = new BULL_Course(1, 2);

        assertEquals(1, course.getIdCourse(),
                "El id del módulo debería ser 1");
        assertEquals(2, course.getCourseNumber(),
                "El número de curso debería ser 2");
        assertTrue(course.getSemesters().isEmpty(),
                "La lista de semestres debería iniciar vacía");
    }

    @Test
    void constructor_lanzaExcepcion_cuandoIdModuleEsCero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Course(0, 1)
        );

        assertEquals(
                "El ID del módulo debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoIdModuleEsNegativo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Course(-1, 1)
        );

        assertEquals(
                "El ID del módulo debe ser mayor a 0.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCourseNumberEsMenorAUno() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Course(1, 0)
        );

        assertEquals(
                "El número de curso debe estar entre 1 y 4.",
                exception.getMessage()
        );
    }

    @Test
    void constructor_lanzaExcepcion_cuandoCourseNumberEsMayorACuatro() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BULL_Course(1, 5)
        );

        assertEquals(
                "El número de curso debe estar entre 1 y 4.",
                exception.getMessage()
        );
    }

    @Test
    void addSemester_agregaSemestreCorrectamente() {
        BULL_Course course = new BULL_Course(1, 2);

        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 10000);

        BULL_Semester semester = new BULL_Semester(
                2025,
                1,
                startDate,
                endDate
        );

        course.addSemester(semester);

        assertEquals(1, course.getSemesters().size(),
                "Debería existir un semestre en la lista");

        assertTrue(course.getSemesters().contains(semester),
                "El semestre agregado debería estar en la lista");
    }

    @Test
    void getSemesters_retornaListaInmodificable() {
        BULL_Course course = new BULL_Course(1, 2);

        List<BULL_Semester> semesters = course.getSemesters();

        assertThrows(
                UnsupportedOperationException.class,
                () -> semesters.add(null),
                "La lista retornada no debería permitir modificaciones"
        );
    }

    @Test
    void toString_retornaRepresentacionCorrecta() {
        BULL_Course course = new BULL_Course(10, 3);

        String result = course.toString();

        assertEquals(
                "Course{idCourse=10, courseNumber=3}",
                result,
                "El toString debería coincidir con el formato esperado"
        );
    }
}