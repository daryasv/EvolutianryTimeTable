package gui.common;

public class Utils {

    public static Double tryParseDouble(String text)  {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer tryParse(String text)  {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
