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

    public OperationResult cancelarInscripcion(String universityCode) {

        if (universityCode == null || universityCode.trim().isEmpty())
            return OperationResult.fail("El código universitario no puede estar vacío.");

        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent())
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");

        BULL_Student estudiante = estudianteOpt.get();

        // Buscar la inscripción activa del estudiante sin necesitar el ID
        BULL_Registration registration = null;
        for (BULL_Registration reg : registrationRepository.findAll()) {
            if (reg.getStudent().getUniversityCode().equals(universityCode) && reg.estaActiva()) {
                registration = reg;
                break;
            }
        }

        if (registration == null)
            return OperationResult.fail("El estudiante " + estudiante.getName() +
                    " no tiene ninguna inscripción activa para cancelar.");

        String idRegistration = registration.getIdRegistration();

        BULL_Group grupo = registration.getGroup();
        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(grupo.getIdGroup());
        if (!grupoOpt.isPresent())
            return OperationResult.fail("No se encontró el grupo asociado a la inscripción.");

        BULL_Group grupoEnRepo = grupoOpt.get();

        registration.cancelar();

        OperationResult removeResult = grupoEnRepo.removeRegistration(idRegistration);
        if (!removeResult.isSuccess())
            return OperationResult.fail("Error al liberar el cupo del grupo: " + removeResult.getMessage());

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