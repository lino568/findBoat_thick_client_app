package fr.cda.findboat.service;

import fr.cda.findboat.helper.Util;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;

public class MGEmail {

    /**
     * Envoie un email avec une pièce jointe via l'API Mailgun.
     *
     * @param pdfFile le fichier PDF à joindre à l'email
     * @return le corps de la réponse JSON
     * @throws UnirestException si l'envoi échoue
     */
    public static JsonNode sendEmailWithAttachment(File pdfFile) throws UnirestException {
        String apiKey = Util.dotenv.get("API_TOKEN");
        String domain = Util.dotenv.get("MAILGUN_DOMAIN");

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
                .basicAuth("api", apiKey)
                .field("from", "Mailgun Sandbox <postmaster@sandboxa14af39b21c44776a163ac9e0f0d730e.mailgun.org>")
                .field("to", "Lino Julien <jlino@greta-bretagne-sud.fr>")
                .field("subject", "Liste des bateaux disponibles")
                .field("text", "Bonjour,\n\n" +
                        "Vous trouverez en pièce jointe la liste des bateaux disponibles.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe FindBoat")
                .field("attachment", pdfFile)
                .asJson();

        return request.getBody();
    }
}