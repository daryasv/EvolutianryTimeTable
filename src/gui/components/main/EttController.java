package gui.components.main;

import UI.ValidationException;
import UI.models.Lesson;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.Grade;
import UI.models.timeTable.Teacher;
import UI.models.timeTable.TimeTableMembers;
import engine.models.IRule;
import engine.models.Solution;
import gui.common.EttResourcesConstants;
import gui.common.Utils;
import gui.components.table.TableController;
import gui.logic.BusinessLogic;
import gui.logic.EngineLogic;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.*;
import java.io.IOException;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class EttController {

    @FXML Pane evolutionSettingsPane;
    @FXML Pane evolutionProgressPane;
    @FXML Pane evolutionConditionsPane;
    @FXML TitledPane timeTableSettingsTPane;

    @FXML Button openXmlBtn;
    @FXML Button clearXmlBtn;
    @FXML Button runEvolutionBtn;

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
    @FXML ProgressBar taskProgressBar;
    @FXML Label progressPercentLabel;
    @FXML Button stopBtn;
    @FXML Label currentGenerationLabel;
    @FXML Label bestFitnessLabel;

    //dar's addition
    @FXML private MenuButton idsMenu;
    @FXML private Button showSolutionBtn;
    @FXML private VBox solutionViewVbox;
    @FXML private ScrollPane tableScrollPane;
    @FXML private ScrollPane RulesDetailsScrollPane;
    @FXML private RadioButton rawRadioBtn;
    @FXML private ToggleGroup viewSolutionOption;
    @FXML private RadioButton teacherViewRadioBtn;
    @FXML private RadioButton classViewRadioBtn;



    private SimpleStringProperty timeTableSettings;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isEndConditionSelected;
    private SimpleBooleanProperty isFileLoaded;
    private SimpleIntegerProperty currentGenerationProperty;
    private SimpleDoubleProperty bestFitnessProperty;

    private  SimpleBooleanProperty isEvolutionRunning;
    private SimpleBooleanProperty isPaused;
    private SimpleBooleanProperty isCrossoverAspectOriented;
    private SimpleBooleanProperty isTableViewSelected;

    private SimpleBooleanProperty isViewTypeSelected;

    public static final ObservableList<String> endConditions = FXCollections.observableArrayList("Generations","Fitness","Time");
    public static final ObservableList<String> selectionMethods = FXCollections.observableArrayList("Truncation","Roulete Wheel");
    public static final ObservableList<String> crossoverMethods = FXCollections.observableArrayList("DayTimeOriented","AspectOreiented");
    public static final ObservableList<String> crossoverOrientations = FXCollections.observableArrayList("Teacher","Grade");
    public static final ObservableList<String> mutationComponents = FXCollections.observableArrayList("D","H", "T", "S", "G");

    private EngineLogic engineLogic;
    private BusinessLogic businessLogic;
    private Stage primaryStage;

    public EttController() {
        //initialize
        selectedFileProperty = new SimpleStringProperty();
        timeTableSettings = new SimpleStringProperty();

        isEndConditionSelected = new SimpleBooleanProperty(false);
        isFileLoaded = new SimpleBooleanProperty(false);
        isEvolutionRunning = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);
        isCrossoverAspectOriented = new SimpleBooleanProperty(false);
        isTableViewSelected= new SimpleBooleanProperty(false);
        currentGenerationProperty = new SimpleIntegerProperty(0);
        bestFitnessProperty = new SimpleDoubleProperty(0);
    }

    public void setEngineLogic(EngineLogic engineLogic) {
        this.engineLogic = engineLogic;
        engineLogic.fileNameProperty().bind(selectedFileProperty);
        engineLogic.getIsPaused().bind(isPaused);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        evolutionSettingsPane.disableProperty().bind(isFileLoaded.not());
        timeTableSettingsTPane.disableProperty().bind(isFileLoaded.not());
        clearXmlBtn.disableProperty().bind(isFileLoaded.not());
        evolutionProgressPane.disableProperty().bind(isFileLoaded.not());
        evolutionConditionsPane.disableProperty().bind(isFileLoaded.not());
        idsMenu.disableProperty().bind(isTableViewSelected.not());
        currentGenerationLabel.textProperty().bind(currentGenerationProperty.asString());
        bestFitnessLabel.textProperty().bind(bestFitnessProperty.asString("%.1f"));

        filePathLabel.textProperty().bind(selectedFileProperty);
        endConditionBox.setItems(endConditions);
        endConditionLimitTextField.disableProperty().bind(isEndConditionSelected.not());
        //todo: load initial selection fron xml + disable until pause btn pushed (for the next 5 lines)
        selectionCBox.setItems(selectionMethods);
        crossoverCBox.setItems(crossoverMethods);
        crossoverOrientationCBox.setItems(crossoverOrientations);
        //todo: create mutations only if needed (can be q or 2 or 0)
        mutation1ComponentCBox.setItems(mutationComponents);
        mutation2ComponentCBox.setItems(mutationComponents);

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

        stopBtn.disableProperty().bind(isEvolutionRunning.not().and(isPaused.not()));
        isEvolutionRunning.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                runEvolutionBtn.setText("Pause");
            }
            else if(isPaused.getValue()) {
                runEvolutionBtn.setText("Resume");
            }else{
                runEvolutionBtn.setText("Start");
            }
        });

        showSolutionBtn.disableProperty().bind(isEvolutionRunning.or(isPaused));
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
            setEvolutionConfig(engineLogic.getEvolutionEngineDataSet());
            isFileLoaded.set(true);
        } catch (ValidationException e) {
            ShowError(e.getMessage());
        }
    }

    //todo complete
    private void setEvolutionConfig(EvolutionConfig evolutionConfig)
    {
        initPopulationTextField.textProperty().set(String.valueOf(evolutionConfig.getInitialPopulation()));
        selectionCBox.setValue(evolutionConfig.getSelection().getType().name);
        //elitismSizeTextFiled.textProperty().set(String.valueOf(evolutionConfig.)); //todo: add to evolution config get elitism
        crossoverCBox.setValue(evolutionConfig.getCrossover().getName().name);
        cuttingPointsTextFiled.textProperty().set(String.valueOf(evolutionConfig.getCrossover().getCuttingPoints()));
        if(Objects.equals(crossoverCBox.getValue(), "AspectOreiented"))
        {
            isCrossoverAspectOriented.set(true);
            crossoverOrientationCBox.setValue(evolutionConfig.getCrossover().getSortOperator());
        }
        else
        {
            isCrossoverAspectOriented.set(false);
            crossoverOrientationCBox.disableProperty().bind(isCrossoverAspectOriented.not());
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
        if(!isEvolutionRunning.getValue() || isPaused.getValue()){
            runEvolution();
            RulesDetailsScrollPane.setVisible(false);
            solutionViewVbox.setVisible(false);
        }else{
            pauseEvolution();
        }
    }

    private void runEvolution(){
        String endCondition = endConditionBox.getValue();
        if(endConditionBox.getValue() == null){
            ShowError("Please choose end condition");
            return;
        }
        Double limit = Utils.tryParseDouble(endConditionLimitTextField.textProperty().getValue());
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
            isEvolutionRunning.set(false);
        });

        isEvolutionRunning.set(true);
        isPaused.set(false);
    }

    private void pauseEvolution()
    {
        engineLogic.stop();
        isPaused.set(true);
        isEvolutionRunning.set(false);
    }

    @FXML
    public void stopEvolution()
    {
        engineLogic.stop();
        isPaused.set(false);
        isEvolutionRunning.set(false);
        runEvolutionBtn.setText("Start");
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

    @FXML
    void showBestSolution() {
        solutionViewVbox.setVisible(true);
        RulesDetailsScrollPane.setVisible(true);
        showRulesDetails();
        showRawSolution();
    }

    void IDSelected(MenuItem chosenId, String tableType, int totalDays, int totalHours){
        chosenId.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                idsMenu.setText(String.format("%s:%s", tableType, chosenId.getText()));
               createTable(tableType, chosenId.getText(),totalDays,totalHours);
            }
        });
    }
    @FXML
    void teacherSolutionSelected() {
        isTableViewSelected.setValue(true);
        int totalDays=engineLogic.getTimeTableDataSet().getTimeTableMembers().getDays();
        int totalHours=engineLogic.getTimeTableDataSet().getTimeTableMembers().getHours();
        String tableType="Teacher";
        HashMap<Integer, Teacher> teachers =engineLogic.getTimeTableDataSet().getTimeTableMembers().getTeachers();
        idsMenu.getItems().clear();
        for (Integer teacherID : teachers.keySet()){
            MenuItem menuItem = new MenuItem(teacherID.toString());
            IDSelected(menuItem, tableType, totalDays, totalHours);
            idsMenu.getItems().add(menuItem);
        }
        idsMenu.setText(String.format("%s:%s", tableType,"1"));
        createTable(tableType, "1",totalDays,totalHours);
    }

    @FXML
    void rawSolutionSelected(ActionEvent event) {
        isTableViewSelected.setValue(false);
        showRawSolution();
    }

    @FXML
    void classSolutionSelected() {
        isTableViewSelected.setValue(true);
        String tableType="Class";
        int totalDays=engineLogic.getTimeTableDataSet().getTimeTableMembers().getDays();
        int totalHours=engineLogic.getTimeTableDataSet().getTimeTableMembers().getHours();
        HashMap<Integer, Grade> grades = engineLogic.getTimeTableDataSet().getTimeTableMembers().getGrades();
        idsMenu.getItems().clear();
        for (Integer classID : grades.keySet()){
            MenuItem menuItem = new MenuItem(classID.toString());
            IDSelected(menuItem, tableType, totalDays,totalHours);
            idsMenu.getItems().add(menuItem);
        }
        idsMenu.setText(String.format("%s:%s", tableType,"1"));
        createTable(tableType, "1",totalDays,totalHours);
    }


    private void showRawSolution() {
        VBox vbox = new VBox();
        tableScrollPane.setContent(vbox);
        Solution<Lesson> timeTableSolution = engineLogic.getGlobalBestSolution().getSolution();
        for (int i = 0; i < timeTableSolution.getList().size(); i++) {
            int classId = timeTableSolution.getList().get(i).getClassId();
            int teacher = timeTableSolution.getList().get(i).getTeacherId();
            int subject = timeTableSolution.getList().get(i).getSubjectId();
            int day = timeTableSolution.getList().get(i).getDay();
            int hour = timeTableSolution.getList().get(i).getHour();
            if (teacher != -1 && subject != -1) {
                String detailsInfo = String.format("Day:%d, hour:%d, classID:%d, teacherID:%d, subject:%d", day, hour, classId, teacher, subject);
                Label detailsLabel = new Label(detailsInfo);
                vbox.getChildren().add(detailsLabel);
            }
        }
    }
    private void showRulesDetails(){
        VBox RulesDetailsVbox = new VBox();
        RulesDetailsScrollPane.setContent(RulesDetailsVbox);
        HashMap<IRule, Double> rulesFitness = engineLogic.getGlobalBestSolution().getRulesFitness();
        Label title = new Label("Rules Details:");
        RulesDetailsVbox.getChildren().add(title);
        RulesDetailsVbox.getChildren().add(new Label(""));
        Label ruleTypeLabel;
        for (Map.Entry<IRule, Double> entry : rulesFitness.entrySet()){
            Label ruleNameLabel = new Label(String.format("Rule: %s ", entry.getKey().getName()));
            if(entry.getKey().isHard()){
                ruleTypeLabel = new Label("Rule type: hard");
            }
            else{
                ruleTypeLabel = new Label("Rule type: soft");
            }
            Label ruleGrade = new Label(String.format("Rule grade: %,.1f ", entry.getValue()));
            RulesDetailsVbox.getChildren().add(ruleNameLabel);
            RulesDetailsVbox.getChildren().add(ruleTypeLabel);
            RulesDetailsVbox.getChildren().add(ruleGrade);
            RulesDetailsVbox.getChildren().add(new Label(""));
        }
    }

    private void createTable(String typeTitle, String typeIdTitle, int totalDays, int totalHours) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EttResourcesConstants.TABLE_FXML_RESOURCE);
            Node table = loader.load();
            TableController tableController = loader.getController();

            Solution<Lesson> bestSolution= engineLogic.getGlobalBestSolution().getSolution();
            TimeTableMembers solMembersDetails= engineLogic.getTimeTableDataSet().getTimeTableMembers();
            //add to scroll pane
            tableScrollPane.setContent(table);
            tableController.showTable(typeTitle,Integer.parseInt(typeIdTitle), bestSolution, totalDays, totalHours,engineLogic.getGlobalBestSolution(), solMembersDetails);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        //this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    @FXML
    public void showCurrentData(){
        engineLogic.getBestSolutionFitness(bestFitnessProperty::set);
        engineLogic.getCurrentGeneration(currentGenerationProperty::set);
    }
}