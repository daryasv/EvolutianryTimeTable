package engine;

import engine.models.*;

import java.util.*;

public class Evolutionary<T> {

    public void run(EvolutionDataSet<T> dataSet)
    {
        int populationSize = dataSet.getPopulationSize();
        List<IRule> rules = dataSet.getRules();
        int hardRulesWeight = dataSet.getHardRulesWeight();
        int generations = dataSet.getGenerations();
        int generationInterval = dataSet.getGenerationInterval();
        int genCounter=0;

        System.out.println("Evolutionary Engine starts !");
        //generate population
        List<Solution<T>> populationList = generatePopulation(populationSize, dataSet);

        while(!isEndOfEvolution(genCounter,generations))
        {
            List<Solution<T>> newGeneration = new ArrayList<>();
            //Fitness
            Map<Solution<T>, Integer> solutionsFitnessMap = fitnessEvaluation(populationList, rules, hardRulesWeight, dataSet);
            //Selection
            List<Solution<T>> selectionSolutions = getSelectionSolutions(solutionsFitnessMap, dataSet.getSelectionData());

            //selection - returns list of best parents
            for (int i = 0; i < populationSize;) {
                //make new generation
                Solution<T> parent1 = getRandomSolution(selectionSolutions);
                Solution<T> parent2 = getRandomSolution(selectionSolutions);
                while (parent1.equals(parent2)) //verify the parents are not the same one
                {
                    parent2 = getRandomSolution(selectionSolutions);
                }
                List<Solution<T>> children = crossover(dataSet, parent1, parent2);
                //run mutation on children
                children.forEach(dataSet::mutation);
                newGeneration.add(children.get(0));
                i++;
                if(i<populationSize) {
                    newGeneration.add(children.get(1));
                    i++;
                }
            }
            populationList = newGeneration;
            genCounter++;

        }

    }

    private List<Solution<T>> getSelectionSolutions(Map<Solution<T>, Integer> map, ISelectionData selectionData) {
        List<Map.Entry<Solution<T>, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((obj01, obj02) -> obj02.getValue() - obj01.getValue());

        if(selectionData.getType() == SelectionType.Truncation){
            double value = selectionData.getValue();
            int numToPull = (int)((value / 100) * list.size());
            list = list.subList(0,numToPull);
        }

        //add to result list
        List<Solution<T>> result = new ArrayList<>();
        for (Map.Entry<Solution<T>, Integer> entry : list) {
            result.add(entry.getKey());
        }
        return result;
    }

//    private LinkedHashMap<Solution, Integer> sortSolutionsByFitness(Map<Solution, Integer> fitnessPerSolution) {
//        LinkedHashMap<Solution, Integer> reverseSortedMap = new LinkedHashMap<>();
//        fitnessPerSolution.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
//        return reverseSortedMap;
//    }
//
//        private List<Solution<T>> selectionProcess(Map<Solution, Integer> fitnessPerSolution, String type, int present){
//       int numOfSolutions=(present)*fitnessPerSolution.size()/100;
//       List<Solution<T>> bestSolutions = new ArrayList<>();
//        if(type.equals("Truncation")){
//            LinkedHashMap<Solution, Integer> sortedMap = sortSolutionsByFitness(fitnessPerSolution);
//            List<Solution> sortedList=new ArrayList<>(sortedMap.keySet());
//            for(int i=0; i<numOfSolutions; i++){
//                bestSolutions.add(sortedList.get(i));
//            }
//        }
//        return bestSolutions;
//    }
//
//
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

    public Solution<T> getRandomSolution (List<Solution<T>> solutions)
    {
        Random random = new Random();
        return solutions.get(random.nextInt(solutions.size()));
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
