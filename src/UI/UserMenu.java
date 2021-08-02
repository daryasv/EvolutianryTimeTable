package UI;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Scanner;

public class UserMenu
{
    public static enum Commands
    {
        LOAD_TABLE_DETAILS("1"), SHOW_TABLE_SETTINGS("2"), RUN_ALGORITHM("3"), SHOW_BEST_SOLUTION("4"), SHOW_ALGORITHM_PROC("5"), EXIT("6");
        private final String commandVal;
        private Boolean isActive;

        private Commands(String value){
            this.commandVal= value;
            this.isActive=false;
        }

        public void setStatus(Boolean status){
            this.isActive=status;
        }

        public Boolean getStatus(){
            return this.isActive;
        }

        public final String getCommandValue(){
            return this.commandVal;
        }
    }
    final String openingMsg= "Welcome!\nplease enter your command:";

    private void printMenu(Boolean isFileLoaded)
    {
        System.out.println(openingMsg);
        System.out.println(String.format("Press (%s) to load a file", Commands.LOAD_TABLE_DETAILS.commandVal));
        System.out.println(String.format("Press (%s) to show table's settings", Commands.SHOW_TABLE_SETTINGS.commandVal));
        System.out.println(String.format("Press (%s) to run the evolutionary algorithm", Commands.RUN_ALGORITHM.commandVal));
        System.out.println(String.format("Press (%s) to show the best solution", Commands.SHOW_BEST_SOLUTION.commandVal));
        System.out.println(String.format("Press (%s) to show algorithm's logs", Commands.SHOW_ALGORITHM_PROC.commandVal));
        System.out.println(String.format("Press (%s) to exit", Commands.EXIT.commandVal));
    }
    public void getUserInput(Boolean isFileLoaded) {
        printMenu(isFileLoaded);
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        if (userInput == Commands.LOAD_TABLE_DETAILS.commandVal) {
            //TODO: load XML file
        } else if (userInput == Commands.SHOW_TABLE_SETTINGS.commandVal) {

        } else if (userInput == Commands.RUN_ALGORITHM.commandVal) {

        } else if (userInput == Commands.SHOW_BEST_SOLUTION.commandVal) {

        } else if (userInput == Commands.SHOW_ALGORITHM_PROC.commandVal) {

        } else if (userInput.equals(Commands.EXIT.commandVal)) {
            Commands.EXIT.isActive=true;
        }
        else{
            System.out.println("invalid command, please try again:\n");
        }

    }

}
