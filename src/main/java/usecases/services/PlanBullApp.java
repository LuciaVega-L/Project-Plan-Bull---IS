package usecases.services;

import entities.*;
import infrastructure.repositories.*;
import usecases.dto.CourseOptionDTO;
import usecases.dto.OpenCallDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.List;

public class PlanBullApp {

    private final BULL_StudentRepository      studentRepository;
    private final BULL_ProfessorRepository    professorRepository;
    private final BULL_CourseRepository       courseRepository;
    private final BULL_GroupRepository        groupRepository;
    private final BULL_ModalityRepository     modalityRepository;
    private final BULL_ScheduleRepository     scheduleRepository;
    private final BULL_RegistrationRepository registrationRepository;

    private final CheckCourseUseCase              checkCourseUseCase;
    private final CourseRegistrationUseCase       courseRegistrationUseCase;
    private final CancelInscriptionUseCase        cancelInscriptionUseCase;
    private final ManageCourseUseCase             manageCourseUseCase;
    private final AssignSpaceUseCase              assignSpaceUseCase;
    private final LoadNotesUsecase                loadNotesUsecase;
    private final RequestHomologationUseCase      requestHomologationUseCase;
    private final ValidateHomologationUseCase     validateHomologationUseCase;
    private final OpenCallForApplicationsUseCase  openCallForApplicationsUseCase;

    public PlanBullApp() {

        // --- Repositorios ---
        this.studentRepository      = new BULL_InMemoryStudentRepository();
        this.professorRepository    = new BULL_InMemoryProfessorRepository();
        this.courseRepository       = new BULL_InMemoryCourseRepository();
        this.groupRepository        = new BULL_InMemoryGroupRepository();
        this.modalityRepository     = new BULL_InMemoryModalityRepository();
        this.scheduleRepository     = new BULL_InMemoryScheduleRepository();
        this.registrationRepository = new BULL_InMemoryRegistrationRepository();

        BULL_HomologationRepository homologationRepository = new BULL_InMemoryHomologationRepository();
        BULL_CallRepository         callRepository         = new BULL_InMemoryCallRepository();

        // --- Casos de uso ---
        this.checkCourseUseCase = new CheckCourseUseCase(
                studentRepository, courseRepository, modalityRepository, groupRepository);

        this.courseRegistrationUseCase = new CourseRegistrationUseCase(
                groupRepository, studentRepository, registrationRepository,
                modalityRepository, homologationRepository);

        this.cancelInscriptionUseCase = new CancelInscriptionUseCase(
                registrationRepository, studentRepository, groupRepository);

        this.manageCourseUseCase = new ManageCourseUseCase(courseRepository);

        this.assignSpaceUseCase = new AssignSpaceUseCase(groupRepository, modalityRepository);

        this.loadNotesUsecase = new LoadNotesUsecase(groupRepository, registrationRepository, studentRepository);

        this.requestHomologationUseCase = new RequestHomologationUseCase(
                studentRepository, homologationRepository);

        this.validateHomologationUseCase = new ValidateHomologationUseCase(homologationRepository);

        AnnounceCallForApplicationsUseCase announceUseCase =
                new AnnounceCallForApplicationsUseCase(studentRepository);

        this.openCallForApplicationsUseCase = new OpenCallForApplicationsUseCase(
                callRepository, announceUseCase);

        cargarDatosIniciales();
    }

