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

        if (idGroup <= 0)
            return OperationResult.fail("El ID del grupo debe ser mayor a 0.");
        if (scheduleId == null || scheduleId.trim().isEmpty())
            return OperationResult.fail("El ID del calendario no puede estar vacío.");
        if (franjas == null || franjas.isEmpty())
            return OperationResult.fail("Debe ingresar al menos una franja horaria.");

        Optional<BULL_Group> grupoOpt = groupRepository.findByIdGroup(idGroup);
        if (!grupoOpt.isPresent())
            return OperationResult.fail("No se encontró el grupo con ID " + idGroup + ".");
        BULL_Group grupo = grupoOpt.get();

        if (grupo.getSchedule() != null && grupo.getSchedule().tieneHorario())
            return OperationResult.fail("El grupo " + idGroup + " ya tiene un calendario asignado. " +
                    "Debe eliminarlo antes de asignar uno nuevo.");

        // Validar formato y que inicio < fin
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            String dia   = entry.getKey();
            String rango = entry.getValue();

            if (dia == null || dia.trim().isEmpty())
                return OperationResult.fail("El nombre del día no puede estar vacío.");
            if (rango == null || !rango.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}"))
                return OperationResult.fail("El formato de la franja para '" + dia +
                        "' es inválido. Use HH:MM-HH:MM (ej: 07:00-09:00).");
            if (aMinutos(rango.split("-")[0]) >= aMinutos(rango.split("-")[1]))
                return OperationResult.fail("En el día '" + dia +
                        "' la hora de inicio debe ser menor a la hora de fin.");
        }

        // Validar que no se cruce con horarios de otros grupos
        for (BULL_Group otroGrupo : groupRepository.findAll()) {
            if (otroGrupo.getIdGroup() == idGroup) continue;
            if (otroGrupo.getSchedule() == null || !otroGrupo.getSchedule().tieneHorario()) continue;

            Map<String, String> otroHorario = otroGrupo.getSchedule().getHourlay();

            for (Map.Entry<String, String> nueva : franjas.entrySet()) {
                String dia = nueva.getKey();
                if (!otroHorario.containsKey(dia)) continue;

                if (seCruzan(nueva.getValue(), otroHorario.get(dia)))
                    return OperationResult.fail("La franja del " + dia + " " + nueva.getValue() +
                            " se cruza con el grupo " + otroGrupo.getIdGroup() +
                            " que tiene " + dia + " " + otroHorario.get(dia) + ".");
            }
        }

        // Crear y asignar el horario
        BULL_Schedule horario = new BULL_Schedule();
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            horario.addTimeSlot(entry.getKey(), entry.getValue());
        }

        grupo.setSchedule(horario);
        scheduleRepository.save(scheduleId, horario);
        groupRepository.save(grupo);

        StringBuilder mensaje = new StringBuilder("Calendario establecido exitosamente. ");
        mensaje.append("Grupo: ").append(idGroup).append(". ");
        mensaje.append("Franjas asignadas (").append(franjas.size()).append("): ");
        for (Map.Entry<String, String> entry : franjas.entrySet()) {
            mensaje.append(entry.getKey()).append(" ").append(entry.getValue()).append("; ");
        }
        return OperationResult.ok(mensaje.toString().trim());
    }

    // "07:00" → 420 (minutos desde medianoche)
    private int aMinutos(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]) * 60 + Integer.parseInt(partes[1]);
    }

    // true si los dos rangos se solapan
    private boolean seCruzan(String rangoA, String rangoB) {
        int inicioA = aMinutos(rangoA.split("-")[0]);
        int finA    = aMinutos(rangoA.split("-")[1]);
        int inicioB = aMinutos(rangoB.split("-")[0]);
        int finB    = aMinutos(rangoB.split("-")[1]);
        return inicioA < finB && inicioB < finA;
    }
}