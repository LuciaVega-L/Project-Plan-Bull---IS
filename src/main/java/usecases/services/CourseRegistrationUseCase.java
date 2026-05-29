package usecases.services;

import entities.*;
import usecases.dto.OperationResult;
import usecases.ports.*;
import java.util.UUID;

public class CourseRegistrationUseCase {

    private final BULL_StudentRepository      studentRepository;
    private final BULL_GroupRepository        groupRepository;
    private final BULL_RegistrationRepository registrationRepository;

    public CourseRegistrationUseCase(BULL_StudentRepository studentRepository,
                                     BULL_GroupRepository groupRepository,
                                     BULL_RegistrationRepository registrationRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.registrationRepository = registrationRepository;
    }

    public OperationResult ejecutar(String universityCode, int idGrupo) {
        // 1. Recuperar Agregados
        BULL_Student estudiante = studentRepository.findByUniversityCode(universityCode)
                .orElse(null);
        BULL_Group grupo = groupRepository.findByIdGroup(idGrupo)
                .orElse(null);

        // 2. Validaciones rápidas (No repetimos la consulta de niveles, solo integridad)
        if (estudiante == null || grupo == null) {
            return OperationResult.fail("Datos de inscripción inválidos.");
        }

        if (estudiante.tieneInscripcionActiva()) {
            return OperationResult.fail("El estudiante ya posee una inscripción activa.");
        }

        // 3. Crear la inscripción (La lógica de ID vive en el Use Case)
        String idReg = "REG-" + universityCode + "-G" + idGrupo + "-" + UUID.randomUUID().toString().substring(0,4);
        BULL_Registration registration = new BULL_Registration(idReg, estudiante, grupo);

        // 4. Delegar a la Entidad (Domain Logic)
        OperationResult result = grupo.addRegistration(registration);
        if (!result.isSuccess()) return result;

        estudiante.addRegistration(registration);

        // 5. Persistir (Clean Architecture: Los repositorios se encargan del detalle)
        registrationRepository.save(registration);
        studentRepository.save(estudiante);
        groupRepository.save(grupo);

        return OperationResult.ok("Registro exitoso. ID: " + idReg);
    }
}