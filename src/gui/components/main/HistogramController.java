package gui.components.main;

import UI.ValidationException;
import gui.common.HistogramsUtils;
import gui.components.singlehistogram.SingleWordController;
import gui.common.HistogramResourcesConstants;
import gui.logic.BusinessLogic;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistogramController {

    @FXML FlowPane histogramFlowPane;
    @FXML Label totalWordsLabel;
    @FXML Button collectMetadataButton;
    @FXML Button calculateHistogramButton;
    @FXML ProgressBar taskProgressBar;
    @FXML Label distinctWordsSoFar;
    @FXML Label taskMessageLabel;
    @FXML Label progressPercentLabel;
    @FXML Label selectedFileName;
    @FXML Button openFileButton;
    @FXML Button clearButton;
    @FXML Button clearTaskButton;
    @FXML Button stopTaskButton;
    @FXML Label totalCurrentProcessedWords;

    private SimpleLongProperty totalWords;
    private SimpleLongProperty totalLines;
    private SimpleIntegerProperty totalDistinctWords;
    private SimpleIntegerProperty totalProcessedWords;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private SimpleBooleanProperty isMetadataCollected;

    private BusinessLogic businessLogic;
    private Stage primaryStage;
    private Map<String, SingleWordController> wordToTileController;


    public HistogramController() {
        totalWords = new SimpleLongProperty(0);
        totalLines = new SimpleLongProperty(0);
        totalDistinctWords = new SimpleIntegerProperty(0);
        totalProcessedWords = new SimpleIntegerProperty(0);
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        isMetadataCollected = new SimpleBooleanProperty(false);
        wordToTileController = new HashMap<>();
    }

    public void setBusinessLogic(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
        businessLogic.fileNameProperty().bind(selectedFileProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        totalWordsLabel.textProperty().bind(Bindings.format("%,d", totalWords));
        distinctWordsSoFar.textProperty().bind(Bindings.format("%,d", totalDistinctWords));
        totalCurrentProcessedWords.textProperty().bind(Bindings.format("%,d", totalProcessedWords));
        selectedFileName.textProperty().bind(selectedFileProperty);
        collectMetadataButton.disableProperty().bind(isFileSelected.not());
        calculateHistogramButton.disableProperty().bind(isMetadataCollected.not());
    }

    @FXML
    public void calculateHistogramAction() {
        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();

        toggleTaskButtons(true);

        businessLogic.calculateHistogram(uiAdapter, () -> toggleTaskButtons(false));
    }

    @FXML
    public void collectMetadataAction() {
        toggleTaskButtons(true);

        businessLogic.collectMetadata(
                totalWords::set,
                totalLines::set,
                () -> {
                    isMetadataCollected.set(true);
                    toggleTaskButtons(false);
                }
        );
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
            businessLogic.loadXmlFile(absolutePath);
            selectedFileProperty.set(absolutePath);
            isFileSelected.set(true);
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid XML");
            alert.setHeaderText("Looks like the xml is invalid.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    public void clearButtonAction() {
        selectedFileProperty.set("");
        isFileSelected.set(false);
        isMetadataCollected.set(false);
        totalWords.set(0);
        totalLines.set(0);
        clearTaskButtonAction();
        cleanOldResults();
    }

    @FXML
    public void clearTaskButtonAction() {
        taskMessageLabel.setText("");
        progressPercentLabel.setText("");
        taskProgressBar.setProgress(0);
    }

    @FXML
    public void stopTaskButtonAction() {
        businessLogic.cancelCurrentTask();
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

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
        this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                histogramData -> {
                    HistogramsUtils.log("EDT: CREATE new tile for [" + histogramData.toString() + "]");
                    createTile(histogramData.getWord(), histogramData.getCount());
                },
                histogramData -> {
                    HistogramsUtils.log("EDT: UPDATE tile for [" + histogramData.toString() + "]");
                    SingleWordController singleWordController = wordToTileController.get(histogramData.getWord());
                    if (singleWordController != null && singleWordController.getCount() != histogramData.getCount()) {
                        singleWordController.setCount(histogramData.getCount());
                    } else {
                        HistogramsUtils.log("ERROR ! Can't find tile for [" + histogramData.getWord() + "] with count " + histogramData.getCount());
                    }
                },
                () -> {
                    HistogramsUtils.log("EDT: INCREASE total distinct words");
                    totalDistinctWords.set(totalDistinctWords.get() + 1);
                },
                (delta) -> {
                    HistogramsUtils.log("EDT: INCREASE total processed words");
                    totalProcessedWords.set(totalProcessedWords.get() + delta);
                }
        );
    }

    private void cleanOldResults() {
        wordToTileController.clear();
        histogramFlowPane.getChildren().clear();
        taskProgressBar.setProgress(0);
        totalDistinctWords.set(0);
        totalProcessedWords.set(0);
    }

    private void createTile(String word, int count) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HistogramResourcesConstants.MAIN_FXML_RESOURCE);
            Node singleWordTile = loader.load();

            SingleWordController singleWordController = loader.getController();
            singleWordController.setCount(count);
            singleWordController.setWord(word);

            histogramFlowPane.getChildren().add(singleWordTile);
            wordToTileController.put(word, singleWordController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleTaskButtons(boolean isActive) {
        stopTaskButton.setDisable(!isActive);
        clearTaskButton.setDisable(isActive);
    }
}