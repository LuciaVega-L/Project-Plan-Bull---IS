package entities;

public class BULL_AsynchronousPlatformModality extends BULL_Modality {

    public BULL_AsynchronousPlatformModality() {
        super("Plataforma - Estudiante Autónomo");
    }

    @Override
    public String toString() {
        return "AsynchronousPlatformModality{groups=" + getGroups().size() + "}";
    }
}
