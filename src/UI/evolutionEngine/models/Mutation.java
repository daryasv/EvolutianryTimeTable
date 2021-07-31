package UI.evolutionEngine.models;

import schema.models.ETTMutation;

public class Mutation
{
    private String name;
    private double probability;
    private int maxTupples;
    private char component;

    public Mutation(ETTMutation ettMutation) {
        setName(ettMutation);
        setProbability(ettMutation);
        setMaxTupples(ettMutation);
        setComponent(ettMutation);
    }

    public String getName() {
        return name;
    }

    //TODO : validation
    public void setName(ETTMutation ettMutation) {
        this.name = ettMutation.getName();
    }

    public double getProbability() {
        return probability;
    }

    //TODO : validation
    public void setProbability(ETTMutation ettMutation) {
        this.probability = ettMutation.getProbability();
    }

    public int getMaxTupples() {
        return maxTupples;
    }

    //TODO : validation
    //very hardcoded
    public void setMaxTupples(ETTMutation ettMutation) {
        maxTupples = Integer.parseInt(ettMutation.getConfiguration().split(",")[0].split("=")[1]);
    }

    public char getComponent() {
        return component;
    }

    //TODO : validation
    //very hardcoded
    public void setComponent(ETTMutation ettMutation) {
        this.component = ettMutation.getConfiguration().charAt(ettMutation.getConfiguration().length());
    }
}
