package engine.models;

import UI.models.Lesson;

import java.util.List;

public interface EvolutionDataSet<T> {

    int getPopulationSize();

    int getGenerations();

    Solution<T> getRandomSolution();

    Solution<T> mutation(Solution<T> solution);

    int getHardRulesWeight();

    List<IRule> getRules();

    List<Solution<T>> crossover(Solution<T> a, Solution<T> b);

    double getFitness(Solution<T> solution, IRule rule);

}
