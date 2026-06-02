package usecases.services;

import entities.*;
import usecases.dto.CourseOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckCourseUseCase implements BULL_StudentRegistrationService.CheckCourseInputPort {

    private final BULL_StudentRepository  studentRepository;
    private final BULL_CourseRepository   courseRepository;
    private final BULL_ModalityRepository modalityRepository;
    private final BULL_GroupRepository    groupRepository;

    private final BULL_HomologationRepository homologationRepository;

    public CheckCourseUseCase(BULL_StudentRepository studentRepository,
                              BULL_CourseRepository courseRepository,
                              BULL_ModalityRepository modalityRepository,
                              BULL_GroupRepository groupRepository,
                              BULL_HomologationRepository homologationRepository) {
        this.studentRepository      = studentRepository;
        this.courseRepository       = courseRepository;
        this.modalityRepository     = modalityRepository;
        this.groupRepository        = groupRepository;
        this.homologationRepository = homologationRepository;
    }

    @Override
    public List<CourseOptionDTO> consultarPorEstudiante(String universityCode) {
        return consultarCoursesDisponibles(universityCode);
    }

    public List<CourseOptionDTO> consultarCoursesDisponibles(String universityCode) {

        List<CourseOptionDTO> opciones = new ArrayList<>();

        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) return opciones;

        BULL_Student estudiante = estudianteOpt.get();

        if (!estudiante.puedeInscribirCourses()) {System.out.println("El estudiante no cumple los requisitos."); return opciones;}

        if (estudiante.tieneInscripcionActiva()) return opciones;

        int maxAprobado = 0;

// Primero: homologación aprobada tiene prioridad
        Optional<BULL_Homologation> homologacion =
                homologationRepository.findApprovedByStudent(universityCode);
        if (homologacion.isPresent()) {
            maxAprobado = homologacion.get().getApprovedModule();
        }

// Luego: inscripciones finalizadas (puede haber avanzado más allá de la homologación)
        for (BULL_Registration reg : estudiante.getRegistrations()) {
            if (BULL_Registration.STATE_FINALIZADA.equals(reg.getState())) {
                int courseNumberAprobado = buscarCourseNumberDeGrupo(reg.getGroup());
                if (courseNumberAprobado > maxAprobado) {
                    maxAprobado = courseNumberAprobado;
                }
            }
        }

        int courseNumberRequerido = maxAprobado + 1;

        BULL_Course courseDelEstudiante = buscarCursoPorCourseNumber(courseNumberRequerido);
        if (courseDelEstudiante == null) return opciones;

        List<BULL_Group>    todosGrupos = groupRepository.findAll();
        List<BULL_Modality> modalidades = modalityRepository.findAll();

        for (BULL_Group grupo : todosGrupos) {

            if (!tieneCupoYConfiguracion(grupo)) continue;

            BULL_Modality modalidad = buscarModalidadDeGrupo(modalidades, grupo);
            if (modalidad == null) continue;

            boolean esPresencial = modalidad instanceof BULL_OnSitePresencial;
            String  ubicacion    = null;
            String  numAula      = null;

            if (esPresencial && grupo.getUbication() != null) {
                ubicacion = grupo.getUbication().getBuilding();
                numAula   = grupo.getUbication().getClassroomNum();
            }

            opciones.add(new CourseOptionDTO(
                    courseDelEstudiante.getIdCourse(),
                    courseDelEstudiante.getCourseNumber(),
                    modalidad.getMode(),
                    grupo.getIdGroup(),
                    grupo.getMaxCapacity().getCuposRestantes(),
                    grupo.getSchedule().getHourlay().toString(),
                    esPresencial,
                    ubicacion,
                    numAula
            ));
        }

        return opciones;
    }

    public OperationResult verificarDisponibilidad(String universityCode) {

        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent())
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");

        BULL_Student estudiante = estudianteOpt.get();

        if (!estudiante.puedeInscribirCourses())
            return OperationResult.fail("El estudiante debe estar en semestre 4 o superior para inscribir courses.");

        if (estudiante.tieneInscripcionActiva())
            return OperationResult.fail(
                    "El estudiante " + estudiante.getName() + " " + estudiante.getSurnames() +
                            " ya tiene una inscripción activa.");

        List<CourseOptionDTO> opciones = consultarCoursesDisponibles(universityCode);
        if (opciones.isEmpty())
            return OperationResult.fail("No hay courses disponibles para el estudiante " + universityCode + ".");

        return OperationResult.ok(
                "Hay " + opciones.size() + " opción(es) disponibles para " +
                        estudiante.getName() + " " + estudiante.getSurnames() + ".");
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private int buscarCourseNumberDeGrupo(BULL_Group grupo) {
        for (BULL_Course course : courseRepository.findAll()) {
            for (BULL_Semester sem : course.getSemesters()) {
                for (BULL_Modality modalidad : sem.getModalities()) {
                    for (BULL_Group g : modalidad.getGroups()) {
                        if (g.getIdGroup() == grupo.getIdGroup()) {
                            return course.getCourseNumber();
                        }
                    }
                }
            }
        }
        return 0;
    }

    private BULL_Course buscarCursoPorCourseNumber(int courseNumber) {
        for (BULL_Course curso : courseRepository.findAll()) {
            if (curso.getCourseNumber() == courseNumber) return curso;
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
                if (g.getIdGroup() == grupo.getIdGroup()) return modalidad;
            }
        }
        return null;
    }
}