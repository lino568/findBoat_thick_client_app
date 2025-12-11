package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DatabaseInfoViewImpl implements DatabaseInfoViewInterface {

    @FXML
    private TextField host;
    @FXML
    private TextField dbName;
    @FXML
    private TextField port;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
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
    public String getHost() {
        return this.host.getText();
    }

    @Override
    public String getDbName() {
        return this.dbName.getText();
    }

    @Override
    public String getPort() {
        return this.port.getText();
    }

    @Override
    public String getLogin() {
        return this.login.getText();
    }

    @Override
    public String getPassword() {
        return this.password.getText();
    }

    @Override
    public void submit(EventHandler<ActionEvent> listener) {
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
