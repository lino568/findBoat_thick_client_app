package fr.cda.findboat.scraper;

import fr.cda.findboat.enums.BoatType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrouestyLocationScraper {

    private static Logger log = LoggerFactory.getLogger(CrouestyLocationScraper.class);
    private static String url = "https://www.crouesty-location.com";

    public static Document setParameters(BoatType boatType, LocalDate startDate, LocalDate endDate) throws IOException {

        String type = "";
        if (boatType == BoatType.VOILIER) {
            type = "voilier";
        } else if (boatType == BoatType.BATEAU_A_MOTEUR) {
            type = "moteur";
        }

        try {
            Document mainPage = Jsoup.connect(url).get();
            Element form = mainPage.selectXpath("//form").first();

            String postUrl = form.attr("action");

            return Jsoup.connect(url + postUrl)
                    .data("type", type)
                    .data("datefrom", startDate.toString())
                    .data("dateto", endDate.toString())
                    .post();

        } catch (IOException e) {
            log.info("An error occurred when reading the main page URL" + e);
            throw new IOException(e);
        }
    }

    public static List<String> getLinks(Document boatsPage) {

        int maxLinks = 10;

        List<String> boatslink = new ArrayList<>();
        Elements boatsElement = boatsPage.select("div.bateau-content");

        for (Element boatDiv : boatsElement) {
            if (boatslink.size() >= maxLinks) break;

            Element link = boatDiv.selectFirst("a");
            if (link != null) {
                boatslink.add(link.attr("href"));
            }
        }

        return boatslink;
    }

    public static HashMap<String, String> getBoatData(BoatType boatType, String boatLink) throws IOException {
        HashMap<String, String> boatValues = new HashMap<>();
        String capacity = "";

        try {
            Document boatPage = Jsoup.connect(boatLink).get();

            String title = boatPage.select("span.breadcrumb_last").text();
            String description = boatPage.select("div.entry-content > p").first().text();

            Element boatFeatures = boatPage.getElementById("carac");

            Element labelLengthDiv = boatFeatures.selectFirst("div.label:containsOwn(Longueur)");
            Element parentLengthDiv = labelLengthDiv.parent();
            String length = parentLengthDiv.selectFirst("div.value").text();

            Element labelYearhDiv = boatFeatures.selectFirst("div.label:containsOwn(Année)");
            Element parentYearDiv = labelYearhDiv.parent();
            String year = parentYearDiv.selectFirst("div.value").text();

            if (boatType == BoatType.VOILIER) {
                Element boatConfort = boatPage.getElementById("confort");

                Element labelCapacityDiv = boatConfort.selectFirst("div.label:containsOwn(couchettes)");
                Element parentCapacityDiv = labelCapacityDiv.parent();
                capacity = parentCapacityDiv.selectFirst("div.value").text();

            } else if (boatType == BoatType.BATEAU_A_MOTEUR) {
                Element boatSecurity = boatPage.getElementById("equip");

                Element labelCapacityDiv = boatSecurity.selectFirst("div.label:containsOwn(Sécurité)");
                Element parentCapacityDiv = labelCapacityDiv.parent();
                capacity = parentCapacityDiv.selectFirst("div.value").text();
            }

            Element pictureDiv = boatPage.getElementById("image");
            String mainPictureLink = pictureDiv.select("a").attr("href");

            Elements weekPriceDiv = boatPage.getElementsByClass(boatType == BoatType.VOILIER ? "tarif_saison_voilier" : "tarif_saison_moteur");
            String weekPrice = "";

            if (boatType == BoatType.VOILIER) {
            weekPrice = weekPriceDiv.selectFirst("td.prix").text();

            } else if (boatType == BoatType.BATEAU_A_MOTEUR) {
                weekPrice =  boatPage.select("tbody tr").get(1).select("td").get(2).text();
            }


            boatValues.put("title", title);
            boatValues.put("description", description);
            boatValues.put("length", length);
            boatValues.put("year", year);
            boatValues.put("capacity", capacity);
            boatValues.put("pictureUrl", mainPictureLink);
            boatValues.put("weekPrice", weekPrice);
            boatValues.put("type", boatType.getLibelle());

            return boatValues;

        } catch (IOException e) {
            log.info("An error occurred when reading the boat page URL" + e);
            throw new IOException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        CrouestyLocationScraper scraper = new CrouestyLocationScraper();
//        Document dom = scraper.setParameters(BoatType.BATEAU_A_MOTEUR, LocalDate.now(), LocalDate.now().plusDays(1));
//        List<String> links = scraper.getLinks(dom);
//        System.out.println(links);
//        for (String link : links) {
//            scraper.getBoatData(BoatType.BATEAU_A_MOTEUR, link);
//        }
        System.out.println(scraper.getBoatData( BoatType.BATEAU_A_MOTEUR,"https://www.crouesty-location.com/bateau/zodiac-medline-660/"));
    }
}
