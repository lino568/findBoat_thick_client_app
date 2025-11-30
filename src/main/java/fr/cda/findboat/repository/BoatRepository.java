package fr.cda.findboat.repository;

import fr.cda.findboat.model.Boat;

import java.sql.SQLException;
import java.util.List;

public interface BoatRepository {

    void save(Boat boat) throws SQLException;
    Boat findById(int id) throws SQLException;
    List<Boat> findAll() throws SQLException;
    void update(Boat boat) throws SQLException;
    void delete(Boat boat) throws SQLException;

}
