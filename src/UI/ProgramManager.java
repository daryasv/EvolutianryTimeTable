package UI;

import Engine.models.Solution;
import UI.models.Lesson;

import javax.swing.*;


public class ProgramManager {
    public void manageProgram(){
        UserMenu.Commands.EXIT.setStatus(false);
        while(!UserMenu.Commands.EXIT.getStatus()){
            Boolean isFileLoaded = false;
            UserMenu menu = new UserMenu();
           menu.getUserInput(isFileLoaded);
        }
        this.exitProgram();

    }
    private void exitProgram(){
        System.out.println("The program closed");
    }


    public void printSolution(int printType, Solution<Lesson> solution){
        if(printType== UserMenu.PRINT_RAW){
            sortDayTimeOriented();
            //printRaw();
        }
        else if (printType==UserMenu.PRINT_PER_TEACHER){
            printPerClass();
        }
        else if(printType==UserMenu.PRINT_PER_CLASS){
            printPerTeacher();
        }
        else{
            System.out.println("unknown print type");
        }
    }

    private void printRaw(Solution<Lesson> solution){
        for(int i=0; i< solution.getList().size(); i++){
            // System.out.println(String.format(<%d%d%d%d%d,));
        }
    }
    private void printPerClass(){}
    private void printPerTeacher(){}
    public void sortDayTimeOriented(){}

}
