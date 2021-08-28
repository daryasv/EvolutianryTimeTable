package gui.components.main;

import UI.ValidationException;
import gui.logic.EngineLogic;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EttController {


//    @FXML Button openFileButton;
//    @FXML Button clearButton;
    @FXML Label filePathLabel;

    private SimpleStringProperty selectedFileProperty;



    private EngineLogic engineLogic;
    private Stage primaryStage;


    public EttController() {
        //initialize
        selectedFileProperty = new SimpleStringProperty();

    }

    public void setEngineLogic(EngineLogic engineLogic) {
        this.engineLogic = engineLogic;
        engineLogic.fileNameProperty().bind(selectedFileProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(selectedFileProperty);
    }

    @FXML
    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        //todo:
        //check file validations in basic logic
        //get if valid or not and message
        //if ok, do the following lines
        String absolutePath = selectedFile.getAbsolutePath();
        try {
            engineLogic.loadXmlFile(absolutePath);
            selectedFileProperty.set(absolutePath);
//            isFileSelected.set(true);
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid XML");
            alert.setHeaderText("Looks like the xml is invalid.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void clearButtonAction(){

    }

    @FXML
    public void showXmlSettingsButtonAction(){

    }
}