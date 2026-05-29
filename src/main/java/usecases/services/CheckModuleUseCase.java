package usecases.services;

import entities.*;
import usecases.dto.ModuleOptionDTO;
import usecases.ports.*;
import java.util.*;

public class CheckModuleUseCase {

    private final BULL_StudentRepository studentRepository;
    private final BULL_CourseRepository  courseRepository;

    public CheckModuleUseCase(BULL_StudentRepository studentRepository,
                              BULL_CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<ModuleOptionDTO> ejecutar(String universityCode) {
        // 1. Obtener al estudiante para determinar su nivel actual
        BULL_Student student = studentRepository.findByUniversityCode(universityCode)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // 2. Lógica de Negocio: Determinar nivel (si no tiene registros, es nivel 1)
        int nextModuleNumber = student.getRegistrations().size() + 1;

        // 3. Filtrar cursos que coincidan con ese nivel
        List<ModuleOptionDTO> opciones = new ArrayList<>();
        courseRepository.findAll().stream()
                .filter(c -> c.getCourseNumber() == nextModuleNumber)
                .forEach(curso -> {
                    for (BULL_Semester sem : curso.getSemesters()) {
                        for (BULL_Modality mod : sem.getModalities()) {
                            for (BULL_Group grupo : mod.getGroups()) {
                                if (grupo.tieneCupoDisponible()) {
                                    opciones.add(mapToDTO(curso, mod, grupo));
                                }
                            }
                        }
                    }
                });
        return opciones;
    }

    private ModuleOptionDTO mapToDTO(BULL_Course c, BULL_Modality m, BULL_Group g) {
        boolean esPresencial = m instanceof BULL_OnSitePresencial;
        return new ModuleOptionDTO(
                c.getIdModule(), c.getCourseNumber(), m.getMode(), g.getIdGroup(),
                g.getMaxCapacity() != null ? g.getMaxCapacity().getCuposRestantes() : 0,
                g.getSchedule() != null ? g.getSchedule().getHourlay().toString() : "Sin horario",
                esPresencial,
                (esPresencial && g.getUbication() != null) ? g.getUbication().getBuilding() : null,
                (esPresencial && g.getUbication() != null) ? g.getUbication().getClassroomNum() : null
        );
    }
}