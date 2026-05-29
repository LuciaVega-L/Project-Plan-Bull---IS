package usecases.ports;

import entities.BULL_Registration;
import java.util.List;
import java.util.Optional;

public interface BULL_RegistrationRepository {
    Optional<BULL_Registration> findByIdRegistration(String idRegistration);
    List<BULL_Registration> findAll();
    void save(BULL_Registration registration);
    void deleteByIdRegistration(String idRegistration);
}
