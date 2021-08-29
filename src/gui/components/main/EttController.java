package gui.components.main;

import UI.ValidationException;
import gui.common.Utils;
import gui.logic.EngineLogic;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.util.Optional;

public class EttController {


//    @FXML Button openFileButton;
//    @FXML Button clearButton;
    @FXML Label filePathLabel;
    @FXML ChoiceBox<String> endConditionBox;
    @FXML TextField endConditionLimitTextField;
    @FXML TextField generationsJumpTextField;
    @FXML Button runEvolutionaryButton;
    @FXML ProgressBar taskProgressBar;
    @FXML Label progressPercentLabel;

    private SimpleStringProperty selectedFileProperty;
    public static final ObservableList<String> endConditions = FXCollections.observableArrayList("Generations","Fitness","Time");
    private SimpleBooleanProperty isEndConditionSelected;
    private SimpleIntegerProperty generationsJump;

    private EngineLogic engineLogic;
    private Stage primaryStage;

    public EttController() {
        //initialize
        selectedFileProperty = new SimpleStringProperty();
        isEndConditionSelected = new SimpleBooleanProperty(false);
        generationsJump = new SimpleIntegerProperty();
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
        endConditionBox.setItems(endConditions);
        endConditionLimitTextField.disableProperty().bind(isEndConditionSelected.not());
        generationsJumpTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    String value = newValue.replaceAll("[^\\d]", "");
                    generationsJumpTextField.setText(value);
                }
            }
        });
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
            ShowError(e.getMessage());
        }
    }

    @FXML
    public void clearButtonAction(){

    }

    @FXML
    public void showXmlSettingsButtonAction(){

    }

    @FXML
    public void onEndConditionSelect(){
        System.out.println(endConditionBox.getValue());
        isEndConditionSelected.set(true);
    }


    @FXML
    public void runEvolutionaryButton(){
        String endCondition = endConditionBox.getValue();
        if(endConditionBox.getValue() == null){
            ShowError("Please choose end condition");
            return;
        }
        Integer limit = Utils.tryParse(endConditionLimitTextField.textProperty().getValue());
        if(limit == null){
            ShowError("Please add end condition limit");
            return;
        }
        Integer interval = Utils.tryParse(generationsJumpTextField.textProperty().getValue());
        if(interval == null){
            ShowError("Invalid interval time");
            return;
        }

        engineLogic.runEvolutionary(endCondition,limit,interval, () -> {

        });


    }

    private void ShowError(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        //taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        //this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }
}