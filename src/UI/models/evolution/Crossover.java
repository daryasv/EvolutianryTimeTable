package UI.models.evolution;

import UI.models.LessonSortType;
import engine.models.ICrossoverData;
import schema.models.ETTCrossover;

import java.io.Serializable;

public class Crossover implements ICrossoverData , Serializable
{
    private LessonSortType name;
    private int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        setName(ettCrossover);
        setCuttingPoints(ettCrossover);
    }

    public LessonSortType getName() {
        return name;
    }

    //TODO : add validation - check if null
    public void setName(ETTCrossover ettCrossover) {
        this.name = LessonSortType.valueOf(ettCrossover.getName());
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
