package usecases.services;

import entities.*;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ModalityRepository;
import usecases.ports.BULL_ProfessorRepository;

import java.util.List;
import java.util.Optional;

public class ManageGroupUseCase {

    private final BULL_GroupRepository     groupRepository;
    private final BULL_ProfessorRepository professorRepository;
    private final BULL_ModalityRepository  modalityRepository;

    public ManageGroupUseCase(BULL_GroupRepository groupRepository,
                              BULL_ProfessorRepository professorRepository,
                              BULL_ModalityRepository modalityRepository) {
        this.groupRepository     = groupRepository;
        this.professorRepository = professorRepository;
        this.modalityRepository  = modalityRepository;
    }

    // "Presencial", "Virtual Sincrónica", "Virtual Asincronica"
    public OperationResult crearGrupo(int idGroup, String idTeaching, int maxCapacity,
                                      String tipoModalidad,
                                      String edificio, String aula) {

        if (idGroup <= 0)
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");
        if (maxCapacity <= 0)
            return OperationResult.fail("El cupo máximo debe ser mayor a 0.");
        if (tipoModalidad == null || tipoModalidad.trim().isEmpty())
            return OperationResult.fail("Debe indicar el tipo de modalidad.");

        if (groupRepository.findByIdGroup(idGroup).isPresent())
            return OperationResult.fail("Ya existe un grupo con ID " + idGroup + ". Use un ID diferente.");

        Optional<BULL_Professor> profesorOpt = professorRepository.findByIdTeaching(idTeaching);
        if (!profesorOpt.isPresent())
            return OperationResult.fail("No se encontró el profesor con ID docente '" + idTeaching + "'.");

        // Construir la modalidad correcta
        BULL_Modality modalidad;
        switch (tipoModalidad.trim()) {
            case "1": case "Presencial":
                modalidad = modalityRepository.findByMode("Presencial")
                        .orElse(new BULL_OnSitePresencial());
                break;
            case "2": case "Virtual Sincrónica":
                modalidad = modalityRepository.findByMode("Virtual Sincrónica")
                        .orElse(new BULL_SynchronousVirtualModality());
                break;
            case "3": case "Virtual Asincronica":
                modalidad = modalityRepository.findByMode("Virtual Asincronica")
                        .orElse(new BULL_AsynchronousVirtualModality());
                break;
            default:
                return OperationResult.fail("Modalidad inválida. Opciones: 1-Presencial, 2-Virtual Sincrónica, 3-Virtual Asincronica.");
        }

        // Validar ubicación solo si es presencial
        boolean esPresencial = modalidad instanceof BULL_OnSitePresencial;
        if (esPresencial && (edificio == null || edificio.trim().isEmpty()
                || aula == null || aula.trim().isEmpty())) {
            return OperationResult.fail("La modalidad Presencial requiere edificio y número de aula.");
        }

        // Crear el grupo
        BULL_Group grupo;
        try {
            grupo = new BULL_Group(idGroup);
            grupo.setProfessor(profesorOpt.get());
            grupo.setMaxCapacity(new BULL_MaxCapacity(maxCapacity));

            if (esPresencial) {
                BULL_Ubication ubicacion = new BULL_Ubication(edificio.trim(), aula.trim());
                grupo.setUbication(ubicacion, modalidad);
            }
        } catch (IllegalArgumentException e) {
            return OperationResult.fail("Error al crear el grupo: " + e.getMessage());
        }

        // Agregar el grupo a la modalidad y guardar ambos
        modalidad.addGroup(grupo);
        groupRepository.save(grupo);
        modalityRepository.save(modalidad);

        return OperationResult.ok(
                "Grupo creado exitosamente. " +
                        "ID: " + idGroup + ". " +
                        "Profesor: " + profesorOpt.get().getName() + ". " +
                        "Modalidad: " + modalidad.getMode() + ". " +
                        "Cupo máximo: " + maxCapacity + "." +
                        (esPresencial ? " Aula: " + aula + " - Edificio: " + edificio + "." : "")
        );
    }

    public OperationResult consultarGrupos() {
        List<BULL_Group> grupos = groupRepository.findAll();
        if (grupos.isEmpty())
            return OperationResult.fail("No hay grupos registrados en el sistema.");

        StringBuilder sb = new StringBuilder();
        sb.append("Grupos registrados (").append(grupos.size()).append("):\n");
        for (BULL_Group g : grupos) {
            String profesor  = g.getProfessor()   != null ? g.getProfessor().getName() : "Sin profesor";
            String cupos     = g.getMaxCapacity() != null ? g.getMaxCapacity().getCuposRestantes() + "/" + g.getMaxCapacity().getMaxCapacity() : "Sin cupo";
            String ubicacion = g.getUbication()   != null ? g.getUbication().getBuilding() + " - " + g.getUbication().getClassroomNum() : "Virtual";
            sb.append("  - ID: ").append(g.getIdGroup())
                    .append(", Profesor: ").append(profesor)
                    .append(", Cupos: ").append(cupos)
                    .append(", Ubicacion: ").append(ubicacion).append(".\n");
        }
        return OperationResult.ok(sb.toString());
    }

    public OperationResult eliminarGrupo(int idGroup) {
        if (idGroup <= 0)
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");

        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGroup);
        if (!grupoOpt.isPresent())
            return OperationResult.fail("No se encontró el grupo con ID " + idGroup + ".");

        if (!grupoOpt.get().getRegistrations().isEmpty())
            return OperationResult.fail("El grupo " + idGroup + " tiene inscripciones. No se puede eliminar.");

        groupRepository.deleteByIdGroup(idGroup);
        return OperationResult.ok("Grupo " + idGroup + " eliminado exitosamente.");
    }
}