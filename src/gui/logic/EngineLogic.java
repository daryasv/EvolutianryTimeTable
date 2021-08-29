package gui.logic;

import UI.ProgramManager;
import UI.ValidationException;
import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.Grade;
import UI.models.timeTable.Rule;
import UI.models.timeTable.Subject;
import UI.models.timeTable.Teacher;
import engine.Evolutionary;
import engine.models.SolutionFitness;
import gui.components.main.EttController;
import gui.components.main.HistogramController;
import gui.components.main.UIAdapter;
import gui.logic.tasks.histogram.CalculateHistogramsTask;
import gui.logic.tasks.metadata.CollectMetadataTask;
import gui.tasks.evolutinary.RunEvolutionaryTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EngineLogic {
    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    SolutionFitness<Lesson> globalBestSolution;
    List<SolutionFitness<Lesson>> bestSolutions;

    private SimpleStringProperty fileName;
    private EttController controller;

    //todo: remove
    private long totalWords;
    private Task<Boolean> currentRunningTask;


    public EngineLogic(EttController controller) {
        this.fileName = new SimpleStringProperty();
        this.controller = controller;
        totalWords = -1;
    }

    public SimpleStringProperty fileNameProperty() {
        return this.fileName;
    }

    public void loadXmlFile(String absolutePath) throws ValidationException {
        try{
            //load xml file into ETT classes
            File file = new File(absolutePath);
            if(!file.exists()){
                throw new ValidationException("File not exists");
            }
            if(!absolutePath.endsWith(".xml")){
                throw new ValidationException("File not xml");
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
            updateDataSets(descriptor);
            //ProgramManager.systemSetting.IS_FILE_LOADED.status=true;
            System.out.println("File Loaded Successfully!\n");
        } catch (JAXBException e) {
            //ProgramManager.systemSetting.IS_FILE_LOADED.status=false;
            System.out.println("Failed to parse xml");
        }
    }

    private void updateDataSets(ETTDescriptor descriptor) throws ValidationException {
        timeTable = new TimeTableDataSet(descriptor);
        evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }

    public void runEvolutionary(int generations,int interval) {
        RunEvolutionaryTask runEvolutionaryTask = new RunEvolutionaryTask(timeTable,evolutionEngineDataSet,generations,interval);
        //controller.bindTaskToUIComponents(runEvolutionaryTask, );

        new Thread(runEvolutionaryTask).start();

    }

    //todo
    public void printTimeTableXmlSettings(Consumer<String> timeTableSettingsDelegate) {
        StringBuilder sbXmlEttSettings = new StringBuilder();
        HashMap<Integer, Subject> subjects = timeTable.getTimeTableMembers().getSubjects();
        HashMap<Integer, Teacher> teachers = timeTable.getTimeTableMembers().getTeachers();
        HashMap<Integer, Grade> grades = timeTable.getTimeTableMembers().getGrades();
        List<Rule> rules = timeTable.getTimeTableMembers().getRules();

        sbXmlEttSettings.append(toStringSubjects(subjects));
        sbXmlEttSettings.append("\n");
        sbXmlEttSettings.append(toStringTeachers(teachers, subjects));
        sbXmlEttSettings.append("\n");
        sbXmlEttSettings.append(toStringGrades(grades, subjects));
        sbXmlEttSettings.append("\n");
        sbXmlEttSettings.append(toStringRules(rules));

        timeTableSettingsDelegate.accept(sbXmlEttSettings.toString());
    }

    private StringBuilder toStringSubjects(HashMap<Integer, Subject> subjects)
    {
        StringBuilder sbSubjects = new StringBuilder();
        sbSubjects.append("SUBJECTS\n");
        sbSubjects.append("__________________________________________________________\n");
        for (Map.Entry<Integer, Subject> entry : subjects.entrySet()) {
            sbSubjects.append(String.format("ID: %d  |  Name: %s\n", entry.getKey(), entry.getValue().getName()));
        }
        sbSubjects.append("__________________________________________________________\n");
        return sbSubjects;
    }

    private StringBuilder toStringTeachers(HashMap<Integer, Teacher> teachers,HashMap<Integer, Subject> subjects){
        StringBuilder sbTeachers = new StringBuilder();
        sbTeachers.append("TEACHERS\n");
        sbTeachers.append("__________________________________________________________\n");
        for(Map.Entry<Integer, Teacher > entry : teachers.entrySet()){
            sbTeachers.append(String.format("Teacher ID %d\n", entry.getKey()));
            sbTeachers.append(String.format("Teaching subjects:\n"));
            for(int i=0; i<entry.getValue().getSubjectsIdsList().size();i++){
                int subjectID=entry.getValue().getSubjectsIdsList().get(i);
                sbTeachers.append(String.format("       Subject ID: %d  |  ", subjectID));
                sbTeachers.append(String.format("Name: %s\n", subjects.get(subjectID).getName()));
            }
            sbTeachers.append("\n");
        }
        sbTeachers.append("__________________________________________________________\n");
        sbTeachers.append("\n");

        return sbTeachers;
    }

    private StringBuilder toStringGrades(HashMap<Integer, Grade> grades,HashMap<Integer, Subject> subjects) {
        StringBuilder sbGrades = new StringBuilder();
        sbGrades.append("GRADES\n");
        sbGrades.append("__________________________________________________________\n");
        for (Map.Entry<Integer, Grade> entry : grades.entrySet()) {
            sbGrades.append(String.format("\nGrade ID %d\n", entry.getKey()));

            for (Map.Entry<Integer, Integer> required : entry.getValue().getRequirements().entrySet()) {
                sbGrades.append(String.format("Subject ID: %d  |  Name: %s  |  ", required.getKey(), subjects.get(required.getKey()).getName()));
                sbGrades.append(String.format("Required Hours: %d", required.getValue()));
                sbGrades.append("\n");
            }
            sbGrades.append("\n");
        }
        sbGrades.append("__________________________________________________________\n");
        sbGrades.append("\n");
        return sbGrades;
    }

    private StringBuilder toStringRules(List<Rule> rules){
        StringBuilder sbRules = new StringBuilder();
        sbRules.append("RULES\n");
        sbRules.append("__________________________________________________________\n");
        for(int i=0; i<rules.size();i++){
            if(rules.get(i).isHard())
                sbRules.append(String.format("Rule Name: %s  |  Type: Hard", rules.get(i).getName()));
            else{
                sbRules.append(String.format("Rule Name: %s  |  Type: Soft", rules.get(i).getName()));
            }
            sbRules.append("\n");
        }
        sbRules.append("__________________________________________________________\n");
        sbRules.append("\n");
        return sbRules;
    }

}
