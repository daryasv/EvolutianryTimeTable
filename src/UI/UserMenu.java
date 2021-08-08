package UI;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Scanner;

public class UserMenu
{
    public static enum Commands
    {
        LOAD_TABLE_DETAILS("1"), SHOW_TABLE_SETTINGS("2"), RUN_ALGORITHM("3"),
        SHOW_BEST_SOLUTION("4"), SHOW_ALGORITHM_PROC("5"), EXIT("6"),
        PRINT_RAW("1"), PRINT_PER_CLASS("2"),PRINT_PER_TEACHER("3");
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
    private final String openingMsg= "please enter your command:";

    private void printMenu()
    {
        System.out.println(openingMsg);
        System.out.println(String.format("Press (%s) to load a file", Commands.LOAD_TABLE_DETAILS.commandVal));
        System.out.println(String.format("Press (%s) to show table's settings", Commands.SHOW_TABLE_SETTINGS.commandVal));
        System.out.println(String.format("Press (%s) to run the evolutionary algorithm", Commands.RUN_ALGORITHM.commandVal));
        System.out.println(String.format("Press (%s) to show the best solution", Commands.SHOW_BEST_SOLUTION.commandVal));
        System.out.println(String.format("Press (%s) to show algorithm's logs", Commands.SHOW_ALGORITHM_PROC.commandVal));
        System.out.println(String.format("Press (%s) to exit", Commands.EXIT.commandVal));
    }
    public void getUserInput() {
        printMenu();
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        if (userInput.equals(Commands.LOAD_TABLE_DETAILS.commandVal)) {
            Commands.LOAD_TABLE_DETAILS.isActive=true;
            System.out.println("please enter your file's path:\n");

        } else if (userInput.equals(Commands.SHOW_TABLE_SETTINGS.commandVal)) {
            Commands.SHOW_TABLE_SETTINGS.isActive=true;

        } else if (userInput.equals( Commands.RUN_ALGORITHM.commandVal)) {
            Commands.RUN_ALGORITHM.isActive=true;

        }
        else if (userInput.equals(Commands.SHOW_BEST_SOLUTION.commandVal)) {
          if(ProgramManager.checkIfFileLoaded()&&(ProgramManager.checkIfSolutionFound())) {
              Commands.SHOW_BEST_SOLUTION.isActive = true;
              System.out.println("Select a printing option\n");
              System.out.println(String.format("Press (%s) to print as a raw", Commands.PRINT_RAW.commandVal));
              System.out.println(String.format("Press (%s) to print per a class", Commands.PRINT_PER_CLASS.commandVal));
              System.out.println(String.format("Press (%s) to print per a teacher", Commands.PRINT_PER_TEACHER.commandVal));
              userInput = sc.nextLine();
              if (userInput.equals(Commands.PRINT_RAW.commandVal))
                  Commands.PRINT_RAW.isActive = true;
              else if (userInput.equals(Commands.PRINT_PER_CLASS.commandVal))
                  Commands.PRINT_PER_CLASS.isActive = true;
              else if (userInput.equals(Commands.PRINT_PER_TEACHER.commandVal))
                  Commands.PRINT_PER_TEACHER.isActive = true;
              else
                  System.out.println("unknown command");

          }
          }
          else if (userInput.equals(Commands.SHOW_ALGORITHM_PROC.commandVal)) {
              Commands.SHOW_ALGORITHM_PROC.isActive=true;

          }
          else if (userInput.equals(Commands.EXIT.commandVal)) {
              Commands.EXIT.isActive=true;
          }
          else{
              System.out.println("invalid command, please try again:\n");
          }

          }
    }


