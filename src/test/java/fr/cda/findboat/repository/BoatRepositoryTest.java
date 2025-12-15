package fr.cda.findboat.repository;

import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.config.InitDatabase;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class BoatRepositoryTest {

    private BoatRepository boatRepository;
    private Type mockType1;
    private Type mockType2;

    private Boat boat1;
    private Boat boat2;
    private Boat boat3;

    private TypeRepository mockTypeRepository;

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
        InitDatabase.initializeDatabase(connection);
    }

    @AfterAll
    static void tearDown() {
        DataBase.resetInstance();
    }

    @BeforeEach
    void setUp() throws Exception {
        Connection connection = DataBase.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM boat");

        mockTypeRepository = mock(TypeRepository.class);

        boatRepository = new BoatRepositoryImpl(mockTypeRepository);

        mockType1 = Mockito.mock(Type.class);
        when(mockType1.getId()).thenReturn(1);
        when(mockType1.getName()).thenReturn("Voilier");

        mockType2 = Mockito.mock(Type.class);
        when(mockType2.getId()).thenReturn(2);
        when(mockType2.getName()).thenReturn("Moteur");

        this.boat1 = new Boat("Bateau A", "Description A", 10.0, 2020, 4,
                "url1.jpg", 1000.00, "http://a.com",
                mockType1);

        this.boat2 = new Boat("Bateau B", "Description B", 15.0, 2021, 6,
                "url2.jpg", 2000.00, "http://b.com",
                mockType2);

        this.boat3 = new Boat("Bateau C", "Description C", 20.0, 2022, 8,
                "url3.jpg", 3000.00, "http://c.com",
                mockType1);
    }

    @Test
    @Order(1)
    void testSave() throws Exception {

        boatRepository.save(this.boat1);

        List<Boat> found = boatRepository.findAll();
        assertNotNull(found, "Le bateau sauvegardé devrait être retrouvé seul dans une liste");

        assertEquals(1, found.size());

        for (Boat boat : found) {
            assertEquals(this.boat1.getTitle(), boat.getTitle());
        }

    }

    @Test
    @Order(2)
    void testFindAll() throws Exception {
        boatRepository.save(this.boat1);
        boatRepository.save(this.boat2);
        boatRepository.save(this.boat3);

        List<Boat> found = boatRepository.findAll();
        assertNotNull(found);
        assertEquals(3, found.size());
    }
}
