package gui.components.main;

import UI.models.Lesson;
import engine.models.Solution;
import gui.common.EvolutianryTimeTableResourcesConstants;
import gui.common.HistogramResourcesConstants;
import gui.components.singlehistogram.SingleWordController;
import gui.components.table.TableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class TimeTableController {


    private void createTable(String typeTitle, String typeIdTitle, List<String> tableContent, int totalDays, int TotalHours, boolean validtable) {
        //should be called from button action
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EvolutianryTimeTableResourcesConstants.TABLE_FXML_RESOURCE);
            Node table = loader.load();

            TableController tableController = loader.getController();
           // tableController.buildTable(typeTitle,typeIdTitle, tableContent, totalDays, TotalHours, validtable);

           // tableTimeFlowPane.getChildren().add(table);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
