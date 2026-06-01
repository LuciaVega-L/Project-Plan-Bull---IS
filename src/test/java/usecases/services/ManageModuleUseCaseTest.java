package usecases.services;

import entities.BULL_Course;
import infrastructure.repositories.BULL_InMemoryCourseRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CourseRepository;

import static org.junit.jupiter.api.Assertions.*;

class ManageModuleUseCaseTest {

    @Test
    void crearModulo_creaModuloCorrectamente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.crearModulo(1, 1);

        assertTrue(result.isSuccess());

        assertTrue(
                repo.findByIdModule(1).isPresent()
        );
    }

    @Test
    void crearModulo_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.crearModulo(0, 1);

        assertFalse(result.isSuccess());
    }

    @Test
    void crearModulo_falla_siNumeroCursoEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.crearModulo(1, 5);

        assertFalse(result.isSuccess());
    }

    @Test
    void crearModulo_falla_siYaExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.crearModulo(1, 2);

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarModulos_falla_siNoHayModulos() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.consultarModulos();

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarModulos_retornaListaDeModulos() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));
        repo.save(new BULL_Course(2, 2));

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.consultarModulos();

        assertTrue(result.isSuccess());

        assertTrue(
                result.getMessage().contains("ID: 1")
        );

        assertTrue(
                result.getMessage().contains("ID: 2")
        );
    }

    @Test
    void consultarModuloPorId_retornaModuloExistente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(10, 3));

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.consultarModuloPorId(10);

        assertTrue(result.isSuccess());
    }

    @Test
    void consultarModuloPorId_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.consultarModuloPorId(0);

        assertFalse(result.isSuccess());
    }

    @Test
    void consultarModuloPorId_falla_siNoExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.consultarModuloPorId(99);

        assertFalse(result.isSuccess());
    }

    @Test
    void eliminarModulo_eliminaCorrectamente() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        repo.save(new BULL_Course(1, 1));

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.eliminarModulo(1);

        assertTrue(result.isSuccess());

        assertFalse(
                repo.findByIdModule(1).isPresent()
        );
    }

    @Test
    void eliminarModulo_falla_siIdEsInvalido() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.eliminarModulo(0);

        assertFalse(result.isSuccess());
    }

    @Test
    void eliminarModulo_falla_siNoExiste() {

        BULL_CourseRepository repo =
                new BULL_InMemoryCourseRepository();

        ManageModuleUseCase useCase =
                new ManageModuleUseCase(repo);

        OperationResult result =
                useCase.eliminarModulo(50);

        assertFalse(result.isSuccess());
    }
}