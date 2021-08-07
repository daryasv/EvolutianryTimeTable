package UI.models.evolution;

import UI.ValidationException;
import schema.models.*;

import java.util.ArrayList;
import java.util.List;

public class EvolutionConfig
{
    private int initialPopulation;
    private Selection selection;
    private Crossover crossover;
    private List<Mutation> mutations;
    private int generations;
    private int generationsInterval;

    public EvolutionConfig(ETTEvolutionEngine ettEvolutionEngine) throws ValidationException {
        setInitialPopulation(ettEvolutionEngine.getETTInitialPopulation());
        setCrossover(ettEvolutionEngine.getETTCrossover());
        setSelection(ettEvolutionEngine.getETTSelection());
        setMutations(ettEvolutionEngine.getETTMutations().getETTMutation());
    }


    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) throws ValidationException {
        if(generations >= 100)
            this.generations = generations;
        else
            throw new ValidationException("generation smaller than 100");
    }

    public int getGenerationsInterval() {
        return generationsInterval;
    }

    public void setGenerationsInterval(int generationsInterval) throws ValidationException {
        if(generationsInterval < this.generations)
            this.generationsInterval = generationsInterval;
        else
            throw new ValidationException("generation interval bigger than generations");

    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    //TODO : validation
    public void setInitialPopulation(ETTInitialPopulation ettInitialPopulation) {
        this.initialPopulation = ettInitialPopulation.getSize();
    }

    public Selection getSelection() {
        return selection;
    }

    //TODO : validation
    public void setSelection(ETTSelection ettSelection) {
        this.selection = new Selection(ettSelection);
    }

    public Crossover getCrossover() {
        return crossover;
    }

    //TODO : validation
    public void setCrossover(ETTCrossover ettCrossover) {
        this.crossover = new Crossover(ettCrossover);
    }

    public List<Mutation> getMutations() {
        return mutations;
    }

    //TODO : validation
    public void setMutations(List<ETTMutation> ettMutations) {
        this.mutations = new ArrayList<>();
        for (ETTMutation ettMutation: ettMutations) {
            mutations.add(new Mutation(ettMutation));
        }
    }
}
