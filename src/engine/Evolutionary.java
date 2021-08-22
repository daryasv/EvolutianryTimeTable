package engine;

import engine.models.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Evolutionary<T> {

     List<SolutionFitness<T>> bestSolutions = new ArrayList<>();
     SolutionFitness<T> globalBestSolution = null;

    public List<SolutionFitness<T>> getBestSolutions() {
        return bestSolutions;
    }

    public SolutionFitness<T> getGlobalBestSolution() {
        return globalBestSolution;
    }

    public void run(EvolutionDataSet<T> dataSet)
    {
        int populationSize = dataSet.getPopulationSize();
        List<IRule> rules = dataSet.getRules();
        int hardRulesWeight = dataSet.getHardRulesWeight();
        int generations = dataSet.getGenerations();
        int generationInterval = dataSet.getGenerationInterval();

        if(generations == 0){
            System.out.println("Generations count can't be 0");
            return;
        }
        if(generationInterval == 0){
            System.out.println("Generations count can't be 0");
            return;
        }
        if(generations < generationInterval){
            System.out.println("generationInterval cant be bigger then generations");
            return;
        }

        int genCounter=0;

        System.out.println("Evolutionary Engine starts !");
        //generate population
        List<Solution<T>> populationList = generatePopulation(populationSize, dataSet);
        //fitness
        List<SolutionFitness<T>> solutionsFitnessMap = fitnessEvaluation(populationList, rules, hardRulesWeight, dataSet);

        //Selection
        SelectionResult<T> selectionSolutions = getSelectionSolutions(solutionsFitnessMap, dataSet.getSelectionData());

        while(!isEndOfEvolution(genCounter,generations))
        {
            List<Solution<T>> newGeneration = selectionSolutions.getEliteSolutions().stream().map(SolutionFitness::getSolution).collect(Collectors.toList());
            //make selectionSolutions into parents solution
            List<Solution<T>> parentSolutions = selectionSolutions.getSelectionSolutions().stream().map(SolutionFitness::getSolution).collect(Collectors.toList());

            //create new generation - children in size of population
            for (int i = 0; i < populationSize - newGeneration.size();) {
                //pick 2 parents
                Solution<T> parent1 = getRandomSolution(parentSolutions);
                Solution<T> parent2 = getRandomSolution(parentSolutions);
                while (parent1.equals(parent2)) //verify the parents are not the same one
                {
                    parent2 = getRandomSolution(parentSolutions);
                }
                //return list of children
                List<Solution<T>> children = crossover(dataSet, parent1, parent2);
                //run mutation on children

                newGeneration.add(children.get(0));
                i++;
                //in case of odd population size, will not enter the if
                if(i<populationSize) {
                    newGeneration.add(children.get(1));
                    i++;
                }
            }

            for (int i=0; i< dataSet.getMutations().size();i++) {
                IMutation<T> mutation = dataSet.getMutations().get(i);
                List<Integer> changed = new ArrayList<>();

                int total = (int)(populationSize * mutation.getProbability());
                for (int j =0;j<total;){
                    int rndSol = getRandomSolutionIndex(newGeneration);
                    if(!changed.contains(rndSol)){
                        changed.add(rndSol);
                        j++;
                        dataSet.mutation(newGeneration.get(rndSol),mutation);
                    }
                }
            }
            //generation created
            populationList = newGeneration;
            genCounter++;

            //Fitness
            solutionsFitnessMap = fitnessEvaluation(populationList, rules, hardRulesWeight, dataSet);
            //Selection
            selectionSolutions = getSelectionSolutions(solutionsFitnessMap, dataSet.getSelectionData());

            SolutionFitness<T> bestGenSolution = selectionSolutions.getSelectionSolutions().get(0);
            if(globalBestSolution == null || bestGenSolution.compareTo(globalBestSolution) > 0) {
                globalBestSolution = bestGenSolution;
            }

            //printing interval every #generationInterva generations
            if(genCounter % generationInterval == 0)
            {
                bestSolutions.add(bestGenSolution);
                DecimalFormat f = new DecimalFormat("##.0");
                System.out.println("EVOLUTIONARY ENGINE STATUS: Generation: "+(genCounter)+
                                    ", Best Fitness On Generation: "+(f.format(bestGenSolution.getFitness())));
            }
        }
    }

    private SelectionResult<T> getSelectionSolutions(List<SolutionFitness<T>> map, ISelectionData selectionData) {
        List<SolutionFitness<T>> list = new ArrayList<>(map);
        List<SolutionFitness<T>> elites = new ArrayList<>();
        //desc sort -> z to a
        list.sort((obj01, obj02) -> obj02.compareTo(obj01));

        if(selectionData.getElitismCount() > 0){
            int toPull = Math.min(selectionData.getElitismCount(), list.size());
            elites = new ArrayList<>(list.subList(0,toPull));
            list = list.subList(toPull,list.size());
        }

        if(selectionData.getType() == SelectionType.Truncation){
            double value = selectionData.getValue();
            int numToPull = (int)((value / 100) * list.size());
            list = list.subList(0,numToPull);
        }

        list.addAll(elites);

        return new SelectionResult<T>(elites,list);
    }

    private List<Solution<T>> crossover(EvolutionDataSet<T> dataSet, Solution<T> parent1, Solution<T> parent2)
    {
        int cuttingPoints = dataSet.getCrossoverData().getCuttingPoints();
        String operator = dataSet.getCrossoverData().getSortOperator();
        Solution<T> child1 = new Solution<>();
        child1.setList(new ArrayList<>());
        Solution<T> child2 = new Solution<>();
        child2.setList(new ArrayList<>());

        //sort parents
        parent1 = dataSet.sort(parent1,operator);
        parent2 = dataSet.sort(parent2,operator);

        //get random cutting points
        int listLength = parent1.getList().size();
        List<Integer> points = new ArrayList<>();
        for (int i = 0;i<cuttingPoints;){
            Random random = new Random();
            int point = random.nextInt(listLength - 1) + 1;
            if(!points.contains(point)){
                points.add(point);
                i++;
            }
        }
        Collections.sort(points);
        points.add(listLength);

        //cut and mix
        int startIndex = 0;
        for ( int i = 0; i < points.size();i++) {
            int end = points.get(i);
            List<T> subList1 = parent1.getList().subList(startIndex, end);
            List<T> subList2 = parent2.getList().subList(startIndex, end);
            if (i % 2 == 0) {
                child1.getList().addAll(subList1);
                child2.getList().addAll(subList2);
            } else {
                child1.getList().addAll(subList2);
                child2.getList().addAll(subList1);
            }
            startIndex = end;
        }

        List<Solution<T>> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        return children;
    }


    private int getRandomSolutionIndex (List<Solution<T>> solutions)
    {
        Random random = new Random();
        return random.nextInt(solutions.size());
    }

    private Solution<T> getRandomSolution (List<Solution<T>> solutions)
    {
        return solutions.get(getRandomSolutionIndex(solutions));
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

    public List<SolutionFitness<T>> fitnessEvaluation(List<Solution<T>> solutions, List<IRule> rules, double hardRulesWeight,EvolutionDataSet<T> dataSet)
    {
        List<SolutionFitness<T>> solutionFitness = new ArrayList<>();
        HashMap<IRule,Double> rulesFitness = new HashMap<>();
        for (Solution<T> solution: solutions)
        {
            int softFitnessSum = 0;
            int hardFitnessSum = 0;
            int hardRulesCount = 0;
            int softRulesCount = 0;
            for (IRule rule: rules)
            {
                double fit = dataSet.getFitness(solution,rule);
                rulesFitness.put(rule,fit);

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
            SolutionFitness<T> solutionFitness1 = new SolutionFitness<T>(solution,finalFitness,rulesFitness,hardAvg,softAvg);
            solutionFitness.add(solutionFitness1);
        }
        return solutionFitness;
    }

    public boolean isEndOfEvolution(int generationCounter,int evolutionGenerationNumber)
    {
        return generationCounter >= evolutionGenerationNumber;
    }

}
