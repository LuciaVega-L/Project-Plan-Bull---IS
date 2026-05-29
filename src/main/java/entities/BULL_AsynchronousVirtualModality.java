package entities;

public class BULL_AsynchronousVirtualModality extends BULL_Modality {
    public BULL_AsynchronousVirtualModality() {
        super();
    }
    @Override
    public String toString() {
        return "AsynchronousVirtualModality{groups=" + getGroups().size() + "}";
    }

    @Override
    public String getMode() { return "Virtual Asincronica"; }
}
