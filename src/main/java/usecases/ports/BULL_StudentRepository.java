package main.java.usecases.ports;

import main.java.entities.BULL_Student;
import java.util.List;
import java.util.Optional;

public interface BULL_StudentRepository {
    Optional<BULL_Student> findByUniversityCode(String universityCode);
    List<BULL_Student> findAll();
    void save(BULL_Student student);
    void deleteByUniversityCode(String universityCode);
}
