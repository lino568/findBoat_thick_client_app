package fr.cda.findboat.view;

import fr.cda.findboat.enums.BoatType;
import fr.cda.findboat.model.Boat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @FXML
    private Label errorMessage;
    @FXML
    private Pane progressBlock;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progress;
    @FXML
    private TableView<Boat> boatTable;
    @FXML
    private TableColumn<Boat, String> colPhoto;
    @FXML
    private TableColumn<Boat, String> colTitle;
    @FXML
    private TableColumn<Boat, String> colDescription;
    @FXML
    private TableColumn<Boat, Integer> colCapacity;
    @FXML
    private TableColumn<Boat, Double> colLength;
    @FXML
    private TableColumn<Boat, Integer> colYear;
    @FXML
    private MenuItem savePdf;
    @FXML
    private MenuItem sendEmail;
    @FXML
    private MenuItem bddInfo;
    @FXML
    private MenuItem saveBdd;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem info;

    private Popup popup;
    private ListView<String> listView;
    private ObservableList<Boat> boatsList = FXCollections.observableArrayList();

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

        List<String> boatTypes = new ArrayList<>();
        for (BoatType boatType : BoatType.values()) {
            boatTypes.add(boatType.getLibelle());
        }
        setBoatType(boatTypes);

        setTableColumn();

        this.boatTable.setOnMouseClicked(event -> {
            Boat boat = boatTable.getSelectionModel().getSelectedItem();
            if (boat != null) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(boat.getUrl()));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.exit.setOnAction(event -> closeWindow());
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
    public String getBoatType() {
        return this.boatType.getValue();
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
            double x = screenBounds.getMinX();
            double y = screenBounds.getMaxY();

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

    @Override
    public void setDeleteListener(EventHandler<ActionEvent> listener) {
        this.delete.setOnAction(listener);
    }

    @Override
    public void setSearchListener(EventHandler<ActionEvent> listener) {
        this.search.setOnAction(listener);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }

    @Override
    public void setProgress(double progress) {
        this.progressBar.setProgress(progress / 100);

        DecimalFormat df = new DecimalFormat("#.##");
        this.progress.setText(df.format(progress) + " %");
    }

    @Override
    public void setIsVisibleProgressBlock(boolean visible) {
        this.progressBlock.setVisible(visible);
    }

    @Override
    public void displayBoats(List<Boat> boats) {
        this.boatsList.clear();
        this.boatsList.addAll(boats);
    }

    @Override
    public void setCreatePdfListener(EventHandler<ActionEvent> listener) {
        this.savePdf.setOnAction(listener);
    }

    @Override
    public void getContextualWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public Window getMainWindow() {
        return savePdf.getParentPopup().getOwnerWindow();
    }

    @Override
    public void setOpenEmailPdfWindow(EventHandler<ActionEvent> listener) {
        this.sendEmail.setOnAction(listener);
    }

    @Override
    public void setOpenBddInfoWindow(EventHandler<ActionEvent> listener) {
        this.bddInfo.setOnAction(listener);
    }

    @Override
    public void setOpenSaveDataWindow(EventHandler<ActionEvent> listener) {
        this.saveBdd.setOnAction(listener);
    }

    @Override
    public void setOpenInfoWindow(EventHandler<ActionEvent> listener) {
        this.info.setOnAction(listener);
    }

    private void setTableColumn() {
        this.colPhoto.setCellValueFactory(new PropertyValueFactory<>("pictureUrl"));
        this.colPhoto.setCellFactory(col -> new TableCell<Boat, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String pictureUrl, boolean empty) {
                super.updateItem(pictureUrl, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Image image = new Image(pictureUrl, true);
                    imageView.setImage(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(80);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        this.colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        this.colLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        this.colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        this.boatTable.setItems(boatsList);
    }

    private void closeWindow() {
        Stage stage = (Stage) this.boatTable.getScene().getWindow();
        stage.close();
    }


}



