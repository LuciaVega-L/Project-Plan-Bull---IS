package entities;

import java.util.Date;


public class BULL_Call {

    public static final String STATE_PENDING = "PENDING";
    public static final String STATE_OPEN    = "OPEN";
    public static final String STATE_CLOSED  = "CLOSED";

    private final String callId;
    private final int    year;
    private final int    period;
    private final String description;
    private String       state;
    private Date         openedAt;

    public BULL_Call(String callId, int year, int period, String description) {
        if (callId == null || callId.trim().isEmpty())
            throw new IllegalArgumentException("Call ID cannot be empty.");
        if (year < 2000)
            throw new IllegalArgumentException("Year must be 2000 or later.");
        if (period < 1 || period > 2)
            throw new IllegalArgumentException("Period must be 1 or 2.");
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty.");

        this.callId      = callId;
        this.year        = year;
        this.period      = period;
        this.description = description;
        this.state       = STATE_PENDING;
    }

    public void open() {
        if (STATE_OPEN.equals(state))
            throw new IllegalStateException("The call is already open.");
        this.state    = STATE_OPEN;
        this.openedAt = new Date();
    }

    public boolean isOpen() { return STATE_OPEN.equals(state); }

    public String getCallId()      { return callId; }
    public int    getYear()        { return year; }
    public int    getPeriod()      { return period; }
    public String getDescription() { return description; }
    public String getState()       { return state; }
    public Date   getOpenedAt()    { return openedAt; }

    @Override
    public String toString() {
        return "Call{id='" + callId + "', " + year + "-" + period + ", state='" + state + "'}";
    }
}