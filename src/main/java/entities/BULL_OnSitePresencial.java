package entities;

public class BULL_OnSitePresencial extends BULL_Modality {

    private BULL_Ubication ubication;

    public BULL_OnSitePresencial(BULL_Ubication ubication) {
        super("Presencial");
        if (ubication == null) {
            throw new IllegalArgumentException("La ubicación para modalidad presencial no puede ser nula.");
        }
        this.ubication = ubication;
    }

    public BULL_Ubication getUbication() { return ubication; }

    @Override
    public String toString() {
        return "OnSitePresencial{ubication=" + ubication + ", groups=" + getGroups().size() + "}";
    }
}
