package fr.cda.findboat.enums;

public enum BoatType {

    VOILIER("Voilier"),
    BATEAU_A_MOTEUR("Bateau à moteur");

    private final String libelle;

    private BoatType(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public static BoatType fromLibelle(String libelle) {
        for (BoatType type : BoatType.values()) {
            if (type.getLibelle().equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de bateau inconnu : " + libelle);
    }
}
