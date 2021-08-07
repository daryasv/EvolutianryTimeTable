package engine;

import engine.models.EvolutionDataSet;
import engine.models.IRule;
import engine.models.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Evolutionary<T> {

    public void run(EvolutionDataSet<T> dataSet)
    {
        int populationSize = dataSet.getPopulationSize();
        List<IRule> rules = dataSet.getRules();
        int hardRulesWeight = dataSet.getHardRulesWeight();
        int generations = dataSet.getGenerations();

        int genCounter=0;
        List<Solution<T>> populationList = generatePopulation(populationSize, dataSet);

        while(!isEndOfEvolution(genCounter,generations))
        {
            HashMap<Solution<T>, Integer> solutionsFitnessMap = fitnessEvaluation(populationList,rules,hardRulesWeight, dataSet);

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

    public HashMap<Solution<T>, Integer> fitnessEvaluation(List<Solution<T>> solutions, List<IRule> rules, double hardRulesWeight,EvolutionDataSet<T> dataSet)
    {
        HashMap<Solution<T>, Integer> solutionFitness = new HashMap<>();
        for (Solution<T> solution: solutions)
        {
            int softFitnessSum = 0;
            int hardFitnessSum = 0;
            int hardRulesCount = 0;
            int softRulesCount = 0;
            for (IRule rule: rules)
            {
                double fit = dataSet.getFitness(solution,rule);
                if(rule.isHard())
                {
                    hardFitnessSum+= fit;
                    hardRulesCount ++;
                }
                else
                {
                    softFitnessSum+=fit;
                    softRulesCount++;
                }
            }
            int hardAvg = hardFitnessSum / hardRulesCount;
            int softAvg = softFitnessSum / softRulesCount;
            double finalFitness = hardAvg*(hardRulesWeight/100)+softAvg*((100-hardRulesWeight)/100);
            solutionFitness.put(solution, (int) finalFitness);
        }
        return solutionFitness;
    }

    public boolean isEndOfEvolution(int generationCounter,int evolutionGenerationNumber)
    {
        return generationCounter >= evolutionGenerationNumber;
    }

}
