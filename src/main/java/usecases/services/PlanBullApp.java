package usecases.services;

import entities.*;
import infrastructure.repositories.*;
import usecases.dto.*;
import usecases.ports.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanBullApp {

    private final BULL_StudentRepository      studentRepository;
    private final BULL_ProfessorRepository    professorRepository;
    private final BULL_CourseRepository       courseRepository;
    private final BULL_GroupRepository        groupRepository;
    private final BULL_ModalityRepository     modalityRepository;
    private final BULL_ScheduleRepository     scheduleRepository;
    private final BULL_RegistrationRepository registrationRepository;
    private final CheckCourseUseCase             checkCourseUseCase;
    private final CourseRegistrationUseCase      courseRegistrationUseCase;
    private final CancelInscriptionUseCase       cancelInscriptionUseCase;
    private final ManageCourseUseCase manageCourseUseCase;
    private final AssignSpaceUseCase             assignSpaceUseCase;
    private final LoadNotesUsecase               loadNotesUsecase;
    private final RequestHomologationUseCase     requestHomologationUseCase;
    private final ValidateHomologationUseCase    validateHomologationUseCase;
    private final AnnounceCallForApplicationsUseCase announceCallUseCase;
    private final EstablishCalendarUseCase establishCalendarUseCase;

    public PlanBullApp(BULL_StudentRepository studentRepository,
                       BULL_ProfessorRepository professorRepository,
                       BULL_CourseRepository courseRepository,
                       BULL_GroupRepository groupRepository,
                       BULL_ModalityRepository modalityRepository,
                       BULL_ScheduleRepository scheduleRepository,
                       BULL_RegistrationRepository registrationRepository,
                       CheckCourseUseCase checkCourseUseCase,
                       CourseRegistrationUseCase courseRegistrationUseCase,
                       CancelInscriptionUseCase cancelInscriptionUseCase,
                       ManageCourseUseCase manageCourseUseCase,
                       AssignSpaceUseCase assignSpaceUseCase,
                       LoadNotesUsecase loadNotesUsecase,
                       RequestHomologationUseCase requestHomologationUseCase,
                       ValidateHomologationUseCase validateHomologationUseCase,
                       AnnounceCallForApplicationsUseCase announceCallUseCase,
                       EstablishCalendarUseCase establishCalendarUseCase
                       ) {
        this.studentRepository           = studentRepository;
        this.professorRepository         = professorRepository;
        this.courseRepository            = courseRepository;
        this.groupRepository             = groupRepository;
        this.modalityRepository          = modalityRepository;
        this.scheduleRepository          = scheduleRepository;
        this.registrationRepository      = registrationRepository;
        this.checkCourseUseCase          = checkCourseUseCase;
        this.courseRegistrationUseCase   = courseRegistrationUseCase;
        this.cancelInscriptionUseCase    = cancelInscriptionUseCase;
        this.manageCourseUseCase = manageCourseUseCase;
        this.assignSpaceUseCase          = assignSpaceUseCase;
        this.loadNotesUsecase            = loadNotesUsecase;
        this.requestHomologationUseCase  = requestHomologationUseCase;
        this.validateHomologationUseCase = validateHomologationUseCase;
        this.announceCallUseCase         = announceCallUseCase;
        this.establishCalendarUseCase    = establishCalendarUseCase;
    }

    public PlanBullApp() {
        this(
                new BULL_InMemoryStudentRepository(),
                new BULL_InMemoryProfessorRepository(),
                new BULL_InMemoryCourseRepository(),
                new BULL_InMemoryGroupRepository(),
                new BULL_InMemoryModalityRepository(),
                new BULL_InMemoryScheduleRepository(),
                new BULL_InMemoryRegistrationRepository(),
                new BULL_InMemoryHomologationRepository()
        );
    }

    private PlanBullApp(BULL_StudentRepository      studentRepository,
                        BULL_ProfessorRepository    professorRepository,
                        BULL_CourseRepository       courseRepository,
                        BULL_GroupRepository        groupRepository,
                        BULL_ModalityRepository     modalityRepository,
                        BULL_ScheduleRepository     scheduleRepository,
                        BULL_RegistrationRepository registrationRepository,
                        BULL_HomologationRepository homologationRepository) {
        this.studentRepository      = studentRepository;
        this.professorRepository    = professorRepository;
        this.courseRepository       = courseRepository;
        this.groupRepository        = groupRepository;
        this.modalityRepository     = modalityRepository;
        this.scheduleRepository     = scheduleRepository;
        this.registrationRepository = registrationRepository;

        this.checkCourseUseCase = new CheckCourseUseCase(
                studentRepository, courseRepository, modalityRepository, groupRepository);

        this.courseRegistrationUseCase = new CourseRegistrationUseCase(
                groupRepository, studentRepository, registrationRepository,
                modalityRepository, homologationRepository);

        this.cancelInscriptionUseCase = new CancelInscriptionUseCase(
                registrationRepository, studentRepository, groupRepository);

        this.manageCourseUseCase = new ManageCourseUseCase(courseRepository);

        this.assignSpaceUseCase = new AssignSpaceUseCase(groupRepository, modalityRepository);

        this.loadNotesUsecase = new LoadNotesUsecase(groupRepository, registrationRepository,studentRepository);

        this.requestHomologationUseCase = new RequestHomologationUseCase(
                studentRepository, homologationRepository);

        this.validateHomologationUseCase = new ValidateHomologationUseCase(homologationRepository);

        this.announceCallUseCase      = new AnnounceCallForApplicationsUseCase(studentRepository);
        this.establishCalendarUseCase = new EstablishCalendarUseCase(groupRepository, scheduleRepository);
    }

    public OperationResult crearCourse(int idCourse, int courseNumber) {
        return manageCourseUseCase.crearCourse(idCourse, courseNumber);
    }

    public OperationResult consultarCourses() {
        return manageCourseUseCase.consultarCourses();
    }

    public OperationResult consultarCoursePorId(int idCourse) {
        return manageCourseUseCase.consultarCoursePorId(idCourse);
    }

    public OperationResult eliminarCourse(int idCourse) {
        return manageCourseUseCase.eliminarCourse(idCourse);
    }

    public List<CourseOptionDTO> consultarCoursesDisponibles(String universityCode) {
        return checkCourseUseCase.consultarCoursesDisponibles(universityCode);
    }

    public OperationResult inscribirEstudiante(String universityCode, int indiceOpcion) {
        List<CourseOptionDTO> opciones = checkCourseUseCase.consultarCoursesDisponibles(universityCode);
        if (opciones.isEmpty()) {
            return OperationResult.fail("No hay módulos disponibles para el estudiante " + universityCode + ".");
        }
        if (indiceOpcion < 0 || indiceOpcion >= opciones.size()) {
            return OperationResult.fail(
                    "Opción " + (indiceOpcion + 1) + " no existe. Hay " + opciones.size() + " opción(es).");
        }
        return courseRegistrationUseCase.inscribirCourse(universityCode, opciones.get(indiceOpcion));
    }

    public OperationResult cancelarInscripcion(String idRegistration, String universityCode) {
        return cancelInscriptionUseCase.cancelarInscripcion(idRegistration, universityCode);
    }

    public OperationResult asignarEspacio(int idGroup, String edificio, String aula) {
        return assignSpaceUseCase.asignarEspacio(idGroup, edificio, aula);
    }

    public OperationResult cargarNota(int idGroup, String idRegistration, GradeType tipo, double valor) {
        return loadNotesUsecase.execute(idGroup, idRegistration, tipo, valor);
    }

    public OperationResult solicitarHomologacion(String universityCode,
                                                 String fileName,
                                                 String fileType) {
        return requestHomologationUseCase.execute(universityCode, fileName, fileType);
    }

    public List<HomologationDTO> listarHomologacionesPendientes() {
        List<HomologationDTO> result = new ArrayList<>();
        for (BULL_Homologation h : validateHomologationUseCase.listPending()) {
            result.add(new HomologationDTO(
                    h.getStudent().getUniversityCode(),
                    h.getStudent().getName() + " " + h.getStudent().getSurnames(),
                    h.getCertificate().getFileName(),
                    h.getCertificate().getFileType(),
                    h.getStatus().name()
            ));
        }
        return result;
    }

    public OperationResult aprobarHomologacion(String universityCode,
                                               int courseNumber,
                                               String observation) {
        return validateHomologationUseCase.approve(universityCode, courseNumber, observation);
    }

    public OperationResult rechazarHomologacion(String universityCode, String reason) {
        return validateHomologationUseCase.reject(universityCode, reason);
    }

    public OperationResult openCall(String callId, int year, int period, String description) {
        BULL_Call call = new BULL_Call(callId, year, period, description);
        call.open();
        return announceCallUseCase.announce(call);
    }

    public OperationResult establecerCalendario(int idGroup, String scheduleId, Map<String, String> franjas) {
        return establishCalendarUseCase.establecerCalendario(idGroup, scheduleId, franjas);
    }

    public List<StudentDTO> getEstudiantes() {
        List<StudentDTO> result = new ArrayList<>();
        for (BULL_Student s : studentRepository.findAll()) {
            result.add(new StudentDTO(
                    s.getUniversityCode(),
                    s.getName() + " " + s.getSurnames(),
                    s.getSemester(),
                    s.isSpecialCondition()
            ));
        }
        return result;
    }

    public List<ProfessorDTO> getProfesores() {
        List<ProfessorDTO> result = new ArrayList<>();
        for (BULL_Professor p : professorRepository.findAll()) {
            result.add(new ProfessorDTO(
                    p.getIdTeaching(),
                    p.getName(),
                    p.getMail()
            ));
        }
        return result;
    }

    public List<RegistrationDTO> getInscripciones() {
        List<RegistrationDTO> result = new ArrayList<>();
        for (BULL_Registration r : registrationRepository.findAll()) {
            result.add(new RegistrationDTO(
                    r.getIdRegistration(),
                    r.getStudent().getName() + " " + r.getStudent().getSurnames(),
                    r.getGroup().getIdGroup(),
                    r.getState()
            ));
        }
        return result;
    }

    public List<GroupDTO> getGrupos() {
        List<GroupDTO> result = new ArrayList<>();
        for (BULL_Group g : groupRepository.findAll()) {
            String profesor  = g.getProfessor()   != null ? g.getProfessor().getName() : "Sin profesor";
            String cupos     = g.getMaxCapacity() != null ? g.getMaxCapacity().getCuposRestantes() + " cupos" : "Sin cupo";
            String ubicacion = g.getUbication()   != null ? g.getUbication().getBuilding() + " - " + g.getUbication().getClassroomNum() : "Virtual";
            result.add(new GroupDTO(g.getIdGroup(), profesor, cupos, ubicacion));
        }
        return result;
    }
}