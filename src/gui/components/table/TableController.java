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

    private boolean isValidTable;

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

    private void buildTable(String typeTitle, int typeIdTitle, List<String> tableContentLst, int totalDays, int totalHours) {
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
        ValidTableLable.setText(String.format("This Table is %s valid",isValidTable? "" :"Not" ));
        }


    public void showTable(String objectType, int objectId, Solution bestSolution, int totalDays, int totalHours){
        List<String> lessonsToAdd = new ArrayList<>();
        if(objectType.equals("Teacher")){
            lessonsToAdd= getLessonsContent("Class", objectId, bestSolution, totalDays, totalHours);
        }
        else if (objectType.equals("Class")){
            lessonsToAdd= getLessonsContent("Teacher", objectId, bestSolution, totalDays, totalHours);
        }
        buildTable(objectType, objectId,lessonsToAdd,totalDays,totalHours);
        }


    private List<String> getLessonsContent(String objectType, int typeTableId, Solution <Lesson> allLessons, int totalDays, int totalHours){
        isValidTable=true;
        String lessonsContent="";
        String lesson;
        List<String> lessonsToAdd= new ArrayList<String>();
        Solution <Lesson> lessonsPerTeacher= getTeacherSolution(allLessons, typeTableId);
        List<Lesson> dayHourSolution= new ArrayList<Lesson>();
        List<String> lessonsData = new ArrayList<>();
        for(int day=1; day<totalDays; day++) {
            for (int hour = 1; hour < totalHours; hour++) {
                lesson="";
                dayHourSolution = getDayHourSolution(lessonsPerTeacher, day, hour);
                if(dayHourSolution.size()==0){
                    lessonsContent =" ";
                }
                if (dayHourSolution.size() > 1) isValidTable = false;
                for (int i = 0; i < dayHourSolution.size(); i++) {
                    lessonsContent = "";
                    if (objectType.equals("Teacher")) {
                        lesson = String.format("%s: %d Subject: %d ", objectType, dayHourSolution.get(i).getTeacherId(), dayHourSolution.get(i).getSubjectId());

                    } else if (objectType.equals("Class")) {
                        lesson = String.format("%s: %d Subject: %d ", objectType, dayHourSolution.get(i).getClassId(), dayHourSolution.get(i).getSubjectId());

                    }
                    if(i>1) lessonsContent+="\n";
                    lessonsContent += lesson;
                    }
                lessonsData.add(lessonsContent);
                }
            }
        return lessonsData;
    }



    public List<Lesson> getDayHourSolution(Solution<Lesson> solution,int day, int hour){
        Solution<Lesson> solutionPerTime= new Solution<Lesson>();
        for(int i=0; i<solution.getList().size(); i++){
            if((solution.getList().get(i).getDay()==day)&&(solution.getList().get(i).getHour()==hour)){
                if(solution.getList().get(i).getTeacherId()!=-1&&solution.getList().get(i).getSubjectId()!=-1)
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

    public Solution <Lesson> getTeacherSolution(Solution<Lesson> timeTableSolution, int teacherId){
        Solution<Lesson> solutionPerTeacher= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getTeacherId()==teacherId){
                solutionPerTeacher.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerTeacher;
    }
}
