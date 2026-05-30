package usecases.services;

import entities.BULL_Professor;
import usecases.dto.OperationResult;
import usecases.ports.BULL_ProfessorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoadProfessorListUseCase {

    private final BULL_ProfessorRepository professorRepository;

    public LoadProfessorListUseCase(BULL_ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public OperationResult cargarListaProfesores(List<BULL_Professor> profesores) {

        if (profesores == null || profesores.isEmpty()) {
            return OperationResult.fail("La lista de profesores no puede estar vacía.");
        }

        List<String> cargados   = new ArrayList<>();
        List<String> duplicados = new ArrayList<>();
        List<String> errores    = new ArrayList<>();

        for (int i = 0; i < profesores.size(); i++) {
            BULL_Professor profesor = profesores.get(i);

            if (profesor == null) {
                errores.add("Posición " + (i + 1) + ": el profesor es nulo.");
                continue;
            }

            if (profesor.getIdTeaching() == null || profesor.getIdTeaching().trim().isEmpty()) {
                errores.add("Posición " + (i + 1) + ": el ID docente no puede estar vacío.");
                continue;
            }

            if (profesor.getName() == null || profesor.getName().trim().isEmpty()) {
                errores.add("ID " + profesor.getIdTeaching() + ": el nombre no puede estar vacío.");
                continue;
            }

            if (profesor.getMail() == null || !profesor.getMail().contains("@")) {
                errores.add("ID " + profesor.getIdTeaching() + ": el correo no tiene un formato válido.");
                continue;
            }

            Optional<BULL_Professor> existente = professorRepository.findByIdTeaching(profesor.getIdTeaching());
            if (existente.isPresent()) {
                duplicados.add(profesor.getIdTeaching());
                continue;
            }

            try {
                professorRepository.save(profesor);
                cargados.add(profesor.getIdTeaching());
            } catch (Exception e) {
                errores.add("ID " + profesor.getIdTeaching() + ": error al guardar — " + e.getMessage());
            }
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Cargue finalizado. ");
        mensaje.append("Registrados: ").append(cargados.size()).append(". ");

        if (!duplicados.isEmpty()) {
            mensaje.append("Duplicados omitidos (").append(duplicados.size()).append("): ")
                    .append(String.join(", ", duplicados)).append(". ");
        }

        if (!errores.isEmpty()) {
            mensaje.append("Errores (").append(errores.size()).append("): ")
                    .append(String.join(" | ", errores)).append(". ");
        }

        if (cargados.isEmpty() && !errores.isEmpty()) {
            return OperationResult.fail(mensaje.toString());
        }

        return OperationResult.ok(mensaje.toString());
    }
}