package fr.cda.findboat.helper;

public class CapacityParser {

    public static int parseCapacity(String capacityText) {
        if (capacityText == null || capacityText.trim().isEmpty()) {
            return 0;
        }

        try {
            // Remplacer les séparateurs par des espaces AVANT de supprimer
            String normalized = capacityText.replace("/", " ")
                    .replace("+", " ");

            // Supprimer tout sauf les chiffres et espaces
            String cleaned = normalized.replaceAll("[^0-9\\s]", "").trim();

            // Prendre le premier nombre
            String firstNumber = cleaned.split("\\s+")[0];

            return Integer.parseInt(firstNumber);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Impossible de parser la capacité : " + capacityText);
            return 0;
        }
    }
}
