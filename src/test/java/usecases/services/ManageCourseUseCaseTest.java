package usecases.services;

import entities.BULL_Course;
import infrastructure.repositories.BULL_InMemoryCourseRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CourseRepository;

import static org.junit.jupiter.api.Assertions.*;

class ManageCourseUseCaseTest {

    @Test
    void crearCourse_creaCourseCorrectamente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.crearCourse(1, 1);

        assertTrue(result.isSuccess());

        assertTrue(
                repo.findByIdCourse(1).isPresent()
        );
    }

    @Test
    void crearCourse_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.crearCourse(0, 1);

        assertFalse(result.isSuccess());
    }

    @Test
    void crearCourse_falla_siNumeroCursoEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.crearCourse(1, 5);

        assertFalse(result.isSuccess());
    }

    @Test
    void crearCourse_falla_siYaExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.crearCourse(1, 2);

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarCourses_falla_siNoHayCourses() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.consultarCourses();

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarCourses_retornaListaDeCourses() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));
        repo.save(new BULL_Course(2, 2));

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.consultarCourses();

        assertTrue(result.isSuccess());

        assertTrue(
                result.getMessage().contains("ID: 1")
        );

        assertTrue(
                result.getMessage().contains("ID: 2")
        );
    }

    @Test
    void consultarCoursePorId_retornaCourseExistente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(10, 3));

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.consultarCoursePorId(10);

        assertTrue(result.isSuccess());
    }

    @Test
    void consultarCoursePorId_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.consultarCoursePorId(0);

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarCoursePorId_falla_siNoExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.consultarCoursePorId(99);

        assertFalse(result.isSuccess());
    }

    @Test
    void eliminarCourse_eliminaCorrectamente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.eliminarCourse(1);

        assertTrue(result.isSuccess());

        assertFalse(
                repo.findByIdCourse(1).isPresent()
        );
    }

    @Test
    void eliminarCourse_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.eliminarCourse(0);

        assertFalse(result.isSuccess());
    }

    @Test
    void eliminarCourse_falla_siNoExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageCourseUseCase useCase =
                new ManageCourseUseCase(repo);

        OperationResult result =
                useCase.eliminarCourse(50);

        assertFalse(result.isSuccess());
    }
}