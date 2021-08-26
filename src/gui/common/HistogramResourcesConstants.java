package gui.common;

import java.net.URL;

public class HistogramResourcesConstants {

    private static final String BASE_PACKAGE = "/gui/";
    private static final  String SINGLE_HISTOGRAM_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/components/singlehistogram/singleWord.fxml";

    public static final String MAIN_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/components/main/histogram.fxml";
    public static final URL MAIN_FXML_RESOURCE = HistogramResourcesConstants.class.getResource(HistogramResourcesConstants.SINGLE_HISTOGRAM_FXML_RESOURCE_IDENTIFIER);

}
