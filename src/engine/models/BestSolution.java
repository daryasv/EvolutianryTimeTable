package engine.models;

import java.util.HashMap;
import java.util.Map;

//todo : implement all

public class BestSolution<T> {
    private Solution<T> solution;
    private int fitness;
    private HashMap<IRule, Integer> rulesFitness;
    private double hardRulesAvg;
    private double softRulesAvg;

    public BestSolution(Solution<T> solution, int fitness, HashMap<IRule, Integer> rulesFitness) {
        this.solution = solution;
        this.fitness = fitness;
        this.rulesFitness = rulesFitness;
    }

    public Solution<T> getSolution() {
        return solution;
    }

    public void setSolution(Solution<T> solution) {
        this.solution = solution;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public HashMap<IRule, Integer> getRulesFitness() {
        return rulesFitness;
    }

    public void setRulesFitness(HashMap<IRule, Integer> rulesFitness) {
        this.rulesFitness = rulesFitness;
    }

    public double getHardRulesAvg() {
        return hardRulesAvg;
    }

    public void setHardRulesAvg(double hardRulesAvg) {
        this.hardRulesAvg = hardRulesAvg;
    }

    public double getSoftRulesAvg() {
        return softRulesAvg;
    }

    public void setSoftRulesAvg(double softRulesAvg) {
        this.softRulesAvg = softRulesAvg;
    }

    //TODO
    public boolean IsBetterSolutionThan(BestSolution<T> genBestSolution)
    {
        return false;
    }
}
