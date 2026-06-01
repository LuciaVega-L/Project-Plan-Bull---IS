package usecases.services;

import entities.BULL_Group;
import entities.BULL_Registration;
import entities.BULL_Student;
import usecases.dto.ModuleOptionDTO;
import usecases.dto.OperationResult;
import usecases.ports.*;

import java.util.Optional;

public class CourseRegistrationUseCase implements BULL_StudentRegistrationService.CourseRegistrationInputPort {

    private final BULL_GroupRepository        groupRepository;
    private final BULL_StudentRepository      studentRepository;
    private final BULL_RegistrationRepository registrationRepository;
    private final BULL_ModalityRepository     modalityRepository;

    public CourseRegistrationUseCase(BULL_GroupRepository groupRepository,
                                     BULL_StudentRepository studentRepository,
                                     BULL_RegistrationRepository registrationRepository,
                                     BULL_ModalityRepository modalityRepository, BULL_HomologationRepository homologationRepository) {
        this.groupRepository        = groupRepository;
        this.studentRepository      = studentRepository;
        this.registrationRepository = registrationRepository;
        this.modalityRepository     = modalityRepository;
    }

    // -------------------------------------------------------------------------
    // Puerto de entrada (Clean Architecture)
    // -------------------------------------------------------------------------
    @Override
    public OperationResult registrar(String universityCode, ModuleOptionDTO opcionElegida) {
        return inscribirModulo(universityCode, opcionElegida);
    }

    // -------------------------------------------------------------------------
    // Caso de uso principal: registrar la elección que el estudiante ya hizo
    // Recibe el DTO que CheckModuleUseCase le presentó — NO re-consulta nada.
    // -------------------------------------------------------------------------
    public OperationResult inscribirModulo(String universityCode, ModuleOptionDTO opcionElegida) {

        // --- 1. Validar entradas básicas ---
        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El código universitario no puede estar vacío.");
        }
        if (opcionElegida == null) {
            return OperationResult.fail("Debe seleccionar una opción de módulo.");
        }

        // --- 2. Verificar que el estudiante sigue existiendo y sin inscripción activa
        //        (pudo haber cambiado entre la consulta y el registro) ---
        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) {
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");
        }
        BULL_Student estudiante = estudianteOpt.get();//clean??

        if (estudiante.tieneInscripcionActiva()) {
            return OperationResult.fail(
                    "El estudiante " + estudiante.getName() + " " + estudiante.getSurnames() +
                            " ya tiene una inscripción activa. Debe cancelarla antes de inscribirse en otro módulo."
            );
        }

        // --- 3. Verificar que el grupo elegido sigue disponible
        //        (el cupo pudo llenarse entre consulta y registro) ---
        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(opcionElegida.getIdGrupo());
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail(
                    "El grupo " + opcionElegida.getIdGrupo() +
                            " ya no está disponible. Por favor consulte nuevamente."
            );
        }
        BULL_Group grupo = grupoOpt.get();

        if (!grupo.tieneCupoDisponible()) {
            return OperationResult.fail(
                    "El grupo " + grupo.getIdGroup() +
                            " se llenó mientras realizaba la selección. Por favor consulte nuevamente."
            );
        }

        // --- 4. Validar ubicación si la opción elegida era presencial ---
        if (opcionElegida.isEsPresencial()) {
            OperationResult validacionUbicacion = validarUbicacionPresencial(grupo);
            if (!validacionUbicacion.isSuccess()) return validacionUbicacion;
        }

        // --- 5. Crear y persistir la inscripción ---
        BULL_Registration registration;//clean????????????
        try {
            registration = new BULL_Registration(estudiante, grupo); // solo 2 params ahora
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear la inscripción: " + e.getMessage());
        }

        OperationResult addResult = grupo.addRegistration(registration);
        if (!addResult.isSuccess()) {
            return OperationResult.fail("El grupo rechazó la inscripción: " + addResult.getMessage());
        }

        estudiante.addRegistration(registration);

        // --- 6. Persistir en repositorios ---
        registrationRepository.save(registration);
        studentRepository.save(estudiante);
        groupRepository.save(grupo);

        return OperationResult.ok(
                "Inscripción exitosa. " +
                        "Estudiante: " + estudiante.getName() + " " + estudiante.getSurnames() + ". " +
                        "Módulo: " + opcionElegida.getCourseNumber() + ". " +
                        "Grupo: " + grupo.getIdGroup() + ". " +
                        "Modalidad: " + opcionElegida.getModalidad() + ". " +
                        construirMensajeUbicacion(opcionElegida) +
                        "Estado: " + registration.getState() + "."
        );
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    private OperationResult validarUbicacionPresencial(BULL_Group grupo) {
        if (grupo.getUbication() == null) {
            return OperationResult.fail(
                    "El grupo " + grupo.getIdGroup() +
                            " es Presencial pero no tiene aula asignada. Contacte al administrador."
            );
        }
        return OperationResult.ok("Ubicación válida.");
    }


    private String construirMensajeUbicacion(ModuleOptionDTO opcion) {
        if (opcion.isEsPresencial() && opcion.getUbicacion() != null) {
            return "Aula: " + opcion.getNumAula() + " en " + opcion.getUbicacion() + ". ";
        }
        return "Modalidad virtual — sin aula física. ";
    }
}