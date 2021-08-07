package UI.models.evolution;

import UI.models.timeTable.RuleType;

public enum CrossoverSortType {
    DayTimeOriented("DayTimeOriented");

    public String name;
    CrossoverSortType(String name) {
        this.name = name;
    }

    public static CrossoverSortType valueOfLabel(String label) {
        for (CrossoverSortType e : values()) {
            if (e.name.equals(label)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
