package UI.models.evolution;

import UI.ValidationException;
import UI.models.LessonSortType;
import engine.models.ICrossoverData;
import schema.models.ETTCrossover;

import java.io.Serializable;

public class Crossover implements ICrossoverData , Serializable {

        private LessonSortType name;
        private int cuttingPoints;
        private CrossoverOrientation orientation;

        public Crossover(ETTCrossover ettCrossover) throws ValidationException {
            setName(ettCrossover);
            setCuttingPoints(ettCrossover);
            setOrientation(ettCrossover);
        }

        private void setOrientation(ETTCrossover ettCrossover) throws ValidationException {
            if(ettCrossover.getConfiguration() != null) {
                String orientationString = ettCrossover.getConfiguration().split("=")[1].toString();
                this.orientation = CrossoverOrientation.valueOfLabel(orientationString);
                if (this.orientation == null) {
                    throw new ValidationException("Invalid Orientation name: " + orientationString);
                }
            }
            else
            {
                orientation = null;
            }
        }

        public LessonSortType getName() {
            return name;
        }

        public void setName(ETTCrossover ettCrossover) throws ValidationException {
            this.name = LessonSortType.valueOfLabel(ettCrossover.getName());
            if (this.name == null) {
                throw new ValidationException("Invalid crossover name: " + ettCrossover.getName());
            }
        }

        public int getCuttingPoints() {
            return cuttingPoints;
        }

        @Override
        public String getSortOperator() {
            return name.toString();
        }

        public void setCuttingPoints(ETTCrossover ettCrossover) throws ValidationException {
            this.cuttingPoints = ettCrossover.getCuttingPoints();
            if (this.cuttingPoints < 1) {
                throw new ValidationException("Crossover cutting points can't be lower then 1");
            }
        }

        @Override
        public CrossoverOrientation getOrientation()
        {
            return orientation;
        }
}
