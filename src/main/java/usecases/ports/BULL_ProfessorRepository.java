package main.java.usecases.ports;

import main.java.entities.BULL_Professor;
import java.util.List;
import java.util.Optional;

public interface BULL_ProfessorRepository {
    Optional<BULL_Professor> findByIdTeaching(String idTeaching);
    List<BULL_Professor> findAll();
    void save(BULL_Professor professor);
    void deleteByIdTeaching(String idTeaching);
}
