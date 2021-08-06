package UI;

import UI.timeTable.models.Lesson;
import UI.timeTable.models.TimeTableDataSet;
import engine.models.Solution;

import javax.swing.*;
import java.awt.*;
import java.util.List;


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
          //  sortDayTimeOriented(); USE DARIA's FUNCTION FROM CROSSOVER
            printRaw(solution);
        }
        else if (printType==UserMenu.PRINT_PER_TEACHER){
           // printPerClass();
        }
        else if(printType==UserMenu.PRINT_PER_CLASS){
           // printPerTeacher();
        }
        else{
            System.out.println("unknown print type");
        }
    }


    private void printRaw(Solution<Lesson> solution){
        for(int i=0; i< solution.getList().size(); i++){
            int classId = solution.getList().get(i).getClassId();
            int teacher =solution.getList().get(i).getTeacherId();
            int subject =solution.getList().get(i).getSubjectId();
            int day =solution.getList().get(i).getDay();
            int hour =solution.getList().get(i).getHour();
             System.out.println(String.format("%d%d%d%d%d",day,hour, classId, teacher,subject));
        }
    }
    private void printPerClass(List<Lesson> lessons){

        for(int i=0; i<lessons.size();i++){
            int teacher =lessons.get(i).getTeacherId();
            int subject =lessons.get(i).getSubjectId();
            int day =lessons.get(i).getDay();
            int hour =lessons.get(i).getHour();
            System.out.println("____________________");
            System.out.println("|____________________|");

            System.out.println(String.format("|%d%d%d%d|",day,hour, teacher,subject));
            System.out.println("____________________");
        }
        //for(int i<)
    }
    private void printPerTeacher(List<Lesson> list){}
    public void sortDayTimeOriented(){}

}
