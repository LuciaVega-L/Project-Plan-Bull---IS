package infrastructure.repositories;
import entities.BULL_Modality;
import usecases.ports.BULL_ModalityRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryModalityRepository implements BULL_ModalityRepository {

    // Key: mode string (e.g. "Presencial", "Virtual Sincrónica", "Plataforma - Estudiante Autónomo")
    private final Map<String, BULL_Modality> storage = new HashMap<>();

    @Override
    public Optional<BULL_Modality> findByMode(String mode) {
        return Optional.ofNullable(storage.get(mode));
    }

    @Override
    public List<BULL_Modality> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Modality modality) {
        storage.put(modality.getMode(), modality);
    }

    @Override
    public void deleteByMode(String mode) {
        storage.remove(mode);
    }
}
