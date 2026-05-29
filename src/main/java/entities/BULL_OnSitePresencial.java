package entities;

public class BULL_OnSitePresencial extends BULL_Modality {

    public BULL_OnSitePresencial() {
        super();
    }

    @Override
    public String getMode() { return "Presencial"; }

    @Override
    public String toString() {
        return "OnSitePresencial{groups=" + getGroups().size() + "}";
    }
}