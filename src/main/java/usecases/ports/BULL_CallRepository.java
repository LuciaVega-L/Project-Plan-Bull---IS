package usecases.ports;

import entities.BULL_Call;
import java.util.List;
import java.util.Optional;


public interface BULL_CallRepository {
    Optional<BULL_Call> findById(String callId);
    List<BULL_Call>     findOpen();
    void save(BULL_Call call);
}