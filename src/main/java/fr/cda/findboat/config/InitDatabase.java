package fr.cda.findboat.config;

import fr.cda.findboat.enums.BoatType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Arrays;

public class InitDatabase {

    public static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

    /**
     * Initialise la base de données en créant les tables nécessaires si elles n'existent pas.
     *
     * @param connection la connexion à la base de données
     */
    public static void initializeDatabase(Connection connection) {

        try {
            if (!isTableExist(connection,"type")) {
                createTypeTable(connection);
                insertBoatType(connection, BoatType.BATEAU_A_MOTEUR.getLibelle());
                insertBoatType(connection, BoatType.VOILIER.getLibelle());
            }

            if (!isTableExist(connection, "boat")) {
                createBoatTable(connection);
            }

        } catch (SQLException e) {
            log.error("Erreur lors de l'initialisation de la bdd : {}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    /**
     * Vérifie si une table existe dans la base de données.
     *
     * @param connection la connexion à la base de données
     * @param tableName le nom de la table à vérifier
     * @return true si la table existe, false sinon
     */
    private static boolean isTableExist(Connection connection, String tableName) {
        try {
            // Récupérer explicitement le nom de la base de données active
            String databaseName = connection.getCatalog();

            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(databaseName, null, tableName, new String[]{"TABLE"});

            return tables.next();

        } catch (SQLException e) {
            log.error("Erreur lors de la vérification de l'existence de la table : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée la table "type" dans la base de données.
     *
     * @param connection la connexion à la base de données
     * @throws SQLException si une erreur SQL survient
     */
    private static void createTypeTable(Connection connection) throws SQLException {
        String query = """
                CREATE TABLE type(
                   idType INT AUTO_INCREMENT,
                   libelle VARCHAR(60) NOT NULL,
                   PRIMARY KEY(idType),
                   UNIQUE(libelle)
                )
                """;

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    /**
     * Crée la table "boat" dans la base de données.
     *
     * @param connection la connexion à la base de données
     * @throws SQLException si une erreur SQL survient
     */
    private static void createBoatTable(Connection connection) throws SQLException {
        String query = """
                CREATE TABLE boat(
                   idBoat INT AUTO_INCREMENT,
                   title VARCHAR(120) NOT NULL,
                   description TEXT,
                   length DECIMAL(7,2),
                   constructionYear INT,
                   capacity INT,
                   pictureUrl VARCHAR(250) NOT NULL,
                   weekPrice DECIMAL(10,2),
                   url VARCHAR(250) NOT NULL,
                   idType INT NOT NULL,
                   PRIMARY KEY(idBoat),
                   FOREIGN KEY(idType) REFERENCES type(idType)
                )
                """;

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    /**
     * Insère un type de bateau dans la table "type".
     *
     * @param connection la connexion à la base de données
     * @param libelle le libellé du type de bateau
     * @throws SQLException si une erreur SQL survient
     */
    private static void insertBoatType(Connection connection, String libelle) throws SQLException {
        String query = "insert into type (libelle) values (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, libelle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("Problème lors de l'insertion des types de bateau en BDD : {}", (Object) e.getStackTrace());
            throw new RuntimeException(e);
        }

    }
}