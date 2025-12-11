package fr.cda.findboat.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAPI {

    private static final String URL = "https://geo.api.gouv.fr/communes?nom=";
    private static final Logger log = LoggerFactory.getLogger(AutoCompleteAPI.class);

    private AutoCompleteAPI() {
    }

    /**
     * Récupère une liste de villes françaises correspondant au texte saisi.
     * Utilise l'API geo.api.gouv.fr pour la recherche.
     *
     * @param cityValue le début du nom de la ville à rechercher
     * @return une liste de noms de villes correspondants
     */
    public static List<String> getCities(String cityValue) {
        List<String> cities = new ArrayList<>();
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL + cityValue))
                    .GET()
                    .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                int statusCode = response.statusCode();
                String responseBody = response.body();

                if (statusCode == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode dataTree = mapper.readTree(responseBody);

                    for (JsonNode section : dataTree) {
                        String city = section.get("nom").asText();
                        if (city.toLowerCase().startsWith(cityValue.toLowerCase())) {
                            cities.add(city);
                        }
                    }
                    return cities;

                } else {
                    log.warn("Erreur lors de l'appel API : {}", responseBody );
                }

        } catch (UncheckedIOException e) {
            log.warn("Erreur IO non vérifiée", e);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return cities;
    }
}