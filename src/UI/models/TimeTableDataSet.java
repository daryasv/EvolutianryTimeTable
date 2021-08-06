package UI.models;

import UI.ValidationException;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.TimeTableMembers;
import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;
import schema.models.ETTDescriptor;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import UI.models.evolution.Mutation;


public class TimeTableDataSet implements EvolutionDataSet<Lesson> {

    final private TimeTableMembers timeTableMembers;
    final private EvolutionConfig evolutionConfig;

    public TimeTableDataSet(ETTDescriptor descriptor) throws ValidationException {
        this.timeTableMembers = new TimeTableMembers(descriptor.getETTTimeTable());
        this.evolutionConfig = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }

    public void runMutation(Solution <Lesson> child){
        List<Mutation> mutations = evolutionConfig.getMutations();
        double probability;
        int maxTupples;
        char component;
        for(int i=0; i<mutations.size();i++){
            probability=mutations.get(i).getProbability();
            maxTupples=mutations.get(i).getMaxTupples();
            component=mutations.get(i).getComponent();
            double randomNum = ThreadLocalRandom.current().nextDouble(0.0, 100.0);


            if(mutations.get(i).getName().equals(Mutation.MutationOperators.FLIP_OPERATOR.getOperatorName())){
                if(randomNum< probability)
                    runFlippingMutation(child,maxTupples, component);
            }
        }
    }

    public static void runFlippingMutation(Solution <Lesson> child, int maxTuples, char component){
        Random rand = new Random();
        int randomTuplesNum = rand.nextInt();
        for(int i=0; i<randomTuplesNum; i ++){
            if (i>maxTuples)
                break;
            else{
                int tupleIndex = rand.nextInt(child.getList().size());
                changeComponent(child.getList().get(tupleIndex), component);
            }
        }
    }

    public static void changeComponent(Lesson lesson, char component){
        int val=0;
        if(component=='C'){
            lesson.setClassId(val);
        }
        else if(component=='T'){
            lesson.setTeacherId(val);
        }
        else if(component=='D'){
            lesson.setDay(val);
        }
        else if(component=='H'){
            lesson.setHour(val);
        }
        else if(component=='S'){
            lesson.setSubjectId(val);
        }
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
