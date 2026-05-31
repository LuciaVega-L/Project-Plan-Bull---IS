package usecases.ports;

import entities.BULL_Homologation;
import entities.HomologationStatus;

import java.util.List;
import java.util.Optional;

public interface BULL_HomologationRepository {

    void save(BULL_Homologation homologation);

    Optional<BULL_Homologation> findByStudent(String universityCode);

    List<BULL_Homologation> findAll();

    List<BULL_Homologation> findByStatus(HomologationStatus status);

    Optional<BULL_Homologation> findApprovedByStudent(String universityCode);
}
