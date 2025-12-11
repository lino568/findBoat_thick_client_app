package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface EmailForPdfViewInterface {

    void setErrorMessage(String message);
    String getEmail();
    void setSendEmailListener(EventHandler<ActionEvent> listener);
    void closeWindow();
}
