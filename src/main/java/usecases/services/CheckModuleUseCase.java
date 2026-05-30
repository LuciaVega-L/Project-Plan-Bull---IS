package usecases.services;

import entities.BULL_Course;
import entities.BULL_Group;
import entities.BULL_Modality;
import entities.BULL_OnSitePresencial;
import entities.BULL_Student;
import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CourseRepository;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ModalityRepository;
import usecases.ports.BULL_StudentRegistrationService;
import usecases.ports.BULL_StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckModuleUseCase implements BULL_StudentRegistrationService.CheckModuleInputPort {

    private final BULL_StudentRepository  studentRepository;
    private final BULL_CourseRepository   courseRepository;
    private final BULL_ModalityRepository modalityRepository;
    private final BULL_GroupRepository    groupRepository;

    public CheckModuleUseCase(BULL_StudentRepository studentRepository,
                              BULL_CourseRepository courseRepository,
                              BULL_ModalityRepository modalityRepository,
                              BULL_GroupRepository groupRepository) {
        this.studentRepository  = studentRepository;
        this.courseRepository   = courseRepository;
        this.modalityRepository = modalityRepository;
        this.groupRepository    = groupRepository;
    }

    // -------------------------------------------------------------------------
    // Puerto de entrada (Clean Architecture)
    // -------------------------------------------------------------------------
    @Override
    public List<ModuleOptionDTO> consultarPorEstudiante(String universityCode) {
        return consultarModulosDisponibles(universityCode);
    }

    // -------------------------------------------------------------------------
    // Caso de uso principal: consultar módulos disponibles para un estudiante
    // Filtra por nivel del estudiante (solo ve el siguiente módulo que le toca)
    // Retorna las opciones de grupo para que el estudiante elija
    // -------------------------------------------------------------------------
    public List<ModuleOptionDTO> consultarModulosDisponibles(String universityCode) {

        List<ModuleOptionDTO> opciones = new ArrayList<>();

        // 1. Buscar estudiante
        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) {
            return opciones;
        }
        BULL_Student estudiante = estudianteOpt.get();

        // 2. Si ya tiene inscripción activa no se le muestran opciones
        if (estudiante.tieneInscripcionActiva()) {
            return opciones;
        }

        int courseNumberRequerido = determinarCourseNumber(estudiante.getSemester());

        // 3. Buscar el curso que corresponde al nivel del estudiante
        BULL_Course cursoDelEstudiante = buscarCursoPorCourseNumber(courseNumberRequerido);
        if (cursoDelEstudiante == null) {
            return opciones;
        }

        // 4. Recopilar todos los grupos con cupo de todas las modalidades
        List<BULL_Group>    todosGrupos = groupRepository.findAll();
        List<BULL_Modality> modalidades = modalityRepository.findAll();

        for (BULL_Group grupo : todosGrupos) {

            if (!tieneCupoYConfiguracion(grupo)) {
                continue;
            }

            BULL_Modality modalidad = buscarModalidadDeGrupo(modalidades, grupo);
            if (modalidad == null) {
                continue;
            }

            boolean esPresencial = modalidad instanceof BULL_OnSitePresencial;
            String  ubicacion    = null;
            String  numAula      = null;

            if (esPresencial && grupo.getUbication() != null) {
                ubicacion = grupo.getUbication().getBuilding();
                numAula   = grupo.getUbication().getClassroomNum();
            }

            ModuleOptionDTO opcion = new ModuleOptionDTO(
                    cursoDelEstudiante.getIdModule(),
                    cursoDelEstudiante.getCourseNumber(),
                    modalidad.getMode(),
                    grupo.getIdGroup(),
                    grupo.getMaxCapacity().getCuposRestantes(),
                    grupo.getSchedule().getHourlay().toString(),
                    esPresencial,
                    ubicacion,
                    numAula
            );

            opciones.add(opcion);
        }

        return opciones;
    }

    // -------------------------------------------------------------------------
    // Verificación rápida: ¿hay algo disponible? (para mensajes de error)
    // -------------------------------------------------------------------------
    public OperationResult verificarDisponibilidad(String universityCode) {

        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) {
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");
        }

        BULL_Student estudiante = estudianteOpt.get();

        if (estudiante.tieneInscripcionActiva()) {
            return OperationResult.fail(
                    "El estudiante " + estudiante.getName() + " " + estudiante.getSurnames() +
                            " ya tiene una inscripción activa."
            );
        }

        List<ModuleOptionDTO> opciones = consultarModulosDisponibles(universityCode);
        if (opciones.isEmpty()) {
            return OperationResult.fail("No hay módulos disponibles para el estudiante " + universityCode + ".");
        }

        return OperationResult.ok(
                "Hay " + opciones.size() + " opción(es) disponibles para " +
                        estudiante.getName() + " " + estudiante.getSurnames() + "."
        );
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    private int determinarCourseNumber(int semestreEstudiante) {
        if (semestreEstudiante <= 2) return 1;
        if (semestreEstudiante <= 4) return 2;
        if (semestreEstudiante <= 6) return 3;
        return 4;
    }

    private BULL_Course buscarCursoPorCourseNumber(int courseNumber) {
        List<BULL_Course> cursos = courseRepository.findAll();
        for (BULL_Course curso : cursos) {
            if (curso.getCourseNumber() == courseNumber) {
                return curso;
            }
        }
        return null;
    }

    private boolean tieneCupoYConfiguracion(BULL_Group grupo) {
        return grupo.tieneCupoDisponible()
                && grupo.getSchedule()    != null
                && grupo.getMaxCapacity() != null
                && grupo.getProfessor()   != null;
    }

    private BULL_Modality buscarModalidadDeGrupo(List<BULL_Modality> modalidades, BULL_Group grupo) {
        for (BULL_Modality modalidad : modalidades) {
            for (BULL_Group g : modalidad.getGroups()) {
                if (g.getIdGroup() == grupo.getIdGroup()) {
                    return modalidad;
                }
            }
        }
        return null;
    }
}