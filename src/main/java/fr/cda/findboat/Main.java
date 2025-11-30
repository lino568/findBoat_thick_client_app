package fr.cda.findboat;

import fr.cda.findboat.controller.MainController;
import fr.cda.findboat.repository.BoatRepository;
import fr.cda.findboat.repository.BoatRepositoryImpl;
import fr.cda.findboat.repository.TypeRepository;
import fr.cda.findboat.repository.TypeRepositoryImpl;
import fr.cda.findboat.service.BoatService;
import fr.cda.findboat.task.TaskAutoComplete;
import fr.cda.findboat.view.MainViewImpl;
import fr.cda.findboat.view.MainViewInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        TypeRepository typeRepository = new TypeRepositoryImpl();
        BoatRepository boatRepository = new BoatRepositoryImpl(typeRepository);

        BoatService boatService = new BoatService();

        MainViewImpl mainView = fxmlLoader.getController();

        MainController mainController = new MainController();

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
