package gui.components.table;

import UI.models.Lesson;
import engine.models.Solution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.Node.*;

import java.util.ArrayList;
import java.util.List;


public class TableController {

    @FXML
    private Label tableTitle;

    @FXML
    private GridPane gridTable;

    @FXML
    private Label ValidTableLable;

    private void insetDataToTable(String data, int tableGridRow, int tableGridCol){
        Label label=new Label(data);
        gridTable.add(label, tableGridRow, tableGridCol);
    }

    private void createDaysInTable(int totalDays) {
        for (int i = 0; i < totalDays; i++) {
            insetDataToTable("Day: " + i + 1, 0, i + 1);
        }
    }

    private void createHoursInTable(int totalHours) {
            for(int i=0; i<totalHours;i++){
                insetDataToTable("Hour: "+ i+1,i+1,0);
            }
    }

    private void buildTable(String typeTitle, String typeIdTitle, List<String> tableContentLst, int totalDays, int totalHours, boolean validTable) {
        int contentLstIndex=0;
        createDaysInTable(totalDays);
        createHoursInTable(totalHours);
        tableTitle.setText(typeTitle + "ID: " + typeTitle);
            for (int day = 0; day < totalDays; day++) {
                for(int hour=0;hour<totalHours; hour++){
                    insetDataToTable(tableContentLst.get(contentLstIndex), hour+1,day+1);
                    contentLstIndex++;
                }
            }
        ValidTableLable.setText(String.format("This Table is %s valid",validTable? "" :"Not" ));
        }

/*
    public void showTacherTable(int teacherId, Solution bestSolution, int totalDays, int totalHours){
        boolean isValidTable=true;
        List<Lesson> lessonsPerTeacher= getTeacherSolution(bestSolution, teacherId);
        List<String> lessonsToAdd= new ArrayList<String>();

        for(int day=0; day<totalDays; day++){
            for(int hour=0; hour<totalHours; hour++){

            }
        }
    }

    private List<String> getLessonsContent(int typeTableId, Solution <Lesson> allLessons, int totalDays, int totalHours){
        boolean isValidTable=true;
        String lessonsContent="";
        List<String> lessonsToAdd= new ArrayList<String>();
        List<Lesson> lessonsPerTeacher= getTeacherSolution(allLessons, typeTableId);
        List<Lesson> dayHourSolution= new ArrayList<Lesson>();
        List<String> myList = new ArrayList<>();
        for(int day=0; day<totalDays; day++){
            for(int hour=0; hour<totalHours; hour++){
                dayHourSolution=getDayHourSolution(lessonsPerTeacher, day, hour);
            }
        }

        return myList;
    }

*/

    public List<Lesson> getDayHourSolution(Solution<Lesson> solution,int day, int hour){
        Solution<Lesson> solutionPerTime= new Solution<Lesson>();
        for(int i=0; i<solution.getList().size(); i++){
            if((solution.getList().get(i).getDay()==day)&&(solution.getList().get(i).getHour()==hour)){
                solutionPerTime.getList().add(solution.getList().get(i));
            }
        }
        return solutionPerTime.getList();
    }

    private List<Lesson> getClassSolution(Solution<Lesson> timeTableSolution, int classId){
        Solution<Lesson> solutionPerClass= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getClassId()==classId){
                solutionPerClass.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerClass.getList();
    }

    public List<Lesson> getTeacherSolution(Solution<Lesson> timeTableSolution, int teacherId){
        Solution<Lesson> solutionPerTeacher= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getTeacherId()==teacherId){
                solutionPerTeacher.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerTeacher.getList();
    }

}
