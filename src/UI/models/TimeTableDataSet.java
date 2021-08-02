package UI.models;

import UI.ValidationException;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.TimeTableMembers;
import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;
import schema.models.ETTDescriptor;
import java.util.List;


public class TimeTableDataSet implements EvolutionDataSet<Lesson> {

    final private TimeTableMembers timeTableMembers;
    final private EvolutionConfig evolutionConfig;

    public TimeTableDataSet(ETTDescriptor descriptor) throws ValidationException {
        this.timeTableMembers = new TimeTableMembers(descriptor.getETTTimeTable());
        this.evolutionConfig = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }

    //TODO
    @Override
    public int getPopulationSize() {
        return 0;
    }

    //TODO
    @Override
    public int getGenerations() {
        return 0;
    }

    @Override
    public Solution<Lesson> getRandomSolution() {
        return timeTableMembers.getRandomSolution();
    }

    //TODO
    @Override
    public Solution<Lesson> mutation(Solution<Lesson> solution) {
        return null;
    }

    //TODO
    @Override
    public int getHardRulesWeight() {
        return 0;
    }

    //TODO
    @Override
    public List<IRule<Lesson>> getRules() {
        return null;
    }

    //TODO
    @Override
    public List<Solution<Lesson>> crossover(Solution<Lesson> a, Solution<Lesson> b) {
        return null;
    }
}
