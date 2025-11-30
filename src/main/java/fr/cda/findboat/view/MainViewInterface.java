package fr.cda.findboat.view;

import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public interface MainViewInterface {

    LocalDate getStartDate();
    LocalDate getEndDate();
    void setBoatType(List<String> boatType);
    TextField getCityInput();
    void setCitiesSuggestions(List<String> citiesSuggestions);
    boolean isSite1Selected();
    boolean isSite2Selected();
    boolean isSite3Selected();

}
