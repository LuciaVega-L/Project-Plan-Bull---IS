package usecases.services;

import entities.*;
import infrastructure.repositories.*;
import org.junit.jupiter.api.Test;
import usecases.dto.CourseOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckCourseUseCaseTest {

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

    private CheckCourseUseCase buildUseCase(
            BULL_StudentRepository studentRepo,
            BULL_CourseRepository courseRepo,
            BULL_ModalityRepository modalityRepo,
            BULL_GroupRepository groupRepo,
            BULL_HomologationRepository homologationRepo) {
        return new CheckCourseUseCase(
                studentRepo, courseRepo, modalityRepo, groupRepo, homologationRepo);
    }

    @Test
    void consultarCoursesDisponibles_retornaVacio_cuandoEstudianteNoExiste() {

        CheckCourseUseCase useCase = buildUseCase(
                new BULL_InMemoryStudentRepository(),
                new BULL_InMemoryCourseRepository(),
                new BULL_InMemoryModalityRepository(),
                new BULL_InMemoryGroupRepository(),
                new BULL_InMemoryHomologationRepository()
        );

        List<CourseOptionDTO> resultado = useCase.consultarCoursesDisponibles("999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void consultarCoursesDisponibles_retornaVacio_cuandoNoExisteCurso() {

        BULL_StudentRepository studentRepo   = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository  courseRepo    = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository   groupRepo     = new BULL_InMemoryGroupRepository();
        BULL_HomologationRepository homoRepo = new BULL_InMemoryHomologationRepository();

        studentRepo.save(createStudent());

        CheckCourseUseCase useCase = buildUseCase(
                studentRepo, courseRepo, modalityRepo, groupRepo, homoRepo);

        List<CourseOptionDTO> resultado =
                useCase.consultarCoursesDisponibles("20201234");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void consultarCoursesDisponibles_retornaOpciones_cuandoTodoEstaDisponible() {

        BULL_StudentRepository  studentRepo  = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository   courseRepo   = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository    groupRepo    = new BULL_InMemoryGroupRepository();
        BULL_HomologationRepository homoRepo = new BULL_InMemoryHomologationRepository();

        studentRepo.save(createStudent());

        // Estudiante semestre 5, sin inscripciones → maxAprobado=0 → busca courseNumber=1
        courseRepo.save(new BULL_Course(1, 1));

        BULL_Group group = new BULL_Group(10);
        group.setProfessor(new BULL_Professor("P1", "Carlos", "carlos@correo.com"));
        group.setMaxCapacity(new BULL_MaxCapacity(30));
        BULL_Schedule schedule = new BULL_Schedule();
        schedule.addTimeSlot("Lunes", "08:00-10:00");
        group.setSchedule(schedule);
        groupRepo.save(group);

        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();
        modality.addGroup(group);
        modalityRepo.save(modality);

        CheckCourseUseCase useCase = buildUseCase(
                studentRepo, courseRepo, modalityRepo, groupRepo, homoRepo);

        List<CourseOptionDTO> resultado =
                useCase.consultarCoursesDisponibles("20201234");

        assertEquals(1, resultado.size());
    }

    @Test
    void consultarCoursesDisponibles_respetaHomologacion_aprobada() {

        BULL_StudentRepository  studentRepo  = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository   courseRepo   = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository    groupRepo    = new BULL_InMemoryGroupRepository();
        BULL_HomologationRepository homoRepo = new BULL_InMemoryHomologationRepository();

        BULL_Student student = createStudent();
        studentRepo.save(student);

        // Homologación aprobada hasta módulo 2 → busca courseNumber=3
        BULL_Certificate cert = new BULL_Certificate("cert.pdf", "PDF", "/path");
        BULL_Homologation homo = new BULL_Homologation(student, cert);
        homo.approve(2, "Aprobado por experiencia previa");
        homoRepo.save(homo);

        courseRepo.save(new BULL_Course(3, 3));

        BULL_Group group = new BULL_Group(20);
        group.setProfessor(new BULL_Professor("P2", "Ana", "ana@correo.com"));
        group.setMaxCapacity(new BULL_MaxCapacity(25));
        BULL_Schedule schedule = new BULL_Schedule();
        schedule.addTimeSlot("Miercoles", "10:00-12:00");
        group.setSchedule(schedule);
        groupRepo.save(group);

        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();
        modality.addGroup(group);
        modalityRepo.save(modality);

        CheckCourseUseCase useCase = buildUseCase(
                studentRepo, courseRepo, modalityRepo, groupRepo, homoRepo);

        List<CourseOptionDTO> resultado =
                useCase.consultarCoursesDisponibles("20201234");

        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getCourseNumber());
    }

    @Test
    void verificarDisponibilidad_retornaError_cuandoEstudianteNoExiste() {

        CheckCourseUseCase useCase = buildUseCase(
                new BULL_InMemoryStudentRepository(),
                new BULL_InMemoryCourseRepository(),
                new BULL_InMemoryModalityRepository(),
                new BULL_InMemoryGroupRepository(),
                new BULL_InMemoryHomologationRepository()
        );

        OperationResult resultado = useCase.verificarDisponibilidad("999");

        assertFalse(resultado.isSuccess());
    }

    @Test
    void verificarDisponibilidad_retornaOk_cuandoHayOpciones() {

        BULL_StudentRepository  studentRepo  = new BULL_InMemoryStudentRepository();
        BULL_CourseRepository   courseRepo   = new BULL_InMemoryCourseRepository();
        BULL_ModalityRepository modalityRepo = new BULL_InMemoryModalityRepository();
        BULL_GroupRepository    groupRepo    = new BULL_InMemoryGroupRepository();
        BULL_HomologationRepository homoRepo = new BULL_InMemoryHomologationRepository();

        studentRepo.save(createStudent());
        courseRepo.save(new BULL_Course(1, 1));

        BULL_Group group = new BULL_Group(1);
        group.setProfessor(new BULL_Professor("P1", "Carlos", "carlos@correo.com"));
        group.setMaxCapacity(new BULL_MaxCapacity(20));
        BULL_Schedule schedule = new BULL_Schedule();
        schedule.addTimeSlot("Martes", "10:00-12:00");
        group.setSchedule(schedule);
        groupRepo.save(group);

        BULL_OnSitePresencial modality = new BULL_OnSitePresencial();
        modality.addGroup(group);
        modalityRepo.save(modality);

        CheckCourseUseCase useCase = buildUseCase(
                studentRepo, courseRepo, modalityRepo, groupRepo, homoRepo);

        OperationResult resultado = useCase.verificarDisponibilidad("20201234");

        assertTrue(resultado.isSuccess());
    }
}