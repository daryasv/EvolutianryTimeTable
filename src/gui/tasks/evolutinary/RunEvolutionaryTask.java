package gui.tasks.evolutinary;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.Evolutionary;
import engine.models.EngineProgressInterface;
import engine.models.SolutionFitness;
import gui.common.HistogramsUtils;
import gui.components.main.UIAdapter;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

public class RunEvolutionaryTask extends Task<Boolean> {

    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    SolutionFitness<Lesson> globalBestSolution;
    List<SolutionFitness<Lesson>> bestSolutions;

    private int generations;
    private int interval;

    public RunEvolutionaryTask(TimeTableDataSet timeTable, EvolutionConfig config,int generations,int interval ) {
        this.timeTable = timeTable;
        this.evolutionEngineDataSet = config;
        this.generations = generations;
        this.interval = interval;
    }

    @Override
    protected Boolean call() throws Exception {

        try {
            //updateMessage("Fetching file...");
            updateProgress(0,1);
            Evolutionary<Lesson> evolutionary = new Evolutionary<>();
            timeTable.setGenerations(1000);
            timeTable.setGenerationsInterval(10);
            evolutionary.run(timeTable,this::updateProgress);
            globalBestSolution = evolutionary.getGlobalBestSolution();
            bestSolutions = evolutionary.getBestSolutions();
            // update in UI
            //todo: update current generations + current best fitness
//            Platform.runLater(
//                    () -> totalWordsDelegate.accept(totalWords)
//            );
            updateMessage("Done...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;
    }

}