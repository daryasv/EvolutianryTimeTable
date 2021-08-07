package engine.models;

import UI.models.timeTable.RuleType;

public enum SelectionType {
    Truncation("Truncation");

    public String name;
    SelectionType(String name) {
        this.name = name;
    }

    public static SelectionType valueOfLabel(String label) {
        for (SelectionType e : values()) {
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
