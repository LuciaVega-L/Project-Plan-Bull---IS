package usecases.services;

import entities.*;
import infrastructure.repositories.BULL_InMemoryStudentRepository;
import infrastructure.repositories.BULL_InMemoryProfessorRepository;
import infrastructure.repositories.BULL_InMemoryCourseRepository;
import infrastructure.repositories.BULL_InMemoryGroupRepository;
import infrastructure.repositories.BULL_InMemoryModalityRepository;
import infrastructure.repositories.BULL_InMemoryScheduleRepository;
import infrastructure.repositories.BULL_InMemoryRegistrationRepository;
import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;
import usecases.services.*;

import java.util.Date;
import java.util.List;

public class PlanBullApp {


    private final BULL_StudentRepository      studentRepository;
    private final BULL_ProfessorRepository    professorRepository;
    private final BULL_CourseRepository       courseRepository;
    private final BULL_GroupRepository        groupRepository;
    private final BULL_ModalityRepository     modalityRepository;
    private final BULL_ScheduleRepository     scheduleRepository;
    private final BULL_RegistrationRepository registrationRepository;


    private final CheckModuleUseCase         checkModuleUseCase;
    private final CourseRegistrationUseCase  courseRegistrationUseCase;
    private final CancelInscriptionUseCase   cancelInscriptionUseCase;

    public PlanBullApp() {
        this.studentRepository      = new BULL_InMemoryStudentRepository();
        this.professorRepository    = new BULL_InMemoryProfessorRepository();
        this.courseRepository       = new BULL_InMemoryCourseRepository();
        this.groupRepository        = new BULL_InMemoryGroupRepository();
        this.modalityRepository     = new BULL_InMemoryModalityRepository();
        this.scheduleRepository     = new BULL_InMemoryScheduleRepository();
        this.registrationRepository = new BULL_InMemoryRegistrationRepository();


        this.checkModuleUseCase        = new CheckModuleUseCase(courseRepository, modalityRepository, groupRepository);
        this.courseRegistrationUseCase = new CourseRegistrationUseCase(groupRepository, studentRepository, registrationRepository);
        this.cancelInscriptionUseCase  = new CancelInscriptionUseCase(registrationRepository, studentRepository, groupRepository);
        cargarDatosIniciales();
    }

    public PlanBullApp(BULL_StudentRepository studentRepository,
                       BULL_ProfessorRepository professorRepository,
                       BULL_CourseRepository courseRepository,
                       BULL_GroupRepository groupRepository,
                       BULL_ModalityRepository modalityRepository,
                       BULL_ScheduleRepository scheduleRepository,
                       BULL_RegistrationRepository registrationRepository,
                       CheckModuleUseCase checkModuleUseCase,
                       CourseRegistrationUseCase courseRegistrationUseCase,
                       CancelInscriptionUseCase cancelInscriptionUseCase) {
        this.studentRepository      = studentRepository;
        this.professorRepository    = professorRepository;
        this.courseRepository       = courseRepository;
        this.groupRepository        = groupRepository;
        this.modalityRepository     = modalityRepository;
        this.scheduleRepository     = scheduleRepository;
        this.registrationRepository = registrationRepository;
        this.checkModuleUseCase        = checkModuleUseCase;
        this.courseRegistrationUseCase = courseRegistrationUseCase;
        this.cancelInscriptionUseCase  = cancelInscriptionUseCase;

    }


    private void cargarDatosIniciales() {
        cargarEstudiantesIniciales();
        cargarProfesoresIniciales();
    }

    private void cargarEstudiantesIniciales() {
        studentRepository.save(new BULL_Student(
                "20231001", "Carlos", "Pérez", "carlos@unillanos.edu.co", 3, "Ingeniería de Sistemas", false));
        studentRepository.save(new BULL_Student(
                "20231002", "Laura", "Gómez", "laura@unillanos.edu.co", 2, "Ingeniería de Sistemas", false));
        studentRepository.save(new BULL_Student(
                "20231003", "Andrés", "Torres", "andres@unillanos.edu.co", 4, "Ingeniería de Sistemas", true));
    }

    private void cargarProfesoresIniciales() {
        professorRepository.save(new BULL_Professor(
                "DOC001", "Luis Martínez", "luis.martinez@unillanos.edu.co"));
        professorRepository.save(new BULL_Professor(
                "DOC002", "Ana Rodríguez", "ana.rodriguez@unillanos.edu.co"));
    }

    public OperationResult consultarModulos() {
        return checkModuleUseCase.consultarModulos();
    }

    public List<ModuleOptionDTO> consultarModulosComoLista() {
        return checkModuleUseCase.consultarModulosComoLista();
    }

    public List<BULL_Group> consultarGruposPorModulo(int idModule) {
        return checkModuleUseCase.consultarGruposPorModulo(idModule);
    }

    public OperationResult inscribirModulo(int idGrupo, String universityCode) {
        return courseRegistrationUseCase.inscribirModulo(idGrupo, universityCode);
    }

    public OperationResult cancelarInscripcion(String idRegistration, String universityCode) {
        return cancelInscriptionUseCase.cancelarInscripcion(idRegistration, universityCode);
    }

    public List<BULL_Student> getEstudiantes() {
        return studentRepository.findAll();
    }

    public List<BULL_Professor> getProfesores() {
        return professorRepository.findAll();
    }

    public List<BULL_Registration> getInscripciones() {
        return registrationRepository.findAll();
    }

}