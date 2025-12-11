package fr.cda.findboat.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private Parser() {
    }

    /**
     * Extrait et parse un nombre décimal d'une chaîne de caractères.
     * Accepte les virgules et points comme séparateurs décimaux.
     *
     * @param value la chaîne contenant le nombre
     * @return le nombre décimal extrait, ou 0.0 si aucun nombre trouvé
     */
    public static double parseDouble(String value) {
        Pattern pattern = Pattern.compile("\\d+(?:[.,]\\d+)?");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            String nombre = matcher.group().replace(',', '.');
            return Double.parseDouble(nombre);
        }
        return  0.0;
    }

    /**
     * Extrait et parse un nombre entier d'une chaîne de caractères.
     *
     * @param value la chaîne contenant le nombre
     * @return le nombre entier extrait, ou 0 si aucun nombre trouvé
     */
    public static int parseInt(String value) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }
}