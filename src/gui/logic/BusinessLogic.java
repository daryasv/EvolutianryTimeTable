package gui.logic;

import UI.ProgramManager;
import UI.ValidationException;
import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.models.SolutionFitness;
import gui.components.main.HistogramController;
import gui.logic.tasks.histogram.CalculateHistogramsTask;
import gui.logic.tasks.metadata.CollectMetadataTask;
import gui.components.main.UIAdapter;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

public class BusinessLogic {
    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    SolutionFitness<Lesson> globalBestSolution;
    List<SolutionFitness<Lesson>> bestSolutions;

    private SimpleStringProperty fileName;
    private HistogramController controller;

    //todo: remove
    private long totalWords;
    private Task<Boolean> currentRunningTask;


    public BusinessLogic(HistogramController controller) {
        this.fileName = new SimpleStringProperty();
        this.controller = controller;
        totalWords = -1;
    }

    public SimpleStringProperty fileNameProperty() {
        return this.fileName;
    }



    public void collectMetadata(Consumer<Long> totalWordsDelegate, Consumer<Long> totalLinesDelegate, Runnable onFinish) {

        Consumer<Long> totalWordsConsumer = tw -> {
            this.totalWords = tw;
            totalWordsDelegate.accept(tw);
        };

        currentRunningTask = new CollectMetadataTask(fileName.get(), totalWordsConsumer, totalLinesDelegate);

        controller.bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();
    }


    public void calculateHistogram(UIAdapter uiAdapter, Runnable onFinish) {
        currentRunningTask = new CalculateHistogramsTask(fileName.get(), totalWords, uiAdapter, (q) -> controller.onTaskFinished(Optional.ofNullable(onFinish)));

        controller.bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();
    }

    public void cancelCurrentTask() {
        currentRunningTask.cancel();
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
            fileName.set(absolutePath);

        } catch (JAXBException e) {
            //ProgramManager.systemSetting.IS_FILE_LOADED.status=false;
            System.out.println("Failed to parse xml");
        }
    }

    private void updateDataSets(ETTDescriptor descriptor) throws ValidationException {
        timeTable = new TimeTableDataSet(descriptor);
        evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }

}
