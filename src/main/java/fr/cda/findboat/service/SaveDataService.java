package fr.cda.findboat.service;

import fr.cda.findboat.model.Boat;
import fr.cda.findboat.model.Type;
import fr.cda.findboat.repository.BoatRepository;
import fr.cda.findboat.repository.TypeRepository;

import java.sql.SQLException;
import java.util.List;

public class SaveDataService {

    private BoatRepository boatRepository;
    private TypeRepository typeRepository;

    /**
     * Constructeur du service de sauvegarde des données.
     *
     * @param boatRepository le repository des bateaux
     * @param typeRepository le repository des types
     */
    public SaveDataService(BoatRepository boatRepository, TypeRepository typeRepository) {
        this.boatRepository = boatRepository;
        this.typeRepository = typeRepository;
    }

    /**
     * Sauvegarde une liste de bateaux en base de données.
     * Associe chaque bateau à son type existant en base.
     *
     * @param boats la liste des bateaux à sauvegarder
     * @throws SQLException si la sauvegarde échoue
     */
    public void saveBoats(List<Boat> boats) throws SQLException {

        if (boats == null || boats.isEmpty()) {
            return;
        }
        for (Boat boat : boats) {
            Type type = this.typeRepository.findByName(boat.getType().getName());
            if (type == null) {
                return;
            }

            boat.setType(type);
            boatRepository.save(boat);
        }
    }

}