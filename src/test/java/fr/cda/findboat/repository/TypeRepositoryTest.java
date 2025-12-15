package fr.cda.findboat.repository;

import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.config.InitDatabase;
import fr.cda.findboat.model.Type;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class TypeRepositoryTest {

    private TypeRepository typeRepository;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Créer la connexion vers le conteneur MySQL
        Connection connection = DriverManager.getConnection(
                mysql.getJdbcUrl(),
                mysql.getUsername(),
                mysql.getPassword()
        );

        DataBase.setTestConnection(connection);

        InitDatabase.createTypeTable(connection);
    }

    @AfterAll
    static void tearDown() {
        DataBase.resetInstance();
    }

    @BeforeEach
    void setUp() throws Exception {
        Connection connection = DataBase.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM type");

        // Initialiser le repository
        typeRepository = new TypeRepositoryImpl();
    }

    @Test
    @Order(1)
    void testSave() throws Exception {
        Type type = new Type("Voilier");

        typeRepository.save(type);

        Type found = typeRepository.findByName("Voilier");
        assertNotNull(found, "Le type sauvegardé devrait être retrouvé");
        assertEquals("Voilier", found.getName());
    }

    @Test
    @Order(2)
    void testFindById() throws Exception {
        Type type = new Type("Voilier");

        typeRepository.save(type);

        Type found = typeRepository.findByName("Voilier");
        assertNotNull(found);

        Type foundById = typeRepository.findById(found.getId());
        assertNotNull(foundById, "Le type doit êrte retrouvé par son Id");
        assertEquals(found.getId(), foundById.getId());
        assertEquals(found.getName(), foundById.getName());
    }

    @Test
    @Order(3)
    void testFindAll() throws Exception {
        typeRepository.save(new Type("Voilier"));
        typeRepository.save(new Type("Moteur"));
        typeRepository.save(new Type("Catamaran"));

        List<Type> found = typeRepository.findAll();
        assertNotNull(found);
        assertEquals(3, found.size());
    }

    @Test
    @Order(5)
    void testUpdate() throws Exception {
        Type type = new Type("Voile");
        typeRepository.save(type);

        Type found = typeRepository.findByName("Voile");
        assertNotNull(found);
        int typeId = found.getId();

        found.setName("Moteur");
        typeRepository.update(found);

        Type updated = typeRepository.findById(typeId);
        assertNotNull(updated, "Le type mis à jour devrait toujours exister");
        assertEquals("Moteur", updated.getName());
        assertEquals(typeId, updated.getId(), "L'ID ne devrait pas changer");

        Type oldName = typeRepository.findByName("Voile");
        assertNull(oldName, "L'ancien nom ne devrait plus exister");
    }

}