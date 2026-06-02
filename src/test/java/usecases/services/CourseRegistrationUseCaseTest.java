package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryGroupRepository;
import infrastructure.repositories.BULL_InMemoryHomologationRepository;
import infrastructure.repositories.BULL_InMemoryModalityRepository;
import infrastructure.repositories.BULL_InMemoryRegistrationRepository;
import infrastructure.repositories.BULL_InMemoryStudentRepository;
import org.junit.jupiter.api.Test;


import usecases.dto.CourseOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import static org.junit.jupiter.api.Assertions.*;

class CourseRegistrationUseCaseTest {

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

    private CourseOptionDTO createOption() {
        return new CourseOptionDTO(
                1,
                3,
                "Presencial",
                1,
                20,
                "{Lunes=8-10}",
                true,
                "A",
                "101"
        );
    }

    @Test
    void inscribirModulo_retornaError_cuandoCodigoEsVacio() {

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryStudentRepository(),
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse("", createOption());

        assertFalse(resultado.isSuccess());
    }

    @Test
    void inscribirModulo_retornaError_cuandoOpcionEsNula() {

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryStudentRepository(),
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse("20201234", null);

        assertFalse(resultado.isSuccess());
    }

    @Test
    void inscribirModulo_retornaError_cuandoEstudianteNoExiste() {

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        new BULL_InMemoryGroupRepository(),
                        new BULL_InMemoryStudentRepository(),
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse(
                        "99999",
                        createOption()
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void inscribirModulo_retornaError_cuandoGrupoNoExiste() {

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        studentRepo.save(createStudent());

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        new BULL_InMemoryGroupRepository(),
                        studentRepo,
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse(
                        "20201234",
                        createOption()
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void inscribirModulo_retornaError_cuandoGrupoPresencialNoTieneUbicacion() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(20));

        groupRepo.save(group);
        studentRepo.save(createStudent());

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        groupRepo,
                        studentRepo,
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse(
                        "20201234",
                        createOption()
                );

        assertFalse(resultado.isSuccess());
    }

    @Test
    void inscribirModulo_realizaInscripcionCorrectamente() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_RegistrationRepository registrationRepo =
                new BULL_InMemoryRegistrationRepository();

        BULL_Group group = new BULL_Group(1);

        group.setMaxCapacity(
                new BULL_MaxCapacity(20)
        );

        group.setUbication(
                new BULL_Ubication("A", "101"),
                new BULL_OnSitePresencial()
        );

        BULL_Student student =
                createStudent();

        studentRepo.save(student);
        groupRepo.save(group);

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        groupRepo,
                        studentRepo,
                        registrationRepo,
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse(
                        "20201234",
                        createOption()
                );

        assertTrue(resultado.isSuccess());

        assertEquals(
                1,
                student.getRegistrations().size()
        );

        assertEquals(
                1,
                group.getRegistrations().size()
        );
    }

    @Test
    void inscribirModulo_retornaError_cuandoEstudianteYaTieneInscripcionActiva() {

        BULL_GroupRepository groupRepo =
                new BULL_InMemoryGroupRepository();

        BULL_StudentRepository studentRepo =
                new BULL_InMemoryStudentRepository();

        BULL_Student student =
                createStudent();

        BULL_Group group = new BULL_Group(1);
        group.setMaxCapacity(new BULL_MaxCapacity(20));

        BULL_Registration registration =
                new BULL_Registration(student, group);

        student.addRegistration(registration);

        studentRepo.save(student);

        CourseRegistrationUseCase useCase =
                new CourseRegistrationUseCase(
                        groupRepo,
                        studentRepo,
                        new BULL_InMemoryRegistrationRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryHomologationRepository()
                );

        OperationResult resultado =
                useCase.inscribirCourse(
                        "20201234",
                        createOption()
                );

        assertFalse(resultado.isSuccess());
    }
}