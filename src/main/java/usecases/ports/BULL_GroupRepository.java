package usecases.ports;

import entities.BULL_Group;
import java.util.List;
import java.util.Optional;

public interface BULL_GroupRepository {
    Optional<BULL_Group> findByIdGroup(int idGroup);
    List<BULL_Group> findAll();
    void save(BULL_Group group);
    void deleteByIdGroup(int idGroup);
}
