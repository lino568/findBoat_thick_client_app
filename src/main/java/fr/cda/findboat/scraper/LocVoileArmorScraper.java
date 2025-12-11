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

public class LocVoileArmorScraper {

    private LocVoileArmorScraper() {
    }

    /**
     * Récupère les liens des bateaux selon le type recherché.
     *
     * @param boatType le type de bateau recherché
     * @return la liste des URLs des bateaux
     * @throws IOException si la connexion échoue
     */
    public static List<String> getLinks(BoatType boatType) throws IOException {

        int maxLinks = 50;
        String url = "";

        if (boatType == BoatType.BATEAU_A_MOTEUR) {
            url = "https://locvoilearmor.com/louer-un-bateau-moteur-a-saint-quay-portrieux/";
        } else if (boatType == BoatType.VOILIER) {
            url = "https://locvoilearmor.com/louer-un-voilier-en-bretagne-nord/";
        }
        List<String> boatslink = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();

        Elements boatsElement = doc.select("a.elementor-element");

        for (Element boatLink : boatsElement) {
            if (boatslink.size() >= maxLinks) break;
            boatslink.add(boatLink.attr("href"));
        }

        return boatslink;
    }

    /**
     * Récupère les données détaillées d'un bateau depuis son URL.
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

            String title = boatPage.select("div.floaty-title").text();

            description = boatPage.select("div.woocommerce-product-details__short-description p").getFirst().text();

            Element caractTable = boatPage.selectFirst("div[data-id=6ed195f]");

            Elements caracValues = caractTable.select("div.elementor-widget-container h3");

            String length = caracValues.get(type == BoatType.BATEAU_A_MOTEUR ? 3 : 1).text();

            String year =  caracValues.get(type == BoatType.BATEAU_A_MOTEUR ? 0 : 3).text();

            capacity = caracValues.get(type == BoatType.BATEAU_A_MOTEUR ? 2 : 6).text();

            Element pictureDiv = boatPage.select("img").get(1);
            String picture = pictureDiv.select("img").attr("src");

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
}