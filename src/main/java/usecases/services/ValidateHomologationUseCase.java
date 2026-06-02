package usecases.services;

import entities.*;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.List;
import java.util.Optional;

public class ValidateHomologationUseCase {

    private final BULL_HomologationRepository homologationRepository;
    private final BULL_StudentRepository      studentRepository;

    public ValidateHomologationUseCase(BULL_HomologationRepository homologationRepository,
                                       BULL_StudentRepository      studentRepository
                                       ) {
        this.homologationRepository = homologationRepository;
        this.studentRepository      = studentRepository;

    }

    public List<BULL_Homologation> listPending() {
        return homologationRepository.findByStatus(HomologationStatus.PENDIENTE);
    }

    public OperationResult approve(String universityCode, int moduleNumber, String observation) {

        if (universityCode == null || universityCode.trim().isEmpty())
            return OperationResult.fail("El codigo universitario no puede estar vacio.");
        if (moduleNumber <= 0)
            return OperationResult.fail("El numero de modulo homologado debe ser mayor a 0.");

        Optional<BULL_Homologation> requestOpt = homologationRepository.findByStudent(universityCode);
        if (!requestOpt.isPresent())
            return OperationResult.fail("No se encontro solicitud de homologacion para el estudiante " + universityCode + ".");

        BULL_Homologation request = requestOpt.get();

        if (!request.isPending())
            return OperationResult.fail("La solicitud del estudiante " + universityCode +
                    " ya fue evaluada. Estado actual: " + request.getStatus() + ".");

        try {
            request.approve(moduleNumber, observation);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al aprobar: " + e.getMessage());
        } catch (IllegalStateException e) {
            return OperationResult.fail("Error de estado: " + e.getMessage());
        }

        homologationRepository.save(request);

        return OperationResult.ok(
                "Homologacion APROBADA. " +
                        "Estudiante: " + request.getStudent().getName() + " " + request.getStudent().getSurnames() +
                        " (" + universityCode + "). " +
                        "Modulo homologado hasta: " + moduleNumber + ". " +
                        "Observacion: " + request.getMessage() + ". " +
                        "El estudiante ya puede inscribirse al modulo " + (moduleNumber + 1) + "."
        );
    }

    public OperationResult reject(String universityCode, String reason) {

        if (universityCode == null || universityCode.trim().isEmpty())
            return OperationResult.fail("El codigo universitario no puede estar vacio.");
        if (reason == null || reason.trim().isEmpty())
            return OperationResult.fail("Debe indicar la razon del rechazo.");

        Optional<BULL_Homologation> requestOpt = homologationRepository.findByStudent(universityCode);
        if (!requestOpt.isPresent())
            return OperationResult.fail("No se encontro solicitud de homologacion para el estudiante " + universityCode + ".");

        BULL_Homologation request = requestOpt.get();

        if (!request.isPending())
            return OperationResult.fail("La solicitud del estudiante " + universityCode +
                    " ya fue evaluada. Estado actual: " + request.getStatus() + ".");

        try {
            request.reject(reason);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al rechazar: " + e.getMessage());
        } catch (IllegalStateException e) {
            return OperationResult.fail("Error de estado: " + e.getMessage());
        }

        homologationRepository.save(request);

        return OperationResult.ok(
                "Homologacion RECHAZADA. " +
                        "Estudiante: " + request.getStudent().getName() + " " + request.getStudent().getSurnames() +
                        " (" + universityCode + "). " +
                        "Razon: " + request.getMessage() + "."
        );
    }
}