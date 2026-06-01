package usecases.services;

import entities.BULL_Group;
import entities.BULL_Schedule;
import usecases.dto.OperationResult;
import usecases.ports.BULL_GroupRepository;
import usecases.ports.BULL_ScheduleRepository;

import java.util.Map;
import java.util.Optional;

public class EstablishCalendarUseCase {

    private final BULL_GroupRepository    groupRepository;
    private final BULL_ScheduleRepository scheduleRepository;

    public EstablishCalendarUseCase(BULL_GroupRepository groupRepository,
                                    BULL_ScheduleRepository scheduleRepository) {
        this.groupRepository    = groupRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public OperationResult establecerCalendario(int idGroup,
                                                String scheduleId,
                                                Map<String, String> franjas) {

        // Validar entradas
        if (idGroup <= 0) {
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");
        }
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            return OperationResult.fail("El ID del calendario no puede estar vacío.");
        }
        if (franjas == null || franjas.isEmpty()) {
            return OperationResult.fail("Debe ingresar al menos una franja horaria.");
        }

        // Verificar que el grupo exista
        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGroup);
        if (!grupoOpt.isPresent()) {
            return OperationResult.fail("No se encontró el grupo con ID " + idGroup + ".");
        }
        BULL_Group grupo = grupoOpt.get();

        // Verificar que el grupo no tenga ya un horario asignado
        if (grupo.getSchedule() != null && grupo.getSchedule().tieneHorario()) {
            return OperationResult.fail(
                    "El grupo " + idGroup + " ya tiene un calendario asignado. " +
                            "Debe eliminarlo antes de asignar uno nuevo."
            );
        }

        // Validar cada franja: día no vacío y rango con formato HH:MM-HH:MM
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            String dia   = entry.getKey();
            String rango = entry.getValue();

            if (dia == null || dia.trim().isEmpty()) {
                return OperationResult.fail("El nombre del día no puede estar vacío.");
            }
            if (rango == null || !rango.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                return OperationResult.fail(
                        "El formato de la franja para '" + dia + "' es inválido. " +
                                "Use el formato HH:MM-HH:MM (ej: 07:00-09:00)."
                );
            }
        }

        // Crear el horario y agregar las franjas
        BULL_Schedule horario = new BULL_Schedule();
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            horario.addTimeSlot(entry.getKey(), entry.getValue());
        }

        // Asignar el horario al grupo y persistir
        grupo.setSchedule(horario);
        scheduleRepository.save(scheduleId, horario);
        groupRepository.save(grupo);

        // Construir mensaje de confirmación
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Calendario establecido exitosamente. ");
        mensaje.append("Grupo: ").append(idGroup).append(". ");
        mensaje.append("Franjas asignadas (").append(franjas.size()).append("): ");
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            mensaje.append(entry.getKey()).append(" ").append(entry.getValue()).append("; ");
        }

        return OperationResult.ok(mensaje.toString().trim());
    }
}