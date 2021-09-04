package gui;

import gui.common.EttResourcesConstants;
import gui.components.main.EngineController;
import gui.logic.EngineLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.fxmisc.cssfx.CSSFX;

import java.net.URL;

public class EttMain extends Application {

    /*
    all long text files are books downloaded from
    http://www.gutenberg.org/ebooks/search/%3Fsort_order%3Ddownloads
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        CSSFX.start();
        
        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource(EttResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        // wire up controller
        EngineController ettController = loader.getController();
        EngineLogic businessLogic = new EngineLogic(ettController);
        ettController.setPrimaryStage(primaryStage);
        ettController.setEngineLogic(businessLogic);

        // set stage
        primaryStage.setTitle("ETT");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {

        launch(args);
    }
}
