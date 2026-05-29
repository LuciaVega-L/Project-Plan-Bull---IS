package infrastructure.repositories;

import entities.BULL_Group;
import usecases.ports.BULL_GroupRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryGroupRepository implements BULL_GroupRepository {

    private final Map<Integer, BULL_Group> storage = new HashMap<>();

    @Override
    public Optional<BULL_Group> findByIdGroup(int idGroup) {
        return Optional.ofNullable(storage.get(idGroup));
    }

    @Override
    public List<BULL_Group> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Group group) {
        storage.put(group.getIdGroup(), group);
    }

    @Override
    public void deleteByIdGroup(int idGroup) {
        storage.remove(idGroup);
    }
}
