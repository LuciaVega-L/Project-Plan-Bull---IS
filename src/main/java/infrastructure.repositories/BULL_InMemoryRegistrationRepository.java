package main.java.infrastructure.repositories;

import main.java.entities.BULL_Registration;
import main.java.usecases.ports.BULL_RegistrationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryRegistrationRepository implements BULL_RegistrationRepository {

    private final Map<String, BULL_Registration> storage = new HashMap<>();

    @Override
    public Optional<BULL_Registration> findByIdRegistration(String idRegistration) {
        return Optional.ofNullable(storage.get(idRegistration));
    }

    @Override
    public List<BULL_Registration> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(BULL_Registration registration) {
        storage.put(registration.getIdRegistration(), registration);
    }

    @Override
    public void deleteByIdRegistration(String idRegistration) {
        storage.remove(idRegistration);
    }
}
