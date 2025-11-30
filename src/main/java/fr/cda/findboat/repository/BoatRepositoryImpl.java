package fr.cda.findboat.repository;

import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.factory.BoatFactory;
import fr.cda.findboat.factory.TypeFactory;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoatRepositoryImpl implements BoatRepository {

    private final Connection connection = DataBase.getInstance().getConnection();
    private final TypeRepository typeRepository;

    public BoatRepositoryImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public void save(Boat boat) throws SQLException {
        String query = "insert into boat (title, description, length, constructionYear, capacity, pictureUrl, weekPrice, idType) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, boat.getTitle());
            stmt.setString(2, boat.getDescription());
            stmt.setDouble(3, boat.getLength());
            stmt.setInt(4, boat.getYear());
            stmt.setInt(5, boat.getCapacity());
            stmt.setString(6, boat.getPictureUrl());
            stmt.setDouble(7, boat.getWeekPrice());
            stmt.setInt(8, boat.getType().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Le bateau n’a pas pu être sauvegardé. Merci de réessayer.");
        }
    }

    @Override
    public Boat findById(int id) throws SQLException {
        String query = "select * from boat where id=?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idBoat = rs.getInt("idBoat");
                String title = rs.getString("title");
                String description = rs.getString("description");
                double length = rs.getDouble("length");
                int year = rs.getInt("constructionYear");
                int capacity = rs.getInt("capacity");
                String pictureUrl = rs.getString("pictureUrl");
                double weekPrice = rs.getDouble("weekPrice");
                Type type = this.typeRepository.findById(rs.getInt("idType"));

                return BoatFactory.create(idBoat, title, description, length, year, capacity, pictureUrl, weekPrice, type);

            }
            return  null;

        } catch (SQLException e) {
            throw new SQLException("Le bateau demandé n’a pas été trouvé.");
        }
    }

    @Override
    public List<Boat> findAll() throws SQLException {
        List<Boat> boats = new ArrayList<>();
        String query = "select * from boat";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idBoat = rs.getInt("idBoat");
                String title = rs.getString("title");
                String description = rs.getString("description");
                double length = rs.getDouble("length");
                int year = rs.getInt("constructionYear");
                int capacity = rs.getInt("capacity");
                String pictureUrl = rs.getString("pictureUrl");
                double weekPrice = rs.getDouble("weekPrice");
                Type type = this.typeRepository.findById(rs.getInt("idType"));

                boats.add(BoatFactory.create(idBoat, title,  description, length, year, capacity, pictureUrl, weekPrice, type));
            }
            return  boats;

        } catch (SQLException e) {
            throw new SQLException("Les bateaux demandés n’ont pas été trouvés.");
        }
    }

    @Override
    public void update(Boat boat) throws SQLException {
        String query = "update boat set title = ?, description = ?, length = ?, constructionYear = ?, capacity = ?,  pictureUrl = ?, weekPrice = ?, idType = ?, where idType = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, boat.getTitle());
            stmt.setString(2, boat.getDescription());
            stmt.setDouble(3, boat.getLength());
            stmt.setInt(4, boat.getYear());
            stmt.setInt(5, boat.getCapacity());
            stmt.setString(6, boat.getPictureUrl());
            stmt.setDouble(7, boat.getWeekPrice());
            stmt.setInt(8, boat.getType().getId());
            stmt.setInt(9, boat.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Le bateau n'a pas pu être mis à jour.");
        }

    }

    @Override
    public void delete(Boat boat) throws SQLException {
        String query = "delete from boat where idBoat = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, boat.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Le bateau n'a pas pu être supprimé.");
        }

    }
}
