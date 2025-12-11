package fr.cda.findboat.scraper;

import fr.cda.findboat.enums.BoatType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtlantiqueLocationScraper {

    private static final String baseUrl = "https://www.atlantique-location.fr";

    private AtlantiqueLocationScraper() {
    }

    /**
     * Configure les paramètres de scraping selon le type de bateau.
     *
     * @param boatType le type de bateau recherché
     * @return le document HTML de la page correspondante
     */
    public static Document setParameters(BoatType boatType) {

        String url = "";
        if (boatType == BoatType.VOILIER) {
            url = "https://www.atlantique-location.fr/location-bateau-morbihan.html";
        } else if (boatType == BoatType.BATEAU_A_MOTEUR) {
            url = "https://www.atlantique-location.fr/location-bateau-moteur-morbihan";
        }
        try {
            return Jsoup.connect(url).get();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extrait les liens des bateaux depuis le document HTML.
     *
     * @param document le document HTML à parser
     * @return la liste des URLs des bateaux
     */
    public static List<String> getLinks(Document document) {

        int maxLinks = 50;
        List<String> links = new ArrayList<>();

        Element boatTable = document.select("table.views-table").first();

        Elements linkList = boatTable.select("tbody tr");

        for (Element link : linkList) {
            if (links.size() >= maxLinks) break;
            String url = extractUrl(link);
            links.add(baseUrl + url);
        }
        return links;
    }

    /**
     * Récupère les données d'un bateau depuis son URL.
     *
     * @param type le type de bateau
     * @param link l'URL de la page du bateau
     * @return un HashMap contenant toutes les données du bateau
     */
    public static HashMap<String, String> getBoatData(BoatType type, String link) {
        HashMap<String, String> boatData = new HashMap<>();
        String description = "";
        String capacity = "";
        String price = "";

        try {
            Document boatPage = Jsoup.connect(link).get();

            String title = boatPage.select("h1 span").text();

            Element caracteristicTable = boatPage.select("table.caracteristiques").first();

            if (type == BoatType.VOILIER) {
                Element commentBlock = caracteristicTable.select("tr.contenu").get(2);
                description = commentBlock.select("td").text();
            }

            Element caractTable = boatPage.select("table.caracteristiques_generales").first();

            String length = caractTable.select("td").get(1).text();

            Element yearLine = caractTable.select("tr").get(type == BoatType.BATEAU_A_MOTEUR ? 1 : 2);
            String year = yearLine.select("td").get(1).text();

            if (type == BoatType.BATEAU_A_MOTEUR) {
                Element capacityLine = caractTable.select("tr").get(2);
                capacity = capacityLine.select("td").get(3).text();
            } else if (type == BoatType.VOILIER) {
                Element interiorBlock = caracteristicTable.select("tr.contenu").first();
                capacity = interiorBlock.select("td").first().text();
            }

            Element pictureDiv = boatPage.select("div.grandePhoto").first();
            String picture = pictureDiv.select("img").attr("src");

            Element priceTable = boatPage.select("table.tarifs").first();
            price = priceTable.select("tbody td").get(type == BoatType.BATEAU_A_MOTEUR ? 3 : 0).text();

            boatData.put("title", title);
            boatData.put("description", description);
            boatData.put("length", length);
            boatData.put("year", year);
            boatData.put("capacity", capacity);
            boatData.put("pictureUrl", picture);
            boatData.put("weekPrice", price);
            boatData.put("boatUrl", link);
            boatData.put("type", type.getLibelle());

            return boatData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extrait l'URL depuis l'attribut onClick d'un élément.
     *
     * @param onClick l'élément contenant l'attribut onClick
     * @return l'URL extraite
     */
    private static String extractUrl(Element onClick) {
        String url = onClick.attr("onClick");
        String afterEqual = url.split("=")[1];
        return afterEqual.replace("'", "").replace("\"", "").trim();
    }
}