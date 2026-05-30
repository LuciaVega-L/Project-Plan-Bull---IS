package usecases.services;

import entities.BULL_Group;
import entities.BULL_Registration;
import entities.BULL_Student;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;
import usecases.ports.BULL_StudentRepository;

import java.util.Optional;

public class CancelInscriptionUseCase {

    private final BULL_RegistrationRepository registrationRepository;
    private final BULL_StudentRepository studentRepository;
    private final BULL_GroupRepository groupRepository;

    public CancelInscriptionUseCase(BULL_RegistrationRepository registrationRepository,
                                    BULL_StudentRepository studentRepository,
                                    BULL_GroupRepository groupRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public OperationResult cancelarInscripcion(String idRegistration, String universityCode) {

        // Validaciones de entrada
        if (idRegistration == null || idRegistration.trim().isEmpty()) {
            return OperationResult.fail("El id de inscripción no puede estar vacío.");
        }
        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El código universitario no puede estar vacío.");
        }

        // Buscar la inscripción
        Optional<BULL_Registration> registrationOpt = registrationRepository.findByIdRegistration(idRegistration);
        if (!registrationOpt.isPresent()) {
            return OperationResult.fail("No se encontró la inscripción con id " + idRegistration + ".");
        }
        BULL_Registration registration = registrationOpt.get();

        // Verificar que la inscripción pertenece al estudiante
        if (!registration.getStudent().getUniversityCode().equals(universityCode)) {
            return OperationResult.fail(
                    "La inscripción " + idRegistration + " no pertenece al estudiante con código " + universityCode + "."
            );
        }

        // Verificar que la inscripción está activa
        if (!registration.estaActiva()) {
            return OperationResult.fail(
                    "La inscripción " + idRegistration + " no puede cancelarse porque su estado es: " + registration.getState() + "."
            );
        }

        // Buscar el estudiante
        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) {
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");
        }
        BULL_Student estudiante = estudianteOpt.get();

        // Buscar el grupo y liberar el cupo
        BULL_Group grupo = registration.getGroup();
        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(grupo.getIdGroup());
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail("No se encontró el grupo asociado a la inscripción.");
        }
        BULL_Group grupoEnRepo = grupoOpt.get();

        // Cancelar la inscripción en la entidad
        registration.cancelar();

        // Remover la inscripción del grupo (libera el cupo)
        OperationResult removeResult = grupoEnRepo.removeRegistration(idRegistration);
        if (!removeResult.isSuccess()) {
            return OperationResult.fail("Error al liberar el cupo del grupo: " + removeResult.getMessage());
        }

        // Persistir cambios
        registrationRepository.save(registration);
        groupRepository.save(grupoEnRepo);
        studentRepository.save(estudiante);

        return OperationResult.ok(
                "Inscripción cancelada exitosamente. " +
                        "ID: " + idRegistration + ". " +
                        "Estudiante: " + estudiante.getName() + " " + estudiante.getSurnames() + ". " +
                        "Grupo: " + grupoEnRepo.getIdGroup() + ". " +
                        "Estado: " + registration.getState() + "."
        );
    }
}