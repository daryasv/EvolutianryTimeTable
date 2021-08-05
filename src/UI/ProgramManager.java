package UI;

import UI.timeTable.models.Lesson;
import UI.timeTable.models.TimeTableDataSet;
import engine.models.Solution;

import javax.swing.*;
import java.awt.*;


public class ProgramManager {
    public void manageProgram(){
        UserMenu.Commands.EXIT.setStatus(false);
        while(!UserMenu.Commands.EXIT.getStatus()){
            Boolean isFileLoaded = false;
            UserMenu menu = new UserMenu();
           menu.getUserInput(isFileLoaded);
           if(UserMenu.Commands.RUN_ALGORITHM.getStatus()){
               Solution <Lesson> test = new Solution <Lesson>();
               TimeTableDataSet.runMutation(test);
           }
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
