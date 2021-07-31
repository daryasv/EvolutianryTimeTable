package Engine;

import Engine.models.EvolutionDataSet;
import Engine.models.IRule;
import UI.models.Lesson;
import Engine.models.Solution;
import schema.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolutionary<T> {

    public List<Solution<T>> generatePopulation(int size,EvolutionDataSet dataSet){
        List<Solution<T>> solutions = new ArrayList<>();
        for (int i = 0; i < size; i++)
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
