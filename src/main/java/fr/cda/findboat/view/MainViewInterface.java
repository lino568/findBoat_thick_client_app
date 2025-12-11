package fr.cda.findboat.view;

import fr.cda.findboat.model.Boat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public interface MainViewInterface {

    LocalDate getStartDate();
    LocalDate getEndDate();
    void setBoatType(List<String> boatType);
    String getBoatType();
    TextField getCityInput();
    void setCitiesSuggestions(List<String> citiesSuggestions);
    boolean isSite1Selected();
    boolean isSite2Selected();
    boolean isSite3Selected();
    void setDeleteListener(EventHandler<ActionEvent> listener);
    void setSearchListener(EventHandler<ActionEvent> listener);
    void setErrorMessage(String errorMessage);
    void setProgress(double progress);
    void setIsVisibleProgressBlock(boolean visible);
    void displayBoats(List<Boat> boats);
    void setCreatePdfListener(EventHandler<ActionEvent> listener);
    void getContextualWindow(String message);
    Window getMainWindow();
    void setOpenEmailPdfWindow(EventHandler<ActionEvent> listener);
    void setOpenBddInfoWindow(EventHandler<ActionEvent> listener);
    void setOpenSaveDataWindow(EventHandler<ActionEvent> listener);

}
