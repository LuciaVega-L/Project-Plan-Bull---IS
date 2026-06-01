package usecases.services;

import entities.BULL_Professor;
import infrastructure.repositories.BULL_InMemoryProfessorRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_ProfessorRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadProfessorListUseCaseTest {

    private BULL_Professor createProfessor(String id) {
        return new BULL_Professor(
                id,
                "Profesor " + id,
                id.toLowerCase() + "@correo.com"
        );
    }

    @Test
    void cargarListaProfesores_retornaError_siListaEsNula() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        OperationResult result =
                useCase.cargarListaProfesores(null);

        assertFalse(result.isSuccess());
    }

    @Test
    void cargarListaProfesores_retornaError_siListaEstaVacia() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        OperationResult result =
                useCase.cargarListaProfesores(new ArrayList<>());

        assertFalse(result.isSuccess());
    }

    @Test
    void cargarListaProfesores_guardaProfesoresCorrectamente() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        List<BULL_Professor> profesores = new ArrayList<>();
        profesores.add(createProfessor("P001"));
        profesores.add(createProfessor("P002"));

        OperationResult result =
                useCase.cargarListaProfesores(profesores);

        assertTrue(result.isSuccess());

        assertEquals(
                2,
                repo.findAll().size()
        );
    }

    @Test
    void cargarListaProfesores_omiteDuplicados() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        repo.save(createProfessor("P001"));

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        List<BULL_Professor> profesores = new ArrayList<>();
        profesores.add(createProfessor("P001"));
        profesores.add(createProfessor("P002"));

        OperationResult result =
                useCase.cargarListaProfesores(profesores);

        assertTrue(result.isSuccess());

        assertEquals(
                2,
                repo.findAll().size()
        );

        assertTrue(
                result.getMessage().contains("Duplicados")
        );
    }

    @Test
    void cargarListaProfesores_ignoraProfesoresNulos() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        List<BULL_Professor> profesores = new ArrayList<>();
        profesores.add(null);
        profesores.add(createProfessor("P001"));

        OperationResult result =
                useCase.cargarListaProfesores(profesores);

        assertTrue(result.isSuccess());

        assertEquals(
                1,
                repo.findAll().size()
        );

        assertTrue(
                result.getMessage().contains("Errores")
        );
    }

    @Test
    void cargarListaProfesores_retornaFail_siNingunoSeCarga() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        List<BULL_Professor> profesores = new ArrayList<>();
        profesores.add(null);

        OperationResult result =
                useCase.cargarListaProfesores(profesores);

        assertFalse(result.isSuccess());

        assertEquals(
                0,
                repo.findAll().size()
        );
    }

    @Test
    void cargarListaProfesores_conMezclaDeValidosYDuplicados() {

        BULL_ProfessorRepository repo =
                new BULL_InMemoryProfessorRepository();

        repo.save(createProfessor("P001"));

        LoadProfessorListUseCase useCase =
                new LoadProfessorListUseCase(repo);

        List<BULL_Professor> profesores = new ArrayList<>();
        profesores.add(createProfessor("P001"));
        profesores.add(createProfessor("P002"));
        profesores.add(createProfessor("P003"));

        OperationResult result =
                useCase.cargarListaProfesores(profesores);

        assertTrue(result.isSuccess());

        assertEquals(
                3,
                repo.findAll().size()
        );
    }
}