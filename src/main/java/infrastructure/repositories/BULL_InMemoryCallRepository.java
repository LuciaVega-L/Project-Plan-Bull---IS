package infrastructure.repositories;

import entities.BULL_Call;
import usecases.ports.BULL_CallRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BULL_InMemoryCallRepository implements BULL_CallRepository {

    private final Map<String, BULL_Call> storage = new HashMap<>();

    @Override
    public Optional<BULL_Call> findById(String callId) {
        return Optional.ofNullable(storage.get(callId));
    }

    @Override
    public List<BULL_Call> findOpen() {
        List<BULL_Call> open = new ArrayList<>();
        for (BULL_Call c : storage.values()) {
            if (c.isOpen()) open.add(c);
        }
        return open;
    }

    @Override
    public void save(BULL_Call call) {
        storage.put(call.getCallId(), call);
    }
}