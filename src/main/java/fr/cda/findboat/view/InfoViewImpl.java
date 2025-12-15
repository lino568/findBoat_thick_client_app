package fr.cda.findboat.view;

import fr.cda.findboat.helper.InfoContent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class InfoViewImpl implements  InfoViewInterface{

    @FXML
    private Button close;
    @FXML
    private TextArea infoContent;

    @FXML
    public void initialize(){
        this.close.setOnAction(e -> closeWindow());
        this.infoContent.setText(InfoContent.INFO_CONTENT);
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
