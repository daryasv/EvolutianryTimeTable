package UI.models.evolution;

import engine.models.ICrossoverData;
import schema.models.ETTCrossover;

public class Crossover implements ICrossoverData
{
    private CrossoverSortType name;
    private int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        setName(ettCrossover);
        setCuttingPoints(ettCrossover);
    }

    public CrossoverSortType getName() {
        return name;
    }

    //TODO : add validation - check if null
    public void setName(ETTCrossover ettCrossover) {
        this.name = CrossoverSortType.valueOf(ettCrossover.getName());
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    @Override
    public String getSortOperator() {
        return name.toString();
    }

    //TODO : add validation
    public void setCuttingPoints(ETTCrossover ettCrossover) {
        this.cuttingPoints = ettCrossover.getCuttingPoints();
    }
}
