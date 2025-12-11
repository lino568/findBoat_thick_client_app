package fr.cda.findboat.repository;

import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;

import java.sql.SQLException;
import java.util.List;

public interface TypeRepository {

    void save(Type type) throws SQLException;
    Type findById(int id) throws SQLException;
    Type findByName(String name) throws SQLException;
    List<Type> findAll() throws SQLException;
    void update(Type type) throws SQLException;
    void delete(Type type) throws SQLException;
}
