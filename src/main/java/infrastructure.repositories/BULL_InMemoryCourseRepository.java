package main.java.infrastructure.repositories;

import main.java.entities.BULL_Course;
import main.java.usecases.ports.BULL_CourseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryCourseRepository implements BULL_CourseRepository {

    private final Map<Integer, BULL_Course> storage = new HashMap<>();

    @Override
    public Optional<BULL_Course> findByIdModule(int idModule) {
        return Optional.ofNullable(storage.get(idModule));
    }

    @Override
    public List<BULL_Course> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Course course) {
        storage.put(course.getIdModule(), course);
    }

    @Override
    public void deleteByIdModule(int idModule) {
        storage.remove(idModule);
    }
}
