package engine.models;

import UI.models.LessonSortType;
import UI.models.evolution.Crossover;

public interface ICrossoverData {

    public enum CrossoverOrientation {
        BY_CLASS("CLASS"), BY_TEACHER("TEACHER");

        public String name;

        CrossoverOrientation(String name) {
            this.name = name;
        }

        public static CrossoverOrientation valueOfLabel(String label) {
            for (CrossoverOrientation e : values()) {
                if (e.name().equals(label)) {
                    return e;
                }
            }
            return null;
        }
    }
    int getCuttingPoints();
    String getSortOperator();
    CrossoverOrientation getOrientation();
}
