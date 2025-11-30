package fr.cda.findboat.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase instance;
    private Connection connection;

    private final String host = "localhost:3306";
    private final String dbName = "findboat";
    private final String user = "root";
    private final String password = "";

    private DataBase() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, user, password);
            InitDatabase.initializeDatabase(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
