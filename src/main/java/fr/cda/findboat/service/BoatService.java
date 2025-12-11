package fr.cda.findboat.service;

import fr.cda.findboat.enums.BoatType;
import fr.cda.findboat.factory.BoatFactory;
import fr.cda.findboat.factory.TypeFactory;
import fr.cda.findboat.helper.Parser;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.scraper.AtlantiqueLocationScraper;
import fr.cda.findboat.scraper.CrouestyLocationScraper;
import fr.cda.findboat.scraper.LocVoileArmorScraper;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class BoatService {

    /**
     * Crée un objet Boat à partir des données récupérées par scraping.
     *
     * @param boatData le HashMap contenant les données du bateau
     * @return un objet Boat créé à partir des données
     */
    public Boat createBoatFromApiData(HashMap<String, String> boatData) {
        double length = Parser.parseDouble(boatData.get("length"));

        int year = Parser.parseInt(boatData.get("year"));
        int capacity = Parser.parseInt(boatData.get("capacity"));
        double weekPrice = Parser.parseDouble(boatData.get("weekPrice"));

        return BoatFactory.create(
                boatData.get("title"),
                boatData.get("description"),
                length,
                year,
                capacity,
                boatData.get("pictureUrl"),
                weekPrice,
                boatData.get("boatUrl"),
                TypeFactory.create(boatData.get("type"))
        );
    }

    /**
     * Récupère les liens des bateaux depuis Crouesty Location.
     *
     * @param boatType le type de bateau recherché
     * @param startDate la date de début de location
     * @param endDate la date de fin de location
     * @return la liste des URLs des bateaux
     * @throws IOException si la récupération échoue
     */
    public List<String> getCrouestyLocationBoatsLink(BoatType boatType, LocalDate startDate, LocalDate endDate) throws IOException {

        try {
            Document document = CrouestyLocationScraper.setParameters(boatType, startDate, endDate);
            return CrouestyLocationScraper.getLinks(document);

        } catch (IOException e) {
            throw new IOException("Une erreur est survenue lors de la recherche. Veuillez réessayer.", e);
        }
    }

    /**
     * Récupère les liens des bateaux depuis Atlantique Location.
     *
     * @param boatType le type de bateau recherché
     * @return la liste des URLs des bateaux
     */
    public List<String> getAtlantiqueLocationLinks(BoatType boatType) {
        Document document = AtlantiqueLocationScraper.setParameters(boatType);
        return AtlantiqueLocationScraper.getLinks(document);
    }

    /**
     * Récupère les liens des bateaux depuis Loc Voile Armor.
     *
     * @param boatType le type de bateau recherché
     * @return la liste des URLs des bateaux
     * @throws IOException si la récupération échoue
     */
    public List<String> getLocVoileArmorLinks(BoatType boatType) throws IOException {
        return LocVoileArmorScraper.getLinks(boatType);
    }

}