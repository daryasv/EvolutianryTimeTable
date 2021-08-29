package gui.tasks.evolutinary;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.Evolutionary;
import engine.models.EndCondition;
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
    private EndCondition.EndConditionType endConditionType;
    private int limit;

    public RunEvolutionaryTask(TimeTableDataSet timeTable, EvolutionConfig config, String endCondition,int limit,int interval) {
        this.timeTable = timeTable;
        this.evolutionEngineDataSet = config;
        this.endConditionType = EndCondition.EndConditionType.valueOfLabel(endCondition);
        this.limit = limit;
        this.interval = interval;
    }

    @Override
    protected Boolean call() throws Exception {

        try {
            //updateMessage("Fetching file...");
            updateProgress(0,1);
            Evolutionary<Lesson> evolutionary = new Evolutionary<>();

            EndCondition endCondition = new EndCondition() {
                @Override
                public EndConditionType getEndCondition() {
                    return endConditionType;
                }

                @Override
                public int getLimit() {
                    return limit;
                }
            };
            timeTable.setGenerationsInterval(interval);
            evolutionary.run(timeTable,endCondition,this::updateProgress);
            globalBestSolution = evolutionary.getGlobalBestSolution();
            bestSolutions = evolutionary.getBestSolutions();
            updateMessage("Done...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;
    }

}