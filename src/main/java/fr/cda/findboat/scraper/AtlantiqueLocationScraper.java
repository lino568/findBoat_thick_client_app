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

    public static HashMap<String, String> getBoatData(BoatType type, String link) {
        HashMap<String, String> boatData = new HashMap<>();
        String description = "";
        String capacity = "";

        try {
            Document boatPage = Jsoup.connect(link).get();

            String title = boatPage.select("h1 span").text();

            Element caracteristicTable = boatPage.select("table.caracteristiques").first();

            if (type == BoatType.VOILIER) {
                Element commentBlock = caracteristicTable.select("tr.contenu").get(2);
                description = caracteristicTable.select("td").text();
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
            String price = priceTable.select("tbody td").get(3).text();

            boatData.put("title", title);
            boatData.put("description", description);
            boatData.put("length", length);
            boatData.put("year", year);
            boatData.put("capacity", capacity);
            boatData.put("pictureUrl", picture);
            boatData.put("weekPrice", price);

            return boatData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractUrl(Element onClick) {
        String url = onClick.attr("onClick");
        String afterEqual = url.split("=")[1];
        return afterEqual.replace("'", "").replace("\"", "").trim();
    }

    public static void main(String[] args) {
        List<HashMap<String, String>> boatData = new ArrayList<>();
        Document dom = setParameters(BoatType.VOILIER);
        List<String> links = getLinks(dom);
//        System.out.println(links);
        for (String link : links) {
            boatData.add(getBoatData(BoatType.VOILIER, link));
        }
        System.out.println(boatData);

//        System.out.println(AtlantiqueLocationScraper.getBoatData("https://www.atlantique-location.fr/location-bateau-moteur/highfield-escape-540-family/grospere-1"));
    }
}
