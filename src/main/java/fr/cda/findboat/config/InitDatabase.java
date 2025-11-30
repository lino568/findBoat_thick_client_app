package fr.cda.findboat.config;

import fr.cda.findboat.enums.BoatType;
import fr.cda.findboat.helper.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Arrays;

public class InitDatabase {

    public static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

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
            log.error("Erreur lors de l'initialisation de la bdd : " + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    private static boolean isTableExist(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return tables.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                   idType INT NOT NULL,
                   PRIMARY KEY(idBoat),
                   FOREIGN KEY(idType) REFERENCES type(idType)
                )
                """;

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    private static void insertBoatType(Connection connection, String libelle) throws SQLException {
        String query = "insert into type (libelle) values (?)";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, libelle);
        stmt.executeUpdate();
    }
}
