package fr.cda.findboat.controller;

import fr.cda.findboat.api.AutoCompleteAPI;
import fr.cda.findboat.helper.Util;
import fr.cda.findboat.task.TaskAutoComplete;
import fr.cda.findboat.view.MainViewInterface;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class MainController {

    private MainViewInterface mainView;

    public void setMainView(MainViewInterface mainView) {
        this.mainView = mainView;
    }

    public void listeners() {
        mainView.getCityInput().addEventFilter(KeyEvent.KEY_RELEASED, e -> autoComplete());
    }

    public void autoComplete() {
        String cityValue = this.mainView.getCityInput().getText();

        if (cityValue.length() >= 3) {
            TaskAutoComplete.executeSearch(cityValue, cities -> {
                mainView.setCitiesSuggestions(cities);
            });
        } else {
            mainView.setCitiesSuggestions(List.of());
        }
    }
}
