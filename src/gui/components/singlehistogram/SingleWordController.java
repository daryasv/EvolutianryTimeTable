package gui.components.singlehistogram;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleWordController extends BasicHistogramData {

    @FXML private Label wordLabel;
    @FXML private Label countLabel;

    public SingleWordController() {
        super("", -1);
    }

    @FXML
    private void initialize() {
        wordLabel.textProperty().bind(Bindings.concat("<", word, ">"));
        countLabel.textProperty().bind(count.asString());
    }
}
