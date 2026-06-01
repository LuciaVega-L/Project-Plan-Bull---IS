package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryCourseRepository;
import infrastructure.repositories.BULL_InMemoryGroupRepository;
import infrastructure.repositories.BULL_InMemoryModalityRepository;
import infrastructure.repositories.BULL_InMemoryStudentRepository;
import org.junit.jupiter.api.Test;
import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckModuleUseCaseTest {

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
    void consultarModulosDisponibles_retornaVacio_cuandoEstudianteNoExiste() {

        BULL_StudentRepository studentRepo = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository courseRepo = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository groupRepo = new BULL_InMemoryGroupRepository();

        CheckModuleUseCase useCase =
                new CheckModuleUseCase(
                        studentRepo,
                        courseRepo,
                        modalityRepo,
                        groupRepo
                );

        List<ModuleOptionDTO> resultado =
                useCase.consultarModulosDisponibles("999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void consultarModulosDisponibles_retornaVacio_cuandoNoExisteCurso() {

        BULL_StudentRepository studentRepo = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository courseRepo = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository groupRepo = new BULL_InMemoryGroupRepository();

        BULL_Student student = createStudent();
        studentRepo.save(student);

        CheckModuleUseCase useCase =
                new CheckModuleUseCase(
                        studentRepo,
                        courseRepo,
                        modalityRepo,
                        groupRepo
                );

        List<ModuleOptionDTO> resultado =
                useCase.consultarModulosDisponibles(
                        student.getUniversityCode()
                );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void consultarModulosDisponibles_retornaOpcionesDisponibles() {

        BULL_StudentRepository studentRepo = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository courseRepo = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository groupRepo = new BULL_InMemoryGroupRepository();

        BULL_Student student = createStudent();
        studentRepo.save(student);

        BULL_Course course = new BULL_Course(1, 3);
        courseRepo.save(course);

        BULL_Group group = new BULL_Group(10);

        group.setProfessor(
                new BULL_Professor(
                        "P1",
                        "Carlos",
                        "carlos@correo.com"
                )
        );

        group.setMaxCapacity(
                new BULL_MaxCapacity(30)
        );

        BULL_Schedule schedule = new BULL_Schedule();
        schedule.addTimeSlot("Lunes", "8-10");
        group.setSchedule(schedule);

        groupRepo.save(group);

        BULL_OnSitePresencial modality =
                new BULL_OnSitePresencial();

        modality.addGroup(group);

        modalityRepo.save(modality);

        CheckModuleUseCase useCase =
                new CheckModuleUseCase(
                        studentRepo,
                        courseRepo,
                        modalityRepo,
                        groupRepo
                );

        List<ModuleOptionDTO> resultado =
                useCase.consultarModulosDisponibles(
                        student.getUniversityCode()
                );

        assertEquals(1, resultado.size());
    }

    @Test
    void verificarDisponibilidad_retornaError_cuandoEstudianteNoExiste() {

        CheckModuleUseCase useCase =
                new CheckModuleUseCase(
                        new BULL_InMemoryStudentRepository(),
                        new BULL_InMemoryCourseRepository(),
                        new BULL_InMemoryModalityRepository(),
                        new BULL_InMemoryGroupRepository()
                );

        OperationResult resultado =
                useCase.verificarDisponibilidad("999");

        assertFalse(resultado.isSuccess());
    }

    @Test
    void verificarDisponibilidad_retornaOk_cuandoHayOpciones() {

        BULL_StudentRepository studentRepo = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository courseRepo = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository groupRepo = new BULL_InMemoryGroupRepository();

        BULL_Student student = createStudent();
        studentRepo.save(student);

        courseRepo.save(new BULL_Course(1, 3));

        BULL_Group group = new BULL_Group(1);

        group.setProfessor(
                new BULL_Professor(
                        "P1",
                        "Carlos",
                        "carlos@correo.com"
                )
        );

        group.setMaxCapacity(
                new BULL_MaxCapacity(20)
        );

        BULL_Schedule schedule = new BULL_Schedule();
        schedule.addTimeSlot("Martes", "10-12");
        group.setSchedule(schedule);

        groupRepo.save(group);

        BULL_OnSitePresencial modality =
                new BULL_OnSitePresencial();

        modality.addGroup(group);

        modalityRepo.save(modality);

        CheckModuleUseCase useCase =
                new CheckModuleUseCase(
                        studentRepo,
                        courseRepo,
                        modalityRepo,
                        groupRepo
                );

        OperationResult resultado =
                useCase.verificarDisponibilidad(
                        student.getUniversityCode()
                );

        assertTrue(resultado.isSuccess());
    }
}