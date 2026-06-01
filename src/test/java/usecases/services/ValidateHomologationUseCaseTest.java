package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryHomologationRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_HomologationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidateHomologationUseCaseTest {

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

    private BULL_Homologation createPendingHomologation() {

        BULL_Student student = createStudent();

        BULL_Certificate certificate =
                new BULL_Certificate(
                        "certificado.pdf",
                        "PDF",
                        "uploads/test"
                );

        return new BULL_Homologation(student, certificate);
    }

    @Test
    void listPending_retornaSolicitudesPendientes() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        repo.save(createPendingHomologation());

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        List<BULL_Homologation> result =
                useCase.listPending();

        assertEquals(1, result.size());
    }

    @Test
    void approve_falla_siCodigoEsNulo() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.approve(null, 2, "ok");

        assertFalse(result.isSuccess());
    }

    @Test
    void approve_falla_siModuloEsInvalido() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.approve("20201234", 0, "ok");

        assertFalse(result.isSuccess());
    }

    @Test
    void approve_falla_siNoExisteSolicitud() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.approve("20201234", 2, "ok");

        assertFalse(result.isSuccess());
    }

    @Test
    void approve_apruebaCorrectamente() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        BULL_Homologation homologation =
                createPendingHomologation();

        repo.save(homologation);

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.approve(
                        "20201234",
                        3,
                        "Aprobada"
                );

        assertTrue(result.isSuccess());

        assertEquals(
                HomologationStatus.APROVADO,
                homologation.getStatus()
        );

        assertEquals(
                3,
                homologation.getApprovedModule()
        );
    }

    @Test
    void approve_falla_siYaFueAprobada() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        BULL_Homologation homologation =
                createPendingHomologation();

        homologation.approve(2, "ok");

        repo.save(homologation);

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.approve(
                        "20201234",
                        3,
                        "otra"
                );

        assertFalse(result.isSuccess());
    }

    @Test
    void reject_falla_siCodigoEsNulo() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.reject(null, "motivo");

        assertFalse(result.isSuccess());
    }

    @Test
    void reject_falla_siRazonEsVacia() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.reject("20201234", "");

        assertFalse(result.isSuccess());
    }

    @Test
    void reject_falla_siNoExisteSolicitud() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.reject(
                        "20201234",
                        "No cumple requisitos"
                );

        assertFalse(result.isSuccess());
    }

    @Test
    void reject_rechazaCorrectamente() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        BULL_Homologation homologation =
                createPendingHomologation();

        repo.save(homologation);

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.reject(
                        "20201234",
                        "No cumple requisitos"
                );

        assertTrue(result.isSuccess());

        assertEquals(
                HomologationStatus.RECHAZADO,
                homologation.getStatus()
        );
    }

    @Test
    void reject_falla_siYaFueRechazada() {

        BULL_HomologationRepository repo =
                new BULL_InMemoryHomologationRepository();

        BULL_Homologation homologation =
                createPendingHomologation();

        homologation.reject("No cumple");

        repo.save(homologation);

        ValidateHomologationUseCase useCase =
                new ValidateHomologationUseCase(repo);

        OperationResult result =
                useCase.reject(
                        "20201234",
                        "otro motivo"
                );

        assertFalse(result.isSuccess());
    }
}