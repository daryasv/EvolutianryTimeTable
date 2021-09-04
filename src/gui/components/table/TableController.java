package gui.components.table;

import UI.models.Lesson;
import UI.models.timeTable.TimeTableMembers;
import engine.models.IRule;
import engine.models.Solution;
import engine.models.SolutionFitness;
import gui.logic.EngineLogic;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableController {

    @FXML private Label tableTitle;
    @FXML private GridPane gridTable;
    @FXML private Label ValidTableLable;
    @FXML private VBox tableDetailsVbox;

    private boolean isValidTable;

    private void insetDataToTable(String data, int tableGridRow, int tableGridCol){
        Label label=new Label(data);
        gridTable.add(label, tableGridCol,tableGridRow);
    }

    private void initializeTable(int totalDays, int totalHours){
        for (int i = 0; i < totalDays; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / totalDays-5);
            gridTable.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < totalHours; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / totalHours-5);
            gridTable.getRowConstraints().add(rowConst);
        }


    }

    private void createDaysInTable(int totalDays) {
        for (int i = 0; i < totalDays; i++) {
            int num = i+1;
            String dayTitle = "Day: " + Integer.toString(num);
            insetDataToTable( dayTitle, 0, i+1);
        }
    }

    private void createHoursInTable(int totalHours) {
            for(int i=0; i<totalHours;i++){
                int num = i+1;
                String hourTitle = "Hour: " + Integer.toString(num);
                insetDataToTable(hourTitle,i+1,0);
            }
    }


    private void buildTable(String typeTitle, int typeIdTitle,HashMap<Integer,List<String>> tableContentLst , int totalDays, int totalHours, SolutionFitness solutionDetails) {
        initializeTable(totalDays,totalHours);
        createDaysInTable(totalDays);
        createHoursInTable(totalHours);
        tableTitle.setText(typeTitle + "ID: " + typeIdTitle);
        for(int day=1; day<=totalDays;day++){
          List <String> lessonsInADay= tableContentLst.get(day);
          for(int i =0; i<lessonsInADay.size(); i++){
              insetDataToTable(lessonsInADay.get(i),i+1, day);
          }
}
        String validword= isValidTable? "":"not";
        String validMsg= String.format("This table is %s valid",validword);
        ValidTableLable.setText(validMsg);

        }


    public void showTable(String objectType, int objectId, Solution bestSolution, int totalDays, int totalHours, SolutionFitness globalSolution, TimeTableMembers solMembersDetails){
        HashMap<Integer,List<String>> lessonsToAdd = new HashMap<>();
        if(objectType.equals("Teacher")){
            lessonsToAdd= getLessonsContent("Class", objectId, bestSolution, totalDays, totalHours, solMembersDetails);
        }
        else if (objectType.equals("Class")){
            lessonsToAdd= getLessonsContent("Teacher", objectId, bestSolution, totalDays, totalHours, solMembersDetails);
        }
        buildTable(objectType, objectId,lessonsToAdd,totalDays,totalHours,globalSolution );
        }






    private HashMap<Integer, List<String>> getLessonsContent(String objectType, int typeTableId, Solution <Lesson> allLessons, int totalDays, int totalHours, TimeTableMembers solMembersDetails){
        isValidTable=true;
        String lessonsContent="";
        String lesson;
        Solution <Lesson> lessonsPerObject;
        if(objectType.equals("Class")){
            lessonsPerObject= getTeacherSolution(allLessons, typeTableId);
        }
        else{
            lessonsPerObject= getClassSolution(allLessons, typeTableId);
        }
        List<Lesson> dayHourSolution= new ArrayList<Lesson>();
        List<String> lessonsData = new ArrayList<>();
        HashMap<Integer, List<String>> lessonsPerDay = new HashMap<>();
        for(int day=1; day<=totalDays; day++) {
            List<String> allHoursLessonsInADay = new ArrayList<String>();
            for (int hour = 1; hour <= totalHours; hour++) {
                lesson="";
                dayHourSolution = getDayHourSolution(lessonsPerObject, day, hour);
                if(dayHourSolution.size()==0){
                    lessonsContent =" ";
                }
                else if (dayHourSolution.size() > 1) isValidTable = false;
                for (int i = 0; i < dayHourSolution.size(); i++) {
                    if (objectType.equals("Teacher")) {
                        lesson = String.format("%s %d %s, Subject %d %s", objectType, dayHourSolution.get(i).getTeacherId(),solMembersDetails.getTeachers().get(dayHourSolution.get(i).getTeacherId()).getName(), dayHourSolution.get(i).getSubjectId(),solMembersDetails.getSubjects().get(dayHourSolution.get(i).getSubjectId()).getName());

                    } else if (objectType.equals("Class")) {
                        lesson = String.format("%s %d %s, Subject %d %s ", objectType, dayHourSolution.get(i).getClassId(),solMembersDetails.getGrades().get(dayHourSolution.get(i).getClassId()).getName(), dayHourSolution.get(i).getSubjectId(),solMembersDetails.getSubjects().get(dayHourSolution.get(i).getSubjectId()).getName());

                    }
                    if(i>=1) lessonsContent+="\n";
                    lessonsContent += lesson;
                    }
                allHoursLessonsInADay.add(lessonsContent);

                lessonsData.add(lessonsContent);
                lessonsContent = "";
                }
            lessonsPerDay.put(day, allHoursLessonsInADay);
            }
        return lessonsPerDay;
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

    private Solution<Lesson> getClassSolution(Solution<Lesson> timeTableSolution, int classId){
        Solution<Lesson> solutionPerClass= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getClassId()==classId){
                solutionPerClass.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerClass;
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
