package fr.cda.findboat.repository;

import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.factory.TypeFactory;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeRepositoryImpl implements TypeRepository{

    private final Connection connection = DataBase.getInstance().getConnection();

    @Override
    public void save(Type type) throws SQLException {
        String query = "insert into type (libelle) VALUES (?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, type.getName());
            stmt.execute();

        } catch (SQLException e) {
            throw new SQLException("Le type de bateau n’a pas pu être sauvegardé. Merci de réessayer.");
        }
    }

    @Override
    public Type findById(int id) throws SQLException {
        String query = "select * from type where idType = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idType = rs.getInt("idType");
                String libelle = rs.getString("libelle");
                return TypeFactory.create(idType, libelle);
            }

            return null;
        } catch (SQLException e) {
            throw new SQLException("Le type de bateau demandé n’a pas été trouvé.");
        }
    }

    @Override
    public Type findByName(String name) throws SQLException {
        String query = "select * from type where libelle = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idType = rs.getInt("idType");
                String libelle = rs.getString("libelle");
                return TypeFactory.create(idType, libelle);
            }

            return null;
        } catch (SQLException e) {
            throw new SQLException("Le type de bateau demandé n’a pas été trouvé.");
        }
    }

    @Override
    public List<Boat> findAll() throws SQLException {
        List<Type> types = new ArrayList<>();
        String query = "select * from type";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int idType = rs.getInt("idType");
                String libelle = rs.getString("libelle");
                types.add(TypeFactory.create(idType, libelle));
            }
            return null;

        } catch (SQLException e) {
            throw new SQLException("Les types de bateau n'ont pas été trouvés");
        }
    }

    @Override
    public void update(Type type) throws SQLException {

        String query = "update type set libelle = ? where idType = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, type.getName());
            stmt.setInt(2, type.getId());
            stmt.execute();

        } catch (SQLException e) {
            throw new SQLException("Le type de bateau n'a pas pu être mis à jour.");
        }
    }

    @Override
    public void delete(Type type) throws SQLException {

        String query = "delete from type where idType = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, type.getId());
            stmt.execute();

        } catch (SQLException e) {
            throw new SQLException("Le type de bateau n'a pas pu être supprimé.");
        }
    }
}