    // Datos iniciales
    public void cargarDatosIniciales() {

        BULL_Course modulo1 = new BULL_Course(1, 1);
        BULL_Course modulo2 = new BULL_Course(2, 2);
        courseRepository.save(modulo1);
        courseRepository.save(modulo2);

        BULL_Professor prof1 = new BULL_Professor("DOC-001", "Carlos Pérez",   "cperez@universidad.edu");
        BULL_Professor prof2 = new BULL_Professor("DOC-002", "Laura Gómez",    "lgomez@universidad.edu");
        professorRepository.save(prof1);
        professorRepository.save(prof2);

        BULL_Schedule horario1 = new BULL_Schedule();
        horario1.addTimeSlot("Lunes",    "07:00-09:00");
        horario1.addTimeSlot("Miércoles","07:00-09:00");
        scheduleRepository.save("SCH-001", horario1);

        BULL_Schedule horario2 = new BULL_Schedule();
        horario2.addTimeSlot("Martes",  "14:00-16:00");
        horario2.addTimeSlot("Jueves",  "14:00-16:00");
        scheduleRepository.save("SCH-002", horario2);

        BULL_Group grupo1 = new BULL_Group(1);
        grupo1.setProfessor(prof1);
        grupo1.setSchedule(horario1);
        grupo1.setMaxCapacity(new BULL_MaxCapacity(30));

        BULL_Group grupo2 = new BULL_Group(2);
        grupo2.setProfessor(prof2);
        grupo2.setSchedule(horario2);
        grupo2.setMaxCapacity(new BULL_MaxCapacity(25));

        groupRepository.save(grupo1);
        groupRepository.save(grupo2);

        BULL_OnSitePresencial presencial = new BULL_OnSitePresencial();
        presencial.addGroup(grupo1);
        modalityRepository.save(presencial);

        BULL_SynchronousVirtualModality virtual = new BULL_SynchronousVirtualModality();
        virtual.addGroup(grupo2);
        modalityRepository.save(virtual);

        java.util.Date hoy   = new java.util.Date();
        java.util.Date fin   = new java.util.Date(hoy.getTime() + 120L * 24 * 60 * 60 * 1000);
        java.util.Date inicio = new java.util.Date(hoy.getTime() - 30L * 24 * 60 * 60 * 1000);

        BULL_Semester semestre = new BULL_Semester(2025, 1, inicio, fin);
        semestre.addModality(presencial);
        semestre.addModality(virtual);
        modulo1.addSemester(semestre);
        modulo2.addSemester(semestre);

        BULL_Student est1 = new BULL_Student("001", "Andrés",   "Torres",   "atorres@uni.edu",   3, "Ingeniería de Sistemas", false);
        BULL_Student est2 = new BULL_Student("002", "María",    "Ramírez",  "mramirez@uni.edu",  2, "Ingeniería de Sistemas", false);
        BULL_Student est3 = new BULL_Student("003", "Sofía",    "Vargas",   "svargas@uni.edu",   4, "Ingeniería de Sistemas", true);
        studentRepository.save(est1);
        studentRepository.save(est2);
        studentRepository.save(est3);

        BULL_Registration reg1 = new BULL_Registration(est1, grupo1);
        grupo1.addRegistration(reg1);
        est1.addRegistration(reg1);
        registrationRepository.save(reg1);
        studentRepository.save(est1);
        groupRepository.save(grupo1);

        BULL_Registration reg2 = new BULL_Registration(est2, grupo2);
        grupo2.addRegistration(reg2);
        est2.addRegistration(reg2);
        registrationRepository.save(reg2);
        studentRepository.save(est2);
        groupRepository.save(grupo2);

    }

    // Módulos
    public OperationResult crearModulo(int idCourse, int courseNumber) {
        return manageCourseUseCase.crearModulo(idCourse, courseNumber);
    }

    public OperationResult consultarModulos() {
        return manageCourseUseCase.consultarModulos();
    }

    public OperationResult consultarModuloPorId(int idCourse) {
        return manageCourseUseCase.consultarModuloPorId(idCourse);
    }

    public OperationResult eliminarModulo(int idCourse) {
        return manageCourseUseCase.eliminarModulo(idCourse);
    }

    // Inscripciones
    public List<CourseOptionDTO> consultarModulosDisponibles(String universityCode) {
        return checkCourseUseCase.consultarModulosDisponibles(universityCode);
    }

    public OperationResult inscribirEstudiante(String universityCode, int indiceOpcion) {
        List<CourseOptionDTO> opciones = checkCourseUseCase.consultarModulosDisponibles(universityCode);
        if (opciones.isEmpty())
            return OperationResult.fail("No hay módulos disponibles para el estudiante " + universityCode + ".");
        if (indiceOpcion < 0 || indiceOpcion >= opciones.size())
            return OperationResult.fail(
                    "Opción " + (indiceOpcion + 1) + " no existe. Hay " + opciones.size() + " opción(es).");
        return courseRegistrationUseCase.inscribirModulo(universityCode, opciones.get(indiceOpcion));
    }

    public OperationResult cancelarInscripcion(String idRegistration, String universityCode) {
        return cancelInscriptionUseCase.cancelarInscripcion(idRegistration, universityCode);
    }

    // Espacios
    public OperationResult asignarEspacio(int idGroup, String edificio, String aula) {
        return assignSpaceUseCase.asignarEspacio(idGroup, edificio, aula);
    }

    // Notas
    public OperationResult cargarNota(int idGroup, String idRegistration, GradeType tipo, double valor) {
        return loadNotesUsecase.execute(idGroup, idRegistration, tipo, valor);
    }

    // Homologaciones
    public OperationResult solicitarHomologacion(String universityCode, String fileName, String fileType) {
        return requestHomologationUseCase.execute(universityCode, fileName, fileType);
    }

    public List<BULL_Homologation> listarHomologacionesPendientes() {
        return validateHomologationUseCase.listPending();
    }

    public OperationResult aprobarHomologacion(String universityCode, int moduleNumber, String observation) {
        return validateHomologationUseCase.approve(universityCode, moduleNumber, observation);
    }

    public OperationResult rechazarHomologacion(String universityCode, String reason) {
        return validateHomologationUseCase.reject(universityCode, reason);
    }

    // Convocatorias
    public OperationResult openCall(String callId, int year, int period, String description) {
        return openCallForApplicationsUseCase.open(new OpenCallDTO(callId, year, period, description));
    }

    // Consultas generales
    public List<BULL_Student>      getEstudiantes()    { return studentRepository.findAll(); }
    public List<BULL_Professor>    getProfesores()     { return professorRepository.findAll(); }
    public List<BULL_Registration> getInscripciones()  { return registrationRepository.findAll(); }
    public List<BULL_Group>        getGrupos()         { return groupRepository.findAll(); }
}