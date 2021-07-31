package UI;

public class UserMenu
{
    public enum Commands
    {
        LOAD_TABLE_DETAILS, SHOW_TABLE_SETTINGS, RUN_ALGORITHM, SHOW_BEST_SOLUTION, SHOW_ALGORITHM_PROC, EXIT,
    }
    final String openingMsg= "Welcome!, please enter your command :)";
    public void PrintMenu()
    {
        System.out.println(openingMsg);
    }
}
