package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.event.ActionListener;

public interface DatabaseInfoViewInterface {

    String getHost();
    String getDbName();
    String getPort();
    String getLogin();
    String getPassword();
    void submit(EventHandler<ActionEvent> listener);
    void setErrorMessage(String message);
    void closeWindow();
}
