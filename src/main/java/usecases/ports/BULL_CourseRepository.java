package usecases.ports;

import entities.BULL_Course;
import java.util.List;
import java.util.Optional;

public interface BULL_CourseRepository {
    Optional<BULL_Course> findByIdCourse(int idCourse);
    List<BULL_Course> findAll();
    void save(BULL_Course course);
    void deleteByIdCourse(int idCourse);
}