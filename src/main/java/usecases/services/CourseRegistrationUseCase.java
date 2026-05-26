package main.java.usecases.services;

import main.java.entities.BULL_Group;
import main.java.entities.BULL_Modality;
import main.java.entities.BULL_Registration;
import main.java.entities.BULL_Semester;
import main.java.entities.BULL_Student;
import main.java.entities.BULL_Ubication;
import main.java.usecases.dto.OperationResult;
import main.java.usecases.ports.BULL_GroupRepository;
import main.java.usecases.ports.BULL_RegistrationRepository;
import main.java.usecases.ports.BULL_StudentRepository;

import java.util.Optional;
import java.util.UUID;

public class CourseRegistrationUseCase {

    private final BULL_GroupRepository groupRepository;
    private final BULL_StudentRepository studentRepository;
    private final BULL_RegistrationRepository registrationRepository;

    public CourseRegistrationUseCase(BULL_GroupRepository groupRepository,
                                     BULL_StudentRepository studentRepository,
                                     BULL_RegistrationRepository registrationRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    public OperationResult inscribirModulo(int idGrupo, String universityCode) {

        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El código universitario no puede estar vacío.");
        }
        if (idGrupo <= 0) {
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");
        }

        Optional<BULL_Student> estudianteOpt = studentRepository.findByUniversityCode(universityCode);
        if (!estudianteOpt.isPresent()) {
            return OperationResult.fail("No se encontró el estudiante con código " + universityCode + ".");
        }
        BULL_Student estudiante = estudianteOpt.get();

        OperationResult validacionEstudiante = validarEstudiante(estudiante);
        if (!validacionEstudiante.isSuccess()) {
            return validacionEstudiante;
        }

        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGrupo);
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail(
                    "No se encontró el grupo con id " + idGrupo + ". " +
                            "Por favor consulte nuevamente los módulos disponibles."
            );
        }
        BULL_Group grupo = grupoOpt.get();

        OperationResult validacionGrupo = validarGrupo(grupo);
        if (!validacionGrupo.isSuccess()) {
            return validacionGrupo;
        }

        OperationResult validacionUbicacion = validarUbicacionSiPresencial(grupo);
        if (!validacionUbicacion.isSuccess()) {
            return validacionUbicacion;
        }

        BULL_Semester semestre = obtenerSemestreDelGrupo(grupo);
        if (semestre == null) {
            return OperationResult.fail(
                    "El grupo " + idGrupo + " no tiene semestre asignado. " +
                            "Contacte al administrador."
            );
        }

        OperationResult vigencia = semestre.validarVigencia();
        if (!vigencia.isSuccess()) {
            return OperationResult.fail("El semestre del grupo no está vigente: " + vigencia.getMessage());
        }

        String idRegistration = generarIdRegistration(universityCode, idGrupo);

        BULL_Registration registration;
        try {
            registration = new BULL_Registration(idRegistration, estudiante, semestre, grupo);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear la inscripción: " + e.getMessage());
        }

        OperationResult validacionNuevaInscripcion = grupo.validarNuevaInscripcion(registration);
        if (!validacionNuevaInscripcion.isSuccess()) {
            return OperationResult.fail("El grupo rechazó la inscripción: " + validacionNuevaInscripcion.getMessage());
        }

        try {
            grupo.addRegistration(registration);
        } catch (IllegalStateException e) {
            return OperationResult.fail("Error al registrar la inscripción en el grupo: " + e.getMessage());
        }

        try {
            estudiante.addRegistration(registration);
        } catch (IllegalStateException e) {
            return OperationResult.fail("Error al registrar la inscripción en el estudiante: " + e.getMessage());
        }

        registrationRepository.save(registration);
        studentRepository.save(estudiante);
        groupRepository.save(grupo);

        return OperationResult.ok(
                "Inscripción exitosa. " +
                        "ID: " + idRegistration + ". " +
                        "Estudiante: " + estudiante.getName() + " " + estudiante.getSurnames() + ". " +
                        "Grupo: " + grupo.getIdGroup() + ". " +
                        "Semestre: " + semestre.getYear() + "-" + semestre.getPeriod() + ". " +
                        "Modalidad: " + grupo.getModality().getMode() + ". " +
                        construirMensajeUbicacion(grupo) +
                        "Estado: " + registration.getState() + "."
        );
    }

    private OperationResult validarEstudiante(BULL_Student estudiante) {
        if (estudiante.tieneInscripcionActiva()) {
            return OperationResult.fail(
                    "El estudiante " + estudiante.getName() + " " + estudiante.getSurnames() +
                            " ya tiene una inscripción activa. " +
                            "Debe cancelarla antes de inscribirse en otro módulo."
            );
        }
        return OperationResult.ok("Estudiante apto para inscripción.");
    }

    private OperationResult validarGrupo(BULL_Group grupo) {
        OperationResult configResult = grupo.validarConfiguracionCompleta();
        if (!configResult.isSuccess()) {
            return configResult;
        }
        OperationResult cupoResult = grupo.validarCupoDisponible();
        if (!cupoResult.isSuccess()) {
            return OperationResult.fail(
                    "El grupo " + grupo.getIdGroup() + " no tiene cupo disponible. " +
                            cupoResult.getMessage()
            );
        }
        return OperationResult.ok("Grupo válido para inscripción.");
    }

    private OperationResult validarUbicacionSiPresencial(BULL_Group grupo) {
        BULL_Modality modalidad = grupo.getModality();
        boolean esPresencial = modalidad.getMode().toLowerCase().contains("presencial");

        if (!esPresencial) {
            return OperationResult.ok("Modalidad virtual, ubicación no requerida.");
        }

        BULL_Ubication ubicacion = grupo.getUbication();
        if (ubicacion == null) {
            return OperationResult.fail(
                    "El grupo " + grupo.getIdGroup() + " es Presencial pero no tiene " +
                            "aula asignada. Contacte al administrador."
            );
        }
        return ubicacion.validarDisponibilidadAula();
    }

    private BULL_Semester obtenerSemestreDelGrupo(BULL_Group grupo) {
        BULL_Modality modalidad = grupo.getModality();
        if (modalidad == null) {
            return null;
        }
        return modalidad.getSemester();
    }

    private String generarIdRegistration(String universityCode, int idGrupo) {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "REG-" + universityCode + "-G" + idGrupo + "-" + uuid;
    }

    private String construirMensajeUbicacion(BULL_Group grupo) {
        if (grupo.getUbication() != null) {
            return "Aula: " + grupo.getUbication().getClassroomNum() +
                    " en " + grupo.getUbication().getUbication() + ". ";
        }
        return "Modalidad virtual — sin aula física asignada. ";
    }
}
