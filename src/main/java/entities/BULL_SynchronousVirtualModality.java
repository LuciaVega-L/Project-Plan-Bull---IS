package entities;

public class BULL_SynchronousVirtualModality extends BULL_Modality {

    public BULL_SynchronousVirtualModality() {
        super("Virtual Sincrónica");
    }

    @Override
    public String toString() {
        return "SynchronousVirtualModality{groups=" + getGroups().size() + "}";
    }
}
