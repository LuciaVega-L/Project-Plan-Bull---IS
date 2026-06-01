package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryGroupRepository;
import infrastructure.repositories.BULL_InMemoryRegistrationRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;

import static org.junit.jupiter.api.Assertions.*;

class LoadNotesUsecaseTest {

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

    @Test
    void execute_retornaError_cuandoIdRegistroEsVacio() {

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "",
                        GradeType.PRIMER_CORTE,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoGradeTypeEsNulo() {

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "REG1",
                        null,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoNotaEsInvalida() {

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "REG1",
                        GradeType.PRIMER_CORTE,
                        6.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoGrupoNoExiste() {

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "REG1",
                        GradeType.PRIMER_CORTE,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoGrupoNoTieneInscritos() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_Group group = new BULL_Group(1);

        groupRepo.save(group);

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        groupRepo,
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "REG1",
                        GradeType.PRIMER_CORTE,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoInscripcionNoExiste() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_Group group = new BULL_Group(1);

        BULL_Student student = createStudent();

        BULL_Registration registration =
                new BULL_Registration(student, group);

        group.setMaxCapacity(new BULL_MaxCapacity(20));
        group.addRegistration(registration);

        groupRepo.save(group);

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        groupRepo,
                        new BULL_InMemoryRegistrationRepository()
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        "REG-INEXISTENTE",
                        GradeType.PRIMER_CORTE,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void execute_retornaError_cuandoRegistroNoPerteneceAlGrupo() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_RegistrationRepository registrationRepo =
                new BULL_InMemoryRegistrationRepository();

        BULL_Group group1 = new BULL_Group(1);
        BULL_Group group2 = new BULL_Group(2);

        group1.setMaxCapacity(new BULL_MaxCapacity(20));

        BULL_Registration registration =
                new BULL_Registration(
                        createStudent(),
                        group2
                );

        group1.addRegistration(registration);

        groupRepo.save(group1);
        registrationRepo.save(registration);

        LoadNotesUsecase useCase =
                new LoadNotesUsecase(
                        groupRepo,
                        registrationRepo
                );

        OperationResult resultado =
                useCase.execute(
                        1,
                        registration.getIdRegistration(),
                        GradeType.PRIMER_CORTE,
                        4.0
                );

        assertFalse(resultado.isSuccess());
    }
}