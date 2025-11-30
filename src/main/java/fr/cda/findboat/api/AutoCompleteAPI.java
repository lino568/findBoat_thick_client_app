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

    private static final String url = "https://geo.api.gouv.fr/communes?nom=";
    private static final Logger log = LoggerFactory.getLogger(AutoCompleteAPI.class);

    public static List<String> getCities(String cityValue) {
        List<String> cities = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url + cityValue))
                    .GET()
                    .build();

            try {
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
                    log.warn("Erreur lors de l'appel API : " + responseBody );
                }

            } catch (IOException e) {
                log.warn("Erreur d'entrée/sortie lors de la communication : " + e);
            } catch (InterruptedException _) {

            }

        } catch (UncheckedIOException e) {
            log.warn("Erreur IO non vérifiée", e);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

        return cities;
    }


}
