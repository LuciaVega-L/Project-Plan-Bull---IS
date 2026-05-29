package usecases.services;


import entities.BULL_Grade;
import entities.BULL_Group;
import entities.BULL_Registration;
import entities.GradeType;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_RegistrationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoadNotesUsecase {
    private final BULL_RegistrationRepository registrationRepository;
    private final BULL_GroupRepository group;

    public LoadNotesUsecase(BULL_GroupRepository group, BULL_RegistrationRepository registrationRepository) {
        this.group = group;
        this.registrationRepository = registrationRepository;
    }
    public OperationResult execute(int IdGroup, String idRegistration,
                                   GradeType gradeType, double valorNota) {

        if (idRegistration == null || idRegistration.trim().isEmpty()) {
            return OperationResult.fail("El id de inscripción no puede estar vacío.");
        }
        if (gradeType == null) {
            return OperationResult.fail("El corte no puede ser nulo.");
        }
        if (valorNota < 0.0 || valorNota > 5.0) {
            return OperationResult.fail("La nota debe estar entre 0.0 y 5.0.");
        }

        Optional<BULL_Group> grupo = group.findByIdGroup(IdGroup);
        if(!grupo.isPresent()) {
            return OperationResult.fail("No se pueden cargar notas, el grupo " + IdGroup + " no existe");
        }
        BULL_Group group = grupo.get();

        List<BULL_Registration> registros = group.getRegistrations();
        if(registros.isEmpty()) {
            return OperationResult.fail("El grupo no tiene estudiante inscritos.");
        }

        Optional<BULL_Registration> registrationEstudent = registrationRepository.findByIdRegistration(idRegistration);
        if(!registrationEstudent.isPresent()) {
            return OperationResult.fail("No se encontro la inscripcion: " + idRegistration);
        }
        BULL_Registration registration = registrationEstudent.get();

        if(registration.getGroup().getIdGroup() != IdGroup) {
            return OperationResult.fail("El registro: " + idRegistration + " no pertenece al grupo " + IdGroup+ ".");
        }

        BULL_Grade grade = new BULL_Grade(gradeType, valorNota);
        OperationResult resultado = registration.addGrade(grade);
        if(resultado.isSuccess()) {
            return resultado;
        }

        registrationRepository.save(registration);
        return OperationResult.ok("Notas cargadas exitosamente.");
    }

}
