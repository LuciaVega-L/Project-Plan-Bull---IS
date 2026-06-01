package usecases.dto;

/**
 * DTO carrying the input data needed to open a call for applications.
 * Follows the same pattern as ModuleOptionDTO in this project.
 */
public class OpenCallDTO {

    private final String callId;
    private final int    year;
    private final int    period;
    private final String description;

    public OpenCallDTO(String callId, int year, int period, String description) {
        this.callId      = callId;
        this.year        = year;
        this.period      = period;
        this.description = description;
    }

    public String getCallId()      { return callId; }
    public int    getYear()        { return year; }
    public int    getPeriod()      { return period; }
    public String getDescription() { return description; }
}