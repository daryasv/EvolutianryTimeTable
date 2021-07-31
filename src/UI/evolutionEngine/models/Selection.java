package UI.evolutionEngine.models;

import schema.models.ETTSelection;

//maybe enum?
public class Selection
{
    private String type;
    private int topPercent;

    public Selection(ETTSelection ettSelection) {
        setType(ettSelection);
        setTopPercent(ettSelection);
    }

    public String getType() {
        return type;
    }

    //TODO : validation
    public void setType(ETTSelection ettSelection) {
        this.type = ettSelection.getType();
    }

    public int getTopPercent() {
        return topPercent;
    }

    //TODO : validation
    //very hardcoded
    public void setTopPercent(ETTSelection ettSelection) {
        this.topPercent = Integer.parseInt(ettSelection.getConfiguration().split("=")[1]);
    }
}
