package usecases.services;

import entities.BULL_Call;
import usecases.dto.OpenCallDTO;
import usecases.dto.OperationResult;
import usecases.ports.BULL_CallRepository;

import java.util.List;


public class OpenCallForApplicationsUseCase {

    private final BULL_CallRepository                  callRepository;
    private final AnnounceCallForApplicationsUseCase   announceUseCase;

    public OpenCallForApplicationsUseCase(BULL_CallRepository callRepository,
                                          AnnounceCallForApplicationsUseCase announceUseCase) {
        this.callRepository  = callRepository;
        this.announceUseCase = announceUseCase;
    }

    public OperationResult open(OpenCallDTO dto) {

        if (dto == null)
            return OperationResult.fail("Call data cannot be null.");
        if (dto.getCallId() == null || dto.getCallId().trim().isEmpty())
            return OperationResult.fail("Call ID cannot be empty.");
        if (dto.getYear() < 2000)
            return OperationResult.fail("Year must be 2000 or later.");
        if (dto.getPeriod() < 1 || dto.getPeriod() > 2)
            return OperationResult.fail("Period must be 1 or 2.");
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty())
            return OperationResult.fail("Description cannot be empty.");

        List<BULL_Call> openCalls = callRepository.findOpen();
        for (int i = 0; i < openCalls.size(); i++) {
            BULL_Call c = openCalls.get(i);
            if (c.getYear() == dto.getYear() && c.getPeriod() == dto.getPeriod()) {
                return OperationResult.fail(
                        "A call for " + dto.getYear() + "-" + dto.getPeriod() + " is already open.");
            }
        }

        BULL_Call call;
        try {
            call = new BULL_Call(dto.getCallId(), dto.getYear(), dto.getPeriod(), dto.getDescription());
            call.open();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return OperationResult.fail("Could not open the call: " + e.getMessage());
        }

        callRepository.save(call);

        OperationResult announcement = announceUseCase.announce(call);

        if (!announcement.isSuccess()) {
            return OperationResult.ok(
                    "Call " + call.getCallId() + " opened. " +
                            "WARNING — announcement: " + announcement.getMessage());
        }

        return OperationResult.ok(
                "Call " + call.getCallId() + " opened. " + announcement.getMessage());
    }
}