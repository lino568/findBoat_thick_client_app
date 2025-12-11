package fr.cda.findboat.controller;

import fr.cda.findboat.enums.BoatType;
import fr.cda.findboat.model.Boat;
import fr.cda.findboat.repository.BoatRepository;
import fr.cda.findboat.repository.BoatRepositoryImpl;
import fr.cda.findboat.repository.TypeRepository;
import fr.cda.findboat.repository.TypeRepositoryImpl;
import fr.cda.findboat.scraper.AtlantiqueLocationScraper;
import fr.cda.findboat.scraper.CrouestyLocationScraper;
import fr.cda.findboat.scraper.LocVoileArmorScraper;
import fr.cda.findboat.service.BoatService;
import fr.cda.findboat.service.DatabaseInfoService;
import fr.cda.findboat.service.PDFService;
import fr.cda.findboat.service.SaveDataService;
import fr.cda.findboat.task.TaskAutoComplete;
import fr.cda.findboat.view.DatabaseInfoViewImpl;
import fr.cda.findboat.view.EmailForPdfImpl;
import fr.cda.findboat.view.MainViewInterface;
import fr.cda.findboat.view.SaveDataViewImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainController {

    private MainViewInterface mainView;
    private BoatService boatService;
    private List<Boat> boatsInMemory = new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(MainController.class);
    private static final String searchErrorMessage = "Veuillez effectuer une recherche.";
    private static final String errorOpenWindow = "Erreur : impossible d'ouvrir la fenêtre";

    /**
     * Constructeur du contrôleur principal.
     *
     * @param boatService le service de gestion des bateaux
     */
    public MainController(BoatService boatService) {
        this.boatService = boatService;
    }

    /**
     * Définit la vue principale.
     *
     * @param mainView l'interface de la vue principale
     */
    public void setMainView(MainViewInterface mainView) {
        this.mainView = mainView;
    }

    /**
     * Configure tous les écouteurs d'événements de la vue principale.
     */
    public void listeners() {
        mainView.getCityInput().addEventFilter(KeyEvent.KEY_RELEASED, e -> autoComplete());
        mainView.setSearchListener(e -> searchBoat());
        mainView.setCreatePdfListener(e -> savePdf());
        mainView.setOpenEmailPdfWindow(e -> openEmailPdfWindow());
        mainView.setOpenBddInfoWindow(e -> openDatabaseInfoWindow());
        mainView.setOpenSaveDataWindow(e -> openSaveDataWindow());
    }

    /**
     * Gère l'autocomplétion des villes via l'API géographique.
     */
    private void autoComplete() {
        String cityValue = this.mainView.getCityInput().getText();

        if (cityValue.length() >= 3) {
            TaskAutoComplete.executeSearch(cityValue, cities -> {
                mainView.setCitiesSuggestions(cities);
            });
        } else {
            mainView.setCitiesSuggestions(List.of());
        }
    }

    /**
     * Lance la recherche de bateaux selon les critères saisis.
     * Effectue le scraping des sites sélectionnés en arrière-plan.
     */
    private void searchBoat() {
        this.mainView.setErrorMessage("");
        this.boatsInMemory.clear();

        LocalDate startDate = this.mainView.getStartDate();
        LocalDate endDate = this.mainView.getEndDate();

        if (startDate == null || endDate == null) {
            this.mainView.setErrorMessage("Veuillez choisir une date de début et une date de fin");
            return;
        }

        if (endDate.isBefore(startDate)) {
            this.mainView.setErrorMessage("La date de fin doit être supérieur à la date de début");
            return;
        }

        String cityValue = this.mainView.getCityInput().getText();
        if (cityValue == null || cityValue.isBlank()) {
            this.mainView.setErrorMessage("Veuillez choisir une ville");
            return;
        }

        String boatType = this.mainView.getBoatType();
        if (boatType == null) {
            this.mainView.setErrorMessage("Veuillez choisir une type de bateau");
            return;
        }

        BoatType type = BoatType.fromLibelle(this.mainView.getBoatType());

        if (!this.mainView.isSite1Selected() && !this.mainView.isSite2Selected() && !this.mainView.isSite3Selected()) {
            this.mainView.setErrorMessage("Veuillez selectionner un site");
            return;
        }

        // Afficher la barre de progression sur le thread JavaFX
        this.mainView.setIsVisibleProgressBlock(Boolean.TRUE);
        this.mainView.setProgress(0);

        // Lancer le traitement en arrière-plan
        Thread searchThread = new Thread(() -> {
            List<Boat> boats = new ArrayList<>();
            List<String> crouestyLocationLinks = new ArrayList<>();
            List<String> atlantiqueLocationLinks = new ArrayList<>();
            List<String> locVoileArmorLinks = new ArrayList<>();

            try {
                if (this.mainView.isSite1Selected()) {
                    crouestyLocationLinks = this.boatService.getCrouestyLocationBoatsLink(type, startDate, endDate);

                }
                if (this.mainView.isSite2Selected()) {
                    atlantiqueLocationLinks = this.boatService.getAtlantiqueLocationLinks(type);

                }
                if (this.mainView.isSite3Selected()) {
                    locVoileArmorLinks = this.boatService.getLocVoileArmorLinks(type);
                }

                double progressMax = 100.0;
                double progressPerLink = progressMax / (crouestyLocationLinks.size() + atlantiqueLocationLinks.size() + locVoileArmorLinks.size());
                double progress = 0;

                if (!crouestyLocationLinks.isEmpty()) {

                    for (String boatLink : crouestyLocationLinks) {
                        HashMap<String, String> boatData = CrouestyLocationScraper.getBoatData(type, boatLink);
                        Boat boat = this.boatService.createBoatFromApiData(boatData);
                        boats.add(boat);

                        progress += progressPerLink;
                        double finalProgress = progress;

                        javafx.application.Platform.runLater(() -> {
                            this.mainView.setProgress(finalProgress);
                        });
                    }
                }
                if (!atlantiqueLocationLinks.isEmpty()) {

                    for (String boatLink : atlantiqueLocationLinks) {
                        HashMap<String, String> boatData = AtlantiqueLocationScraper.getBoatData(type, boatLink);
                        Boat boat = this.boatService.createBoatFromApiData(boatData);
                        boats.add(boat);

                        progress += progressPerLink;
                        double finalProgress = progress;

                        javafx.application.Platform.runLater(() -> {
                            this.mainView.setProgress(finalProgress);
                        });
                    }
                }

                if (!locVoileArmorLinks.isEmpty()) {
                    for (String boatLink : locVoileArmorLinks) {
                        HashMap<String, String> boatData = LocVoileArmorScraper.getBoatData(type, boatLink);
                        Boat boat = this.boatService.createBoatFromApiData(boatData);
                        boats.add(boat);

                        progress += progressPerLink;
                        double finalProgress = progress;

                        javafx.application.Platform.runLater(() -> {
                            this.mainView.setProgress(finalProgress);
                        });
                    }
                }

                this.boatsInMemory.addAll(boats);


                javafx.application.Platform.runLater(() -> {
                    this.mainView.setIsVisibleProgressBlock(Boolean.FALSE);
                    this.mainView.displayBoats(boats);
                });

            } catch (IOException e) {
                javafx.application.Platform.runLater(() -> {
                    this.mainView.setIsVisibleProgressBlock(Boolean.FALSE);
                    this.mainView.setErrorMessage("Un problème est survenu lors de la recherche. Veuillez réessayer.");
                });
                e.printStackTrace();
            }
        });

        searchThread.setDaemon(true);
        searchThread.start();
    }

    /**
     * Génère un PDF des bateaux trouvés et ouvre une boîte de dialogue de sauvegarde.
     */
    private void savePdf() {
        if (this.boatsInMemory.isEmpty()) {
            this.mainView.setErrorMessage(searchErrorMessage);
            return;
        }

        PDFService.createPDFWithDialog(this.boatsInMemory, this.mainView.getMainWindow());
    }

    /**
     * Ouvre la fenêtre modale d'envoi de PDF par email.
     */
    private void openEmailPdfWindow() {

        if (this.boatsInMemory.isEmpty()) {
            this.mainView.setErrorMessage(searchErrorMessage);
            return;
        }

        try {
            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/cda/findboat/emailForPdf-view.fxml"));
            Parent root = loader.load();

            // Récupérer la vue depuis le loader
            EmailForPdfImpl view = loader.getController();

            // Créer le controller et le lier à la vue
            EmailForPdfController emailController = new EmailForPdfController(view);
            emailController.setListBoat(this.boatsInMemory);
            emailController.setupListeners();

            createStage("Envoyer par email", root);

        } catch (Exception e) {
            log.error("Erreur lors de l'ouverture de la fenêtre email", e);
            this.mainView.setErrorMessage(errorOpenWindow);
        }
    }

    /**
     * Ouvre la fenêtre modale de configuration de la base de données.
     */
    private void openDatabaseInfoWindow() {

        try {
            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/cda/findboat/databaseInfo-view.fxml"));
            Parent root = loader.load();

            // Récupérer la vue depuis le loader
            DatabaseInfoViewImpl databaseInfoViewImpl = loader.getController();

            // Créer le controller et le lier à la vue
            DatabaseInfoService databaseInfoService = new DatabaseInfoService();
            DatabaseInfoController databaseInfoController = new DatabaseInfoController(databaseInfoService, databaseInfoViewImpl);
            databaseInfoController.setupListeners();

            createStage("Information base de données", root);

        } catch (Exception e) {
            log.error("Erreur lors de l'ouverture de la fenêtre information BDD", e);
            this.mainView.setErrorMessage(errorOpenWindow);
        }
    }

    /**
     * Ouvre la fenêtre modale de sauvegarde des bateaux en base de données.
     */
    private void openSaveDataWindow() {

        if (this.boatsInMemory.isEmpty()) {
            this.mainView.setErrorMessage(searchErrorMessage);
            return;
        }

        try {
            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/cda/findboat/saveData-view.fxml"));
            Parent root = loader.load();

            // Récupérer la vue depuis le loader
            SaveDataViewImpl saveDataViewImpl = loader.getController();

            // Créer le controller et le lier à la vue
            TypeRepository typeRepository = new TypeRepositoryImpl();
            BoatRepository boatRepository = new BoatRepositoryImpl(typeRepository);
            SaveDataService saveDataService = new SaveDataService(boatRepository, typeRepository);
            SaveDataController saveDataController = new SaveDataController(saveDataService, saveDataViewImpl);
            saveDataController.setupListeners();
            saveDataController.setListBoat(this.boatsInMemory);

            createStage("Transmission BDD", root);

        } catch (Exception e) {
            log.error("Erreur lors de l'ouverture de la fenêtre information BDD", e);
            this.mainView.setErrorMessage(errorOpenWindow);
        }
    }

    /**
     * Crée et affiche une fenêtre modale.
     *
     * @param title le titre de la fenêtre
     * @param root le contenu de la fenêtre
     */
    private void createStage(String title, Parent root) {
        // Créer la fenêtre modale
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(mainView.getMainWindow());
        dialogStage.setScene(new Scene(root));
        dialogStage.setResizable(false);

        // Afficher la fenêtre
        dialogStage.show();
    }


}