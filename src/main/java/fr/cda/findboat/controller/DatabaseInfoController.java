package fr.cda.findboat.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.cda.findboat.config.DataBase;
import fr.cda.findboat.service.DatabaseInfoService;
import fr.cda.findboat.view.DatabaseInfoViewInterface;

public class DatabaseInfoController {

    private final DatabaseInfoService databaseInfoService;
    private final DatabaseInfoViewInterface databaseInfoViewInterface;

    /**
     * Constructeur du contrôleur de configuration de la base de données.
     *
     * @param databaseInfoService le service de gestion des informations de base de données
     * @param databaseInfoViewInterface l'interface de la vue
     */
    public DatabaseInfoController(DatabaseInfoService databaseInfoService, DatabaseInfoViewInterface databaseInfoViewInterface) {
        this.databaseInfoService = databaseInfoService;
        this.databaseInfoViewInterface = databaseInfoViewInterface;
    }

    /**
     * Configure les écouteurs d'événements de la vue.
     */
    public void setupListeners() {
        this.databaseInfoViewInterface.submit(e -> submit());
    }

    /**
     * Traite la soumission du formulaire de configuration de la base de données.
     * Valide les champs, teste la connexion et enregistre la configuration chiffrée.
     */
    private void submit() {
        this.databaseInfoViewInterface.setErrorMessage("");

        String host = this.databaseInfoViewInterface.getHost();
        String port = this.databaseInfoViewInterface.getPort();
        String dbName = this.databaseInfoViewInterface.getDbName();
        String login = this.databaseInfoViewInterface.getLogin();
        String password = this.databaseInfoViewInterface.getPassword();

        if (host.isBlank() || port.isBlank() || dbName.isBlank() || login.isBlank()) {
            this.databaseInfoViewInterface.setErrorMessage("Veuillez remplir tout les champs !");
            return;
        }

        if (!DataBase.checkConnection(host, port, dbName, login, password)) {
            this.databaseInfoViewInterface.setErrorMessage("Problème de connexion avec la base de données, veuillez vérifier les informations saisies.");
            return;
        }

        ObjectNode json = this.databaseInfoService.createDatabaseConfigNode(host, port, dbName, login, password);

        try {
            this.databaseInfoService.encryptJson(json);

            this.databaseInfoViewInterface.closeWindow();

        } catch (Exception _) {
            this.databaseInfoViewInterface.setErrorMessage("Un problème est survenu lors de l'enregistrement des informations !");
        }
    }
}