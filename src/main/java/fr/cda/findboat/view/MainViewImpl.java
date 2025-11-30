package fr.cda.findboat.view;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.stage.Popup;

import java.time.LocalDate;
import java.util.List;

public class MainViewImpl implements MainViewInterface {

    @FXML
    private ComboBox<String> boatType;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField locationField;

    @FXML
    private Button delete;

    @FXML
    private Button search;

    @FXML
    private CheckBox site1;

    @FXML
    private CheckBox site2;

    @FXML
    private CheckBox site3;

    private Popup popup;
    private ListView<String> listView;

    public MainViewImpl() {
    }
    @FXML
    private void initialize() {
        popup = new Popup();
        popup.setAutoHide(true);

        listView = new ListView<>();
        listView.setPrefHeight(200);

        listView.setOnMouseClicked(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                locationField.setText(selected);
                popup.hide();
            }
        });

        popup.getContent().add(listView);
    }

    @Override
    public LocalDate getStartDate() {
        return this.startDate.getValue();
    }

    @Override
    public LocalDate getEndDate() {
        return this.endDate.getValue();
    }

    @Override
    public void setBoatType(List<String> boatType) {
        this.boatType.getItems().addAll(boatType);
    }

    @Override
    public TextField getCityInput() {
        return this.locationField;
    }

    @Override
    public void setCitiesSuggestions(List<String> cities) {
        listView.getItems().clear();

        if (cities == null || cities.isEmpty() || locationField.getText().length() < 3) {
            popup.hide();
            return;
        }

        listView.getItems().addAll(cities);

        if (!popup.isShowing()) {
            // 1. Obtenir coordonnées locales
            Bounds localBounds = locationField.getBoundsInLocal();

            // 2. Convertir en coordonnées écran
            Bounds screenBounds = locationField.localToScreen(localBounds);

            // 3. Extraire les positions
            double x = screenBounds.getMinX();  // Gauche du TextField
            double y = screenBounds.getMaxY();  // Bas du TextField

            // 4. Afficher le popup
            popup.show(locationField.getScene().getWindow(), x, y);
        }

        listView.setPrefWidth(locationField.getWidth());
    }

    @Override
    public boolean isSite1Selected() {
        return this.site1.isSelected();
    }

    @Override
    public boolean isSite2Selected() {
        return this.site2.isSelected();
    }

    @Override
    public boolean isSite3Selected() {
        return this.site3.isSelected();
    }
}



