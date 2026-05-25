package main.java.usecases.ports;

import main.java.entities.BULL_Modality;
import java.util.List;
import java.util.Optional;

public interface BULL_ModalityRepository {
    Optional<BULL_Modality> findByMode(String mode);
    List<BULL_Modality> findAll();
    void save(BULL_Modality modality);
    void deleteByMode(String mode);
}
