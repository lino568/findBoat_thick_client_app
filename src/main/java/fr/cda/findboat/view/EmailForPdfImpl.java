package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmailForPdfImpl implements EmailForPdfViewInterface{

    @FXML
    private TextField email;
    @FXML
    private Label errorMessage;
    @FXML
    private Button sendEmail;
    @FXML
    private Button cancel;

    @FXML
    public void initialize(){
        this.cancel.setOnAction(e -> closeWindow());
    }

    @Override
    public void setErrorMessage(String message) {
        this.errorMessage.setText(message);
    }

    @Override
    public String getEmail() {
        return email.getText();
    }

    @Override
    public void setSendEmailListener(EventHandler<ActionEvent> listener) {
        this.sendEmail.setOnAction(listener);
    }

    public void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }
}
