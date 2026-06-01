package infrastructure.repositories;

import entities.BULL_Homologation;
import entities.HomologationStatus;
import usecases.ports.BULL_HomologationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BULL_InMemoryHomologationRepository implements BULL_HomologationRepository {

    private final List<BULL_Homologation> storage = new ArrayList<>();

    @Override
    public void save(BULL_Homologation homologation) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getStudent().getUniversityCode()
                    .equals(homologation.getStudent().getUniversityCode())) {
                storage.set(i, homologation);
                return;
            }
        }
        storage.add(homologation);
    }

    @Override
    public Optional<BULL_Homologation> findByStudent(String universityCode) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getStudent().getUniversityCode().equals(universityCode)) {
                return Optional.of(storage.get(i));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<BULL_Homologation> findAll() {
        List<BULL_Homologation> result = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            result.add(storage.get(i));
        }
        return result;
    }

    @Override
    public List<BULL_Homologation> findByStatus(HomologationStatus status) {
        List<BULL_Homologation> result = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getStatus() == status) {
                result.add(storage.get(i));
            }
        }
        return result;
    }

    @Override
    public Optional<BULL_Homologation> findApprovedByStudent(String universityCode) {
        for (int i = 0; i < storage.size(); i++) {
            BULL_Homologation homologation = storage.get(i);
            if (homologation.getStudent().getUniversityCode().equals(universityCode)
                    && homologation.isApproved()) {
                return Optional.of(homologation);
            }
        }
        return Optional.empty();
    }
}
