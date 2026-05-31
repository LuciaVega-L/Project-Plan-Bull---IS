package usecases.services;

import entities.BULL_Certificate;
import entities.BULL_Homologation;
import entities.BULL_Student;
import entities.HomologationStatus;
import usecases.dto.OperationResult;
import usecases.ports.BULL_HomologationRepository;
import usecases.ports.BULL_StudentRepository;

import java.util.Optional;

public class RequestHomologationUseCase {

    private static final String[] ALLOWED_TYPES = { "PDF", "DOCX", "TXT", "JPG", "PNG" };

    private final BULL_StudentRepository studentRepository;
    private final BULL_HomologationRepository homologationRepository;

    public RequestHomologationUseCase(BULL_StudentRepository studentRepository,
                                      BULL_HomologationRepository homologationRepository) {
        this.studentRepository = studentRepository;
        this.homologationRepository = homologationRepository;
    }

    public OperationResult execute(String universityCode, String fileName, String fileType) {

        // --- 1. Validar entradas basicas ---
        if (universityCode == null || universityCode.trim().isEmpty()) {
            return OperationResult.fail("El codigo universitario no puede estar vacio.");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            return OperationResult.fail("El nombre del archivo no puede estar vacio.");
        }
        if (fileType == null || fileType.trim().isEmpty()) {
            return OperationResult.fail("El tipo de archivo no puede estar vacio.");
        }

        Optional<BULL_Student> studentOpt = studentRepository.findByUniversityCode(universityCode);
        if (!studentOpt.isPresent()) {
            return OperationResult.fail("No se encontro el estudiante con codigo " + universityCode + ".");
        }
        BULL_Student student = studentOpt.get();

        if (student.tieneInscripcionActiva()) {
            return OperationResult.fail(
                    "El estudiante " + student.getName() + " " + student.getSurnames() +
                            " tiene una inscripcion activa. Debe cancelarla antes de solicitar una homologacion."
            );
        }

        Optional<BULL_Homologation> existing = homologationRepository.findByStudent(universityCode);
        if (existing.isPresent()) {
            if (existing.get().isPending()) {
                return OperationResult.fail(
                        "El estudiante ya tiene una solicitud de homologacion PENDIENTE."
                );
            }
            if (existing.get().isApproved()) {
                return OperationResult.fail(
                        "El estudiante ya tiene una homologacion APROBADA hasta el modulo " +
                                existing.get().getApprovedModule() + "."
                );
            }
        }

        boolean typeValid = false;
        for (int i = 0; i < ALLOWED_TYPES.length; i++) {
            if (ALLOWED_TYPES[i].equals(fileType.trim().toUpperCase())) {
                typeValid = true;
            }
        }
        if (!typeValid) {
            String listed = "";
            for (int i = 0; i < ALLOWED_TYPES.length; i++) {
                listed = listed + ALLOWED_TYPES[i];
                if (i < ALLOWED_TYPES.length - 1) {
                    listed = listed + ", ";
                }
            }
            return OperationResult.fail("Tipo de archivo no permitido. Tipos aceptados: " + listed + ".");
        }

        String simulatedPath = "uploads/" + universityCode + "/" + fileName.trim();

        BULL_Certificate certificate;
        try {
            certificate = new BULL_Certificate(fileName, fileType, simulatedPath);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error en los datos del certificado: " + e.getMessage());
        }

        BULL_Homologation request;
        try {
            request = new BULL_Homologation(student, certificate);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear la solicitud: " + e.getMessage());
        }

        homologationRepository.save(request);

        return OperationResult.ok(
                "Solicitud de homologacion enviada exitosamente. " +
                        "Estudiante: " + student.getName() + " " + student.getSurnames() + ". " +
                        "Certificado: " + certificate.getFileName() + " (" + certificate.getFileType() + "). " +
                        "Estado: " + request.getStatus() + "."
        );
    }
}