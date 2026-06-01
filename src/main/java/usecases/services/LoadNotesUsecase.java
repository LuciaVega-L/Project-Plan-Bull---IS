package usecases.services;

import entities.BULL_Grade;
import entities.BULL_Group;
import entities.BULL_Registration;
import entities.BULL_Student;
import entities.GradeType;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;
import usecases.ports.BULL_StudentRepository;

import java.util.Optional;

public class LoadNotesUsecase {

    private final BULL_RegistrationRepository registrationRepository;
    private final BULL_GroupRepository groupRepository;
    private final BULL_StudentRepository studentRepository;

    public LoadNotesUsecase(BULL_GroupRepository group,
                            BULL_RegistrationRepository registrationRepository,
                            BULL_StudentRepository student) {
        this.groupRepository = group;
        this.registrationRepository = registrationRepository;
        this.studentRepository = student;
    }

    public OperationResult execute(int idGroup, String universityCode,
                                   GradeType gradeType, double valorNota) {

        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El código universitario no puede estar vacío.");
        }
        if (gradeType == null) {
            return OperationResult.fail("El corte no puede ser nulo.");
        }
        if (valorNota < 0.0 || valorNota > 5.0) {
            return OperationResult.fail("La nota debe estar entre 0.0 y 5.0.");
        }

        BULL_Group grupo = groupRepository.findByIdGroup(idGroup).orElse(null);
        if (grupo == null) {
            return OperationResult.fail("No existe un grupo con ID " + idGroup + ".");
        }

        BULL_Student estudiante = studentRepository.findByUniversityCode(universityCode).orElse(null);
        if (estudiante == null) {
            return OperationResult.fail("No existe un estudiante con código " + universityCode + ".");
        }

        Optional<BULL_Registration> inscripcionOpt = registrationRepository.findAll()
                .stream()
                .filter(r -> r.getStudent().getUniversityCode().equals(universityCode)
                        && r.getGroup().getIdGroup() == idGroup
                        && r.estaActiva())
                .findFirst();

        if (inscripcionOpt.isEmpty()) {
            return OperationResult.fail(
                    "El estudiante " + universityCode + " no tiene una inscripción activa en el grupo " + idGroup + ".");
        }

        BULL_Registration inscripcion = inscripcionOpt.get();

        boolean yaExiste = inscripcion.getGrades().stream()
                .anyMatch(g -> g.getType() == gradeType);

        if (yaExiste) {
            return OperationResult.fail(
                    "Ya existe una nota para " + gradeType.name() +
                            " del estudiante " + universityCode + ". Use la opción de actualizar nota.");
        }

        try {
            BULL_Grade nuevaNota = new BULL_Grade(gradeType, valorNota);
            inscripcion.addGrade(nuevaNota);
            registrationRepository.save(inscripcion);
        } catch (Exception e) {
            return OperationResult.fail("Error al guardar la nota: " + e.getMessage());
        }

        return OperationResult.ok(
                "Nota de " + gradeType.name() + " (" + gradeType.getPorcentaje() + "%) " +
                        "asignada correctamente: " + valorNota +
                        " → Estudiante " + universityCode + " en grupo " + idGroup + ".");
    }
}