package gui.common;

import java.net.URL;

public class EttResourcesConstants {

    private static final String BASE_PACKAGE = "/gui/";
//    private static final  String SINGLE_HISTOGRAM_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/components/singlehistogram/singleWord.fxml";

    public static final String MAIN_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/components/main/ett.fxml";
    //public static final URL MAIN_FXML_RESOURCE = HistogramResourcesConstants.class.getResource(HistogramResourcesConstants.SINGLE_HISTOGRAM_FXML_RESOURCE_IDENTIFIER);

    private static final  String TABLE_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/components/table/table.fxml";
    public static final URL TABLE_FXML_RESOURCE= EttResourcesConstants.class.getResource(EttResourcesConstants.TABLE_FXML_RESOURCE_IDENTIFIER);

}
