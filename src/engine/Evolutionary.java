package engine;

import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evolutionary<T> {

    public void run(EvolutionDataSet<T> dataSet)
    {
        int populationSize = dataSet.getPopulationSize();
        List<IRule<T>> rules = dataSet.getRules();
        int hardRulesWeight = dataSet.getHardRulesWeight();
        int generations = dataSet.getGenerations();

        int genCounter=0;
        List<Solution<T>> populationList = generatePopulation(populationSize, dataSet);

        while(!isEndOfEvolution(genCounter,generations))
        {
            HashMap<Solution<T>, Integer> solutionsFitnessMap = fitnessEvaluation(populationList,rules,hardRulesWeight);

            //selection - returns list of best parents
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
        HashMap<Solution<T>, Integer> solutionFitness = new HashMap<>();
        for (Solution<T> solution: solutions)
        {
            int softFitness = 0;
            int hardFitness = 0;
            for (IRule<T> rule: rules)
            {
                int fit = rule.getFitness(solution);
                if(rule.isHard())
                {
                    hardFitness+= fit;
                }
                else
                {
                    softFitness+=fit;
                }
            }
            int finalFitness = hardFitness*(hardRulesWeight/100)+softFitness*((100-hardRulesWeight)/100);
            solutionFitness.put(solution, finalFitness);
        }
        return solutionFitness;
    }

    public boolean isEndOfEvolution(int generationCounter,int evolutionGenerationNumber)
    {
        return generationCounter >= evolutionGenerationNumber;
    }






}
