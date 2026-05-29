package entities;

public class BULL_SynchronousVirtualModality extends BULL_Modality {
    public BULL_SynchronousVirtualModality() {
        super();
    }
    @Override
    public String toString() {
        return "SynchronousVirtualModality{groups=" + getGroups().size() + "}";
    }

    @Override
    public String getMode() { return "Virtual Sincrónica"; }
}
