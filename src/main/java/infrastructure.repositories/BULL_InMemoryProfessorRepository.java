package infrastructure.repositories;

import entities.BULL_Professor;
import usecases.ports.BULL_ProfessorRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryProfessorRepository implements BULL_ProfessorRepository {

    private final Map<String, BULL_Professor> storage = new HashMap<>();

    @Override
    public Optional<BULL_Professor> findByIdTeaching(String idTeaching) {
        return Optional.ofNullable(storage.get(idTeaching));
    }

    @Override
    public List<BULL_Professor> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Professor professor) {
        storage.put(professor.getIdTeaching(), professor);
    }

    @Override
    public void deleteByIdTeaching(String idTeaching) {
        storage.remove(idTeaching);
    }
}
