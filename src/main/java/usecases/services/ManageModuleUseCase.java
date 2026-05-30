package usecases.services;

import entities.BULL_Course;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CourseRepository;

import java.util.List;
import java.util.Optional;

public class ManageModuleUseCase {

    private final BULL_CourseRepository courseRepository;

    public ManageModuleUseCase(BULL_CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public OperationResult crearModulo(int idModule, int courseNumber) {

        if (idModule <= 0) {
            return OperationResult.fail("El ID del módulo debe ser mayor a 0.");
        }
        if (courseNumber < 1 || courseNumber > 4) {
            return OperationResult.fail("El número de curso debe estar entre 1 y 4.");
        }

        Optional<BULL_Course> existente = courseRepository.findByIdModule(idModule);
        if (existente.isPresent()) {
            return OperationResult.fail(
                    "Ya existe un módulo con ID " + idModule + ". " +
                            "Use un ID diferente."
            );
        }

        BULL_Course curso;
        try {
            curso = new BULL_Course(idModule, courseNumber);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear el módulo: " + e.getMessage());
        }

        courseRepository.save(curso);

        return OperationResult.ok(
                "Módulo creado exitosamente. " +
                        "ID: " + idModule + ". " +
                        "Número de curso: " + courseNumber + "."
        );
    }

    public OperationResult consultarModulos() {

        List<BULL_Course> cursos = courseRepository.findAll();

        if (cursos.isEmpty()) {
            return OperationResult.fail("No hay módulos registrados en el sistema.");
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Módulos registrados (").append(cursos.size()).append("):\n");

        for (int i = 0; i < cursos.size(); i++) {
            BULL_Course curso = cursos.get(i);
            mensaje.append("  - ID: ").append(curso.getIdModule())
                    .append(", Curso: ").append(curso.getCourseNumber())
                    .append(", Semestres: ").append(curso.getSemesters().size())
                    .append(".\n");
        }

        return OperationResult.ok(mensaje.toString());
    }

    public OperationResult consultarModuloPorId(int idModule) {

        if (idModule <= 0) {
            return OperationResult.fail("El ID del módulo debe ser mayor a 0.");
        }

        Optional<BULL_Course> cursoOpt = courseRepository.findByIdModule(idModule);
        if (!cursoOpt.isPresent()) {
            return OperationResult.fail("No se encontró el módulo con ID " + idModule + ".");
        }

        BULL_Course curso = cursoOpt.get();

        return OperationResult.ok(
                "Módulo encontrado. " +
                        "ID: " + curso.getIdModule() + ". " +
                        "Número de curso: " + curso.getCourseNumber() + ". " +
                        "Semestres asociados: " + curso.getSemesters().size() + "."
        );
    }

    public OperationResult eliminarModulo(int idModule) {

        if (idModule <= 0) {
            return OperationResult.fail("El ID del módulo debe ser mayor a 0.");
        }

        Optional<BULL_Course> cursoOpt = courseRepository.findByIdModule(idModule);
        if (!cursoOpt.isPresent()) {
            return OperationResult.fail("No se encontró el módulo con ID " + idModule + ".");
        }

        BULL_Course curso = cursoOpt.get();

        if (!curso.getSemesters().isEmpty()) {
            return OperationResult.fail(
                    "El módulo " + idModule + " tiene " + curso.getSemesters().size() +
                            " semestre(s) asociado(s). " +
                            "Elimine los semestres antes de eliminar el módulo."
            );
        }

        courseRepository.deleteByIdModule(idModule);

        return OperationResult.ok("Módulo " + idModule + " eliminado exitosamente.");
    }
}