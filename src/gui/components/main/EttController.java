package gui.components.main;

import UI.ValidationException;
import gui.common.Utils;
import gui.logic.EngineLogic;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
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

import java.io.File;
import java.util.Optional;

public class EttController {


    @FXML Button openXmlBtn;
    @FXML Button clearXmlBtn;
    @FXML Label filePathLabel;
    @FXML TextArea timeTableSettingsTextArea;

    @FXML TextField initPopulationTextField;
    @FXML ChoiceBox<String> selectionCBox;
    @FXML TextField elitismSizeTextFiled;
    @FXML ChoiceBox<String> crossoverCBox;
    @FXML TextField cuttingPointsTextFiled;
    @FXML ChoiceBox<String> crossoverOrientationCBox;

    @FXML Label mutation1NameLabel;
    @FXML TextField probability1TextField;
    @FXML TextField maxTupples1TextField;
    @FXML ChoiceBox<String> mutation1ComponentCBox;
    @FXML Label mutation2NameLabel;
    @FXML TextField probability2TextField;
    @FXML TextField maxTupples2TextField;
    @FXML ChoiceBox<String> mutation2ComponentCBox;

    @FXML ChoiceBox<String> endConditionBox;
    @FXML TextField endConditionLimitTextField;
    @FXML TextField generationsJumpTextField;
    @FXML Button runEvolutionaryButton;
    @FXML ProgressBar taskProgressBar;
    @FXML Label progressPercentLabel;
    @FXML Button pauseBtn;

    private SimpleStringProperty timeTableSettings;
    private SimpleStringProperty selectedFileProperty;
    public static final ObservableList<String> endConditions = FXCollections.observableArrayList("Generations","Fitness","Time");
    private SimpleBooleanProperty isEndConditionSelected;
    private SimpleIntegerProperty generationsJump;

    public static final ObservableList<String> selectionMethod = FXCollections.observableArrayList("Truncation","Roulete Wheel");
    public static final ObservableList<String> crossoverMethod = FXCollections.observableArrayList("Day Time Oriented","Aspect Oreiented");
    public static final ObservableList<String> crossoverOrientation = FXCollections.observableArrayList("Teacher","Grade");
    public static final ObservableList<String> mutationComponent = FXCollections.observableArrayList("D","H", "T", "S", "G");

    private EngineLogic engineLogic;
    private Stage primaryStage;

    public EttController() {
        //initialize
        selectedFileProperty = new SimpleStringProperty();
        isEndConditionSelected = new SimpleBooleanProperty(false);
        generationsJump = new SimpleIntegerProperty();
        timeTableSettings = new SimpleStringProperty();
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
        //todo: load initial selection fron xml + disable until pause btn pushed (for the next 5 lines)
        selectionCBox.setItems(selectionMethod);
        crossoverCBox.setItems(crossoverMethod);
        crossoverOrientationCBox.setItems(crossoverOrientation);
        //todo: create mutations only if needed (can be q or 2 or 0)
        mutation1ComponentCBox.setItems(mutationComponent);
        mutation2ComponentCBox.setItems(mutationComponent);

        timeTableSettingsTextArea.textProperty().bind(timeTableSettings);
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
            engineLogic.printTimeTableXmlSettings(timeTableSettings::set);
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

    @FXML
    public void onEndConditionSelect(){
        System.out.println(endConditionBox.getValue());
        isEndConditionSelected.set(true);
    }


    @FXML
    public void runEvolutionaryButton(){
        Integer interval = Utils.tryParse(generationsJumpTextField.textProperty().getValue());
        if(interval != null){
            //todo: get generations num
            engineLogic.runEvolutionary(1000,interval);
        }

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