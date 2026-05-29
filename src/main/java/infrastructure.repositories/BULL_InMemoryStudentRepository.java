package infrastructure.repositories;

import entities.BULL_Student;
import usecases.ports.BULL_StudentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryStudentRepository implements BULL_StudentRepository {

    private final Map<String, BULL_Student> storage = new HashMap<>();

    @Override
    public Optional<BULL_Student> findByUniversityCode(String universityCode) {
        return Optional.ofNullable(storage.get(universityCode));
    }

    @Override
    public List<BULL_Student> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Student student) {
        storage.put(student.getUniversityCode(), student);
    }

    @Override
    public void deleteByUniversityCode(String universityCode) {
        storage.remove(universityCode);
    }
}
