package fr.cda.findboat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface SaveDataViewInterface {

    void setSaveListener(EventHandler<ActionEvent> listener);
    void setErrorMessage(String message);
    void closeWindow();

}
