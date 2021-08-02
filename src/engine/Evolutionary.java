package engine;

import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evolutionary<T> {

    public List<Solution<T>> generatePopulation(int size,EvolutionDataSet dataSet){
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


}
