package fr.cda.findboat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.cda.findboat.helper.AESKeyManager;
import fr.cda.findboat.helper.AESManager;

import javax.crypto.SecretKey;

public class DatabaseInfoService {

    /**
     * Crée un nœud JSON contenant la configuration de la base de données.
     *
     * @param host l'hôte de la base de données
     * @param port le port de connexion
     * @param dbName le nom de la base de données
     * @param login le nom d'utilisateur
     * @param password le mot de passe
     * @return un ObjectNode contenant la configuration
     */
    public ObjectNode createDatabaseConfigNode(String host, String port, String dbName, String login, String password) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode config = mapper.createObjectNode();
        config.put("host", host);
        config.put("port", Integer.parseInt(port));
        config.put("dbname", dbName);
        config.put("login", login);
        config.put("password", password);

        return config;
    }

    /**
     * Chiffre et sauvegarde la configuration JSON de la base de données.
     *
     * @param jsonObject l'objet JSON à chiffrer
     * @throws Exception si le chiffrement échoue
     */
    public void encryptJson(ObjectNode jsonObject) throws Exception {

        AESKeyManager.generateAndSaveKey();
        SecretKey secretKey = AESKeyManager.loadKeyFromFile();

        AESManager.encryptObjectNode(jsonObject, secretKey);
    }
}