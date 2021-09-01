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

    EvolutionaryTaskMembers evolutionaryTaskMembers;
    EndCondition endCondition;
    private int interval;

    public RunEvolutionaryTask(EvolutionaryTaskMembers evolutionaryTaskMembers, String endConditionType,double limit,int interval) {
        this.evolutionaryTaskMembers = evolutionaryTaskMembers;
        EndCondition.EndConditionType endConditionTypeEnum = EndCondition.EndConditionType.valueOfLabel(endConditionType);
        this.interval = interval;
        endCondition = new EndCondition() {
            @Override
            public EndConditionType getEndCondition() {
                return endConditionTypeEnum;
            }

            @Override
            public double getLimit() {
                return limit;
            }
        };
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            if(evolutionaryTaskMembers.getEvolutionary().getGlobalBestSolution() == null){
                updateProgress(0,1);
            }
            evolutionaryTaskMembers.getTimeTable().setGenerationsInterval(interval);
            evolutionaryTaskMembers.getEvolutionary().run(evolutionaryTaskMembers.getTimeTable(),endCondition,this::updateProgress);
            evolutionaryTaskMembers.setGlobalBestSolution(evolutionaryTaskMembers.getEvolutionary().getGlobalBestSolution());
            evolutionaryTaskMembers.setBestSolutions(evolutionaryTaskMembers.getEvolutionary().getBestSolutions());
            updateMessage("Done...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;
    }

    public void stopAlgo(){
        evolutionaryTaskMembers.getEvolutionary().stop();
    }

    public EvolutionaryTaskMembers getEvolutionaryTaskMembers(){
        return this.evolutionaryTaskMembers;
    }
}