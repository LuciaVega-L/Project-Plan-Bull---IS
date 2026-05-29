package usecases.services;

import entities.BULL_Grade;
import entities.BULL_GradeType;   // el enum
import entities.BULL_Group;
import entities.BULL_Registration;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;

import java.util.List;
import java.util.Optional;

public class CargarNotasUseCase {

    private final BULL_GroupRepository        groupRepository;
    private final BULL_RegistrationRepository registrationRepository;

    public CargarNotasUseCase(BULL_GroupRepository groupRepository,
                              BULL_RegistrationRepository registrationRepository) {
        this.groupRepository        = groupRepository;
        this.registrationRepository = registrationRepository;
    }

    public OperationResult cargarNotas(int idGrupo, String idRegistration,
                                       GradeType tipoNota, double valorNota) {

        // --- Validaciones de entrada (las básicas; tú agregarás las demás) ---
        if (idRegistration == null || idRegistration.trim().isEmpty()) {
            return OperationResult.fail("El id de inscripción no puede estar vacío.");
        }
        if (tipoNota == null) {
            return OperationResult.fail("El tipo de nota (CargarNotas.Null) no puede ser nulo.");
        }
        if (valorNota < 0.0 || valorNota > 5.0) {
            return OperationResult.fail("La nota debe estar entre 0.0 y 5.0.");
        }

        // --- Verificar que el grupo existe ---
        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGrupo);
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail("No existen notas para cargar: grupo " + idGrupo + " no encontrado.");
        }
        BULL_Group grupo = grupoOpt.get();

        // --- Verificar que las inscripciones del grupo no están vacías ---
        List<BULL_Registration> inscripciones = grupo.getRegistrations();
        if (inscripciones == null || inscripciones.isEmpty()) {
            return OperationResult.fail("No existen notas para cargar: el grupo no tiene estudiantes inscritos.");
        }

        // --- Buscar la inscripción específica ---
        Optional<BULL_Registration> regOpt = registrationRepository.findByIdRegistration(idRegistration);
        if (!regOpt.isPresent()) {
            return OperationResult.fail("No se encontró la inscripción " + idRegistration + ".");
        }
        BULL_Registration registration = regOpt.get();

        // --- Verificar que la inscripción pertenece a este grupo ---
        if (registration.getGroup().getIdGroup() != idGrupo) {
            return OperationResult.fail("La inscripción no pertenece al grupo " + idGrupo + ".");
        }

        // --- Cargar la nota ---
        BULL_Grade grade = new BULL_Grade(tipoNota, valorNota);
        OperationResult resultado = registration.addGrade(grade);

        if (!resultado.isSuccess()) {
            return resultado;
        }

        // Persistir el cambio (la registration ya fue modificada en memoria,
        // pero guardamos para consistencia con el patrón del proyecto)
        registrationRepository.save(registration);

        return OperationResult.ok("Notas cargadas exitosamente.");
    }
}