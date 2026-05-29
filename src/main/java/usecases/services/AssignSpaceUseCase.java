package usecases.services;

import entities.BULL_Group;
import entities.BULL_Modality;
import entities.BULL_OnSitePresencial;
import entities.BULL_Ubication;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ModalityRepository;

import java.util.Optional;

public class AssignSpaceUseCase {

    private final BULL_GroupRepository    groupRepository;
    private final BULL_ModalityRepository modalityRepository;

    public AssignSpaceUseCase(BULL_GroupRepository groupRepository,
                              BULL_ModalityRepository modalityRepository) {
        this.groupRepository    = groupRepository;
        this.modalityRepository = modalityRepository;
    }

    public OperationResult asignarEspacio(int idGroup, String building, String classroomNum) {
        
        if (idGroup <= 0) {
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");
        }
        if (building == null || building.trim().isEmpty()) {
            return OperationResult.fail("El edificio no puede estar vacío.");
        }
        if (classroomNum == null || classroomNum.trim().isEmpty()) {
            return OperationResult.fail("El número de aula no puede estar vacío.");
        }

        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGroup);
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail("No se encontró el grupo con ID " + idGroup + ".");
        }
        BULL_Group grupo = grupoOpt.get();

        BULL_Modality modalidad = obtenerModalidadDelGrupo(grupo);
        if (modalidad == null) {
            return OperationResult.fail(
                    "El grupo " + idGroup + " no tiene modalidad asignada. " +
                            "Contacte al administrador."
            );
        }

        if (!(modalidad instanceof BULL_OnSitePresencial)) {
            return OperationResult.fail(
                    "El grupo " + idGroup + " es de modalidad '" + modalidad.getMode() + "'. " +
                            "Solo los grupos presenciales pueden tener espacio asignado."
            );
        }

        if (grupo.getUbication() != null) {
            return OperationResult.fail(
                    "El grupo " + idGroup + " ya tiene asignado el aula " +
                            grupo.getUbication().getClassroomNum() + " en el edificio " +
                            grupo.getUbication().getBuilding() + ". " +
                            "Debe liberar el espacio actual antes de asignar uno nuevo."
            );
        }

        BULL_Ubication ubicacion;
        try {
            ubicacion = new BULL_Ubication(building, classroomNum);
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear la ubicación: " + e.getMessage());
        }

        OperationResult resultado = grupo.setUbication(ubicacion, modalidad);
        if (!resultado.isSuccess()) {
            return OperationResult.fail("Error al asignar el espacio: " + resultado.getMessage());
        }

        groupRepository.save(grupo);

        return OperationResult.ok(
                "Espacio asignado exitosamente. " +
                        "Grupo: " + idGroup + ". " +
                        "Edificio: " + building + ". " +
                        "Aula: " + classroomNum + "."
        );
    }

    private BULL_Modality obtenerModalidadDelGrupo(BULL_Group grupo) {
        return modalityRepository.findAll().stream()
                .filter(m -> m.getGroups().stream()
                        .anyMatch(g -> g.getIdGroup() == grupo.getIdGroup()))
                .findFirst()
                .orElse(null);
    }
}