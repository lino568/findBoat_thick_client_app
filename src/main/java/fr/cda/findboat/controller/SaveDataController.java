package fr.cda.findboat.controller;

import fr.cda.findboat.model.Boat;
import fr.cda.findboat.service.SaveDataService;
import fr.cda.findboat.view.SaveDataViewInterface;

import java.sql.SQLException;
import java.util.List;

public class SaveDataController {

    private final SaveDataService saveDataService;
    private final SaveDataViewInterface saveDataView;
    private List<Boat> boats;

    /**
     * Constructeur du contrôleur de sauvegarde des données.
     *
     * @param saveDataService le service de sauvegarde des bateaux
     * @param saveDataView l'interface de la vue
     */
    public SaveDataController(SaveDataService saveDataService, SaveDataViewInterface saveDataView) {
        this.saveDataService = saveDataService;
        this.saveDataView = saveDataView;
    }

    /**
     * Configure les écouteurs d'événements de la vue.
     */
    public void setupListeners() {
        this.saveDataView.setSaveListener(e -> saveData());
    }

    /**
     * Définit la liste des bateaux à sauvegarder.
     *
     * @param boats la liste des bateaux
     */
    public void setListBoat(List<Boat> boats) {
        this.boats = boats;
    }

    /**
     * Sauvegarde les bateaux en base de données.
     */
    private void saveData() {

        if (this.boats == null || this.boats.isEmpty()) {
            this.saveDataView.setErrorMessage("Aucun bateau à enregistrer, veuillez effectuer une nouvelle recherche.");
            return;
        }

        try {
            this.saveDataService.saveBoats(this.boats);

            this.saveDataView.closeWindow();
        } catch (SQLException e) {
            this.saveDataView.setErrorMessage(e.getMessage());
        }
    }
}