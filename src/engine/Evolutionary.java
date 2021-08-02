package engine;

import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evolutionary<T> {

    private int evolutionGenerationNumber;

    public int getEvolutionGenerationNumber() {
        return evolutionGenerationNumber;
    }

    public void setEvolutionGenerationNumber(int evolutionGenerationNumber) {
        this.evolutionGenerationNumber = evolutionGenerationNumber;
    }

    //TODO
    public void run(int generations,int populationSize, EvolutionDataSet dataSet, List<IRule<T>> rules,int hardRulesWeight)
    {
        int genCounter=0;
        evolutionGenerationNumber = generations;
        List<Solution<T>> populationList = generatePopulation(populationSize, dataSet);

        while(!isEndOfEvolution(genCounter))
        {
            HashMap<Solution<T>, Integer> solutionsFitnessMap = fitnessEvaluation(populationList,rules,hardRulesWeight);
            //solution - returns list of best parents
            //make new generation
            //{ while (children <= population size)
            //  1. pick two parents
            //  2. crossover (parent 1, parent 2, operator) - returs list of 2 children
            //  3. mutation (child 1) returns child
            //  4. mutation (child 2) returns child
            //  5. child counter ++
            genCounter++;
        }

    }

    public List<Solution<T>> generatePopulation(int size, EvolutionDataSet dataSet){
        List<Solution<T>> solutions = new ArrayList<>();
        for (int i = 1; i <= size; i++)
        {
            Solution<T> solution = dataSet.getRandomSolution();
            solutions.add(solution);
        }
        return solutions;
    }

    public HashMap<Solution<T>, Integer> fitnessEvaluation(List<Solution<T>> solutions, List<IRule<T>> rules, int hardRulesWeight)
    {
        return null;
    }

    public boolean isEndOfEvolution(int generationCounter)
    {
        if(generationCounter >= evolutionGenerationNumber)
        {
            return true;
        }
        return false;
    }






}
