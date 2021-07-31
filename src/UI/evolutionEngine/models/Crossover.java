package UI.evolutionEngine.models;

import schema.models.ETTCrossover;

//maybe unum?
public class Crossover
{
    private String name;
    private int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        setName(ettCrossover);
        setCuttingPoints(ettCrossover);
    }

    public String getName() {
        return name;
    }

    //TODO : add validation
    public void setName(ETTCrossover ettCrossover) {
        this.name = ettCrossover.getName();
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    //TODO : add validation
    public void setCuttingPoints(ETTCrossover ettCrossover) {
        this.cuttingPoints = ettCrossover.getCuttingPoints();
    }
}
