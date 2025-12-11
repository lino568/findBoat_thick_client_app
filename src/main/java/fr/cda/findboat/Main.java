package fr.cda.findboat;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.cda.findboat.controller.EmailForPdfController;
import fr.cda.findboat.controller.MainController;
import fr.cda.findboat.repository.BoatRepositoryImpl;
import fr.cda.findboat.repository.TypeRepository;
import fr.cda.findboat.repository.TypeRepositoryImpl;
import fr.cda.findboat.service.BoatService;
import fr.cda.findboat.service.DatabaseInfoService;
import fr.cda.findboat.task.TaskAutoComplete;
import fr.cda.findboat.view.EmailForPdfImpl;
import fr.cda.findboat.view.EmailForPdfViewInterface;
import fr.cda.findboat.view.MainViewImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        TypeRepository typeRepository = new TypeRepositoryImpl();
        new BoatRepositoryImpl(typeRepository);

        BoatService boatService = new BoatService();

        MainViewImpl mainView = fxmlLoader.getController();
        EmailForPdfViewInterface emailForPdfView = new EmailForPdfImpl();

        new EmailForPdfController(emailForPdfView);
        MainController mainController = new MainController(boatService);

        mainController.setMainView(mainView);
        mainController.listeners();

        Scene scene = new Scene(root, 1600, 900);

        stage.setTitle("Find'Boat");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        TaskAutoComplete.shutdown();
    }
}
