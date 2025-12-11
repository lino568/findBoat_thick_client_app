package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SaveDataViewImpl implements SaveDataViewInterface {

    @FXML
    private Button submit;
    @FXML
    private Button cancel;
    @FXML
    private Label errorMessage;

    @FXML
    public void initialize() {
        cancel.setOnAction(e -> closeWindow());
    }

    @Override
    public void setSaveListener(EventHandler<ActionEvent> listener) {
        this.submit.setOnAction(listener);
    }

    @Override
    public void setErrorMessage(String message) {
        this.errorMessage.setText(message);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
