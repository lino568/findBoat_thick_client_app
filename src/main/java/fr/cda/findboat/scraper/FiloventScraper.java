package fr.cda.findboat.scraper;

import fr.cda.findboat.enums.BoatType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;

public class FiloventScraper {

    public static Document setParameters(BoatType boatType, LocalDate startDate, LocalDate endDate, String city) {
        String type = "";
        if (boatType == BoatType.VOILIER) {
            type = "Sailboat";
        } else if (boatType == BoatType.BATEAU_A_MOTEUR) {
            type = "Motorboat";
        }

        String url = "https://www.clickandboat.com/location-bateau/recherche?boatTypes=" + type + "&dateFrom=" + startDate.toString() + "&dateTo=" + endDate.toString() + "&where=" + city;
        String test = "https://www.atlantique-location.fr/location-bateau-morbihan.html";
        try {
        return Jsoup.connect(test).get();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(setParameters(BoatType.BATEAU_A_MOTEUR, LocalDate.now(), LocalDate.now(), "Vannes"));
    }
}
