package usecases.services;

import entities.*;
import infrastructure.repositories.*;
import usecases.dto.ModuleOptionDTO;
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

    private final CheckModuleUseCase             checkModuleUseCase;
    private final CourseRegistrationUseCase      courseRegistrationUseCase;
    private final CancelInscriptionUseCase       cancelInscriptionUseCase;
    private final ManageModuleUseCase            manageModuleUseCase;
    private final AssignSpaceUseCase             assignSpaceUseCase;
    private final LoadNotesUsecase               loadNotesUsecase;
    private final RequestHomologationUseCase     requestHomologationUseCase;
    private final ValidateHomologationUseCase    validateHomologationUseCase;

    public PlanBullApp(BULL_StudentRepository studentRepository,
                       BULL_ProfessorRepository professorRepository,
                       BULL_CourseRepository courseRepository,
                       BULL_GroupRepository groupRepository,
                       BULL_ModalityRepository modalityRepository,
                       BULL_ScheduleRepository scheduleRepository,
                       BULL_RegistrationRepository registrationRepository,
                       CheckModuleUseCase checkModuleUseCase,
                       CourseRegistrationUseCase courseRegistrationUseCase,
                       CancelInscriptionUseCase cancelInscriptionUseCase,
                       ManageModuleUseCase manageModuleUseCase,
                       AssignSpaceUseCase assignSpaceUseCase,
                       LoadNotesUsecase loadNotesUsecase,
                       RequestHomologationUseCase requestHomologationUseCase,
                       ValidateHomologationUseCase validateHomologationUseCase) {
        this.studentRepository           = studentRepository;
        this.professorRepository         = professorRepository;
        this.courseRepository            = courseRepository;
        this.groupRepository             = groupRepository;
        this.modalityRepository          = modalityRepository;
        this.scheduleRepository          = scheduleRepository;
        this.registrationRepository      = registrationRepository;
        this.checkModuleUseCase          = checkModuleUseCase;
        this.courseRegistrationUseCase   = courseRegistrationUseCase;
        this.cancelInscriptionUseCase    = cancelInscriptionUseCase;
        this.manageModuleUseCase         = manageModuleUseCase;
        this.assignSpaceUseCase          = assignSpaceUseCase;
        this.loadNotesUsecase            = loadNotesUsecase;
        this.requestHomologationUseCase  = requestHomologationUseCase;
        this.validateHomologationUseCase = validateHomologationUseCase;
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

        this.checkModuleUseCase = new CheckModuleUseCase(
                studentRepository, courseRepository, modalityRepository, groupRepository);

        this.courseRegistrationUseCase = new CourseRegistrationUseCase(
                groupRepository, studentRepository, registrationRepository,
                modalityRepository, homologationRepository);

        this.cancelInscriptionUseCase = new CancelInscriptionUseCase(
                registrationRepository, studentRepository, groupRepository);

        this.manageModuleUseCase = new ManageModuleUseCase(courseRepository);

        this.assignSpaceUseCase = new AssignSpaceUseCase(groupRepository, modalityRepository);

        this.loadNotesUsecase = new LoadNotesUsecase(groupRepository, registrationRepository);

        this.requestHomologationUseCase = new RequestHomologationUseCase(
                studentRepository, homologationRepository);

        this.validateHomologationUseCase = new ValidateHomologationUseCase(homologationRepository);

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        studentRepository.save(new BULL_Student(
                "20231001", "Carlos", "Pérez", "carlos@unillanos.edu.co", 3, "Ingeniería de Sistemas", false));
        studentRepository.save(new BULL_Student(
                "20231002", "Laura", "Gómez", "laura@unillanos.edu.co", 2, "Ingeniería de Sistemas", false));
        studentRepository.save(new BULL_Student(
                "20231003", "Andrés", "Torres", "andres@unillanos.edu.co", 4, "Ingeniería de Sistemas", true));

        BULL_Professor prof1 = new BULL_Professor("DOC001", "Luis Martínez", "luis.martinez@unillanos.edu.co");
        BULL_Professor prof2 = new BULL_Professor("DOC002", "Ana Rodríguez", "ana.rodriguez@unillanos.edu.co");
        professorRepository.save(prof1);
        professorRepository.save(prof2);
        manageModuleUseCase.crearModulo(123, 2);

        BULL_Schedule h1 = new BULL_Schedule();
        h1.addTimeSlot("Lunes", "07:00-09:00");
        h1.addTimeSlot("Miércoles", "07:00-09:00");

        BULL_Schedule h2 = new BULL_Schedule();
        h2.addTimeSlot("Martes", "14:00-16:00");
        h2.addTimeSlot("Jueves", "14:00-16:00");

        BULL_Group g1 = new BULL_Group(101);
        g1.setProfessor(prof1);
        g1.setSchedule(h1);
        g1.setMaxCapacity(new BULL_MaxCapacity(30));

        BULL_Group g2 = new BULL_Group(102);
        g2.setProfessor(prof2);
        g2.setSchedule(h2);
        g2.setMaxCapacity(new BULL_MaxCapacity(25));

        groupRepository.save(g1);
        groupRepository.save(g2);

        BULL_OnSitePresencial presencial = new BULL_OnSitePresencial();
        presencial.addGroup(g1);

        BULL_SynchronousVirtualModality sincrona = new BULL_SynchronousVirtualModality();
        sincrona.addGroup(g2);

        modalityRepository.save(presencial);
        modalityRepository.save(sincrona);

        assignSpaceUseCase.asignarEspacio(101, "Edificio A", "101");
    }

    public OperationResult crearModulo(int idModule, int courseNumber) {
        return manageModuleUseCase.crearModulo(idModule, courseNumber);
    }

    public OperationResult consultarModulos() {
        return manageModuleUseCase.consultarModulos();
    }

    public OperationResult consultarModuloPorId(int idModule) {
        return manageModuleUseCase.consultarModuloPorId(idModule);
    }

    public OperationResult eliminarModulo(int idModule) {
        return manageModuleUseCase.eliminarModulo(idModule);
    }

    public List<ModuleOptionDTO> consultarModulosDisponibles(String universityCode) {
        return checkModuleUseCase.consultarModulosDisponibles(universityCode);
    }

    public OperationResult inscribirEstudiante(String universityCode, int indiceOpcion) {
        List<ModuleOptionDTO> opciones = checkModuleUseCase.consultarModulosDisponibles(universityCode);
        if (opciones.isEmpty()) {
            return OperationResult.fail("No hay módulos disponibles para el estudiante " + universityCode + ".");
        }
        if (indiceOpcion < 0 || indiceOpcion >= opciones.size()) {
            return OperationResult.fail(
                    "Opción " + (indiceOpcion + 1) + " no existe. Hay " + opciones.size() + " opción(es).");
        }
        return courseRegistrationUseCase.inscribirModulo(universityCode, opciones.get(indiceOpcion));
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

    public List<BULL_Homologation> listarHomologacionesPendientes() {
        return validateHomologationUseCase.listPending();
    }

    public OperationResult aprobarHomologacion(String universityCode,
                                               int moduleNumber,
                                               String observation) {
        return validateHomologationUseCase.approve(universityCode, moduleNumber, observation);
    }

    public OperationResult rechazarHomologacion(String universityCode, String reason) {
        return validateHomologationUseCase.reject(universityCode, reason);
    }

    public List<BULL_Student>       getEstudiantes()   { return studentRepository.findAll(); }
    public List<BULL_Professor>     getProfesores()    { return professorRepository.findAll(); }
    public List<BULL_Registration>  getInscripciones() { return registrationRepository.findAll(); }
    public List<BULL_Group>         getGrupos()        { return groupRepository.findAll(); }
}