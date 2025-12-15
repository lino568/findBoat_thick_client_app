package fr.cda.findboat.repository;

import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.factory.TypeFactory;
import fr.cda.findboat.model.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeRepositoryImpl implements TypeRepository{

    private final Connection connection = DataBase.getInstance().getConnection();

    /**
     * Sauvegarde un type de bateau en base de données.
     *
     * @param type le type de bateau à sauvegarder
     * @throws SQLException si la sauvegarde échoue
     */
    @Override
    public void save(Type type) throws SQLException {
        String query = "insert into type (libelle) VALUES (?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, type.getName());
            stmt.execute();

        } catch (SQLException _) {
            throw new SQLException("Le type de bateau n'a pas pu être sauvegardé. Merci de réessayer.");
        }
    }

    /**
     * Recherche un type de bateau par son identifiant.
     *
     * @param id l'identifiant du type
     * @return le type trouvé, ou null si non trouvé
     * @throws SQLException si la recherche échoue
     */
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
        } catch (SQLException _) {
            throw new SQLException("Le type de bateau demandé n'a pas été trouvé.");
        }
    }

    /**
     * Recherche un type de bateau par son nom.
     *
     * @param name le nom du type
     * @return le type trouvé, ou null si non trouvé
     * @throws SQLException si la recherche échoue
     */
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
        } catch (SQLException _) {
            throw new SQLException("Le type de bateau demandé n'a pas été trouvé.");
        }
    }

    /**
     * Récupère tous les types de bateaux de la base de données.
     *
     * @return la liste de tous les types
     * @throws SQLException si la récupération échoue
     */
    @Override
    public List<Type> findAll() throws SQLException {
        List<Type> types = new ArrayList<>();
        String query = "select * from type";

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int idType = rs.getInt("idType");
                String libelle = rs.getString("libelle");
                types.add(TypeFactory.create(idType, libelle));
            }
            return types;

        } catch (SQLException e) {
            throw new SQLException("Les types de bateau n'ont pas été trouvés");
        }
    }

    /**
     * Met à jour un type de bateau existant en base de données.
     *
     * @param type le type avec les nouvelles données
     * @throws SQLException si la mise à jour échoue
     */
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

    /**
     * Supprime un type de bateau de la base de données.
     *
     * @param type le type à supprimer
     * @throws SQLException si la suppression échoue
     */
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