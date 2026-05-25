package main.java.usecases.ports;

import main.java.entities.BULL_Course;
import java.util.List;
import java.util.Optional;

public interface BULL_CourseRepository {
    Optional<BULL_Course> findByIdModule(int idModule);
    List<BULL_Course> findAll();
    void save(BULL_Course course);
    void deleteByIdModule(int idModule);
}
