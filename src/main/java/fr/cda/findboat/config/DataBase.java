package fr.cda.findboat.config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.cda.findboat.helper.AESKeyManager;
import fr.cda.findboat.helper.AESManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase instance;
    private Connection connection;
    private static boolean testMode = false;
    private final Logger log = LoggerFactory.getLogger(DataBase.class);

    /**
     * Pattern Singleton afin de créer la connexion à la BDD.
     * Charge la configuration chiffrée et initialise la connexion.
     */
    private DataBase() {

        // Si on est en mode test, ne rien faire
        if (testMode) {
            return;
        }

        try {
            ObjectNode dbInfoJson;
            AESKeyManager.generateAndSaveKey();
            SecretKey secretKey = AESKeyManager.loadKeyFromFile();

            dbInfoJson = AESManager.decryptToObjectNode(secretKey);

            String host = dbInfoJson.get("host").asText();
            String port = dbInfoJson.get("port").asText();
            String dbName = dbInfoJson.get("dbname").asText();
            String login = dbInfoJson.get("login").asText();
            String password = dbInfoJson.get("password").asText();

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

            this.connection = DriverManager.getConnection(jdbcUrl, login, password);

            InitDatabase.initializeDatabase(connection);

        } catch (SQLException e) {
            this.log.warn("Problème de connexion à la BDD : {}", e.getMessage());
            throw new RuntimeException("Erreur de connexion à la base de données");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne l'instance unique de la connexion à la base de données.
     *
     * @return l'instance de DataBase
     */
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    /**
     * Retourne la connexion active à la base de données.
     *
     * @return la connexion SQL
     */
    public Connection getConnection() {
        return this.connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Vérifie si une connexion peut être établie avec les paramètres fournis.
     *
     * @param host     l'hôte de la base de données
     * @param port     le port de connexion
     * @param dbName   le nom de la base de données
     * @param user     le nom d'utilisateur
     * @param password le mot de passe
     * @return true si la connexion réussit, false sinon
     */
    public static boolean checkConnection(String host, String port, String dbName, String user, String password) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + dbName, user, password)) {

            if (connection != null && !connection.isClosed()) {
                instance = null;
                return true;
            }
            return false;

        } catch (SQLException _) {
            return false;
        }
    }

    /**
     * MÉTHODE POUR LES TESTS - Injecte une connexion sans appeler le constructeur
     */
    public static void setTestConnection(Connection testConnection) {
        // Activer le mode test
        testMode = true;

        // Créer l'instance vide
        instance = new DataBase();

        // Injecter la connexion via le setter privé (plus simple !)
        instance.setConnection(testConnection);
    }

    /**
     * Réinitialise l'instance (utile pour les tests).
     */
    public static void resetInstance() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                throw new  RuntimeException(e);
            }
        }
        instance = null;
        testMode = false;
    }
}