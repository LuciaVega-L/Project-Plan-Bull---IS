package infrastructure.repositories;

import entities.BULL_Course;
import usecases.ports.BULL_CourseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryCourseRepository implements BULL_CourseRepository {

    private final Map<Integer, BULL_Course> storage = new HashMap<>();

    @Override
    public Optional<BULL_Course> findByIdCourse(int idCourse) {
        return Optional.ofNullable(storage.get(idCourse));
    }

    @Override
    public List<BULL_Course> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Course course) {
        storage.put(course.getIdCourse(), course);
    }

    @Override
    public void deleteByIdCourse(int idCourse) {
        storage.remove(idCourse);
    }
}