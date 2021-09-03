package gui.components.table;

import UI.models.Lesson;
import engine.models.Solution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TableController {

    @FXML private Label tableTitle;
    @FXML private GridPane gridTable;
    @FXML private Label ValidTableLable;

    private boolean isValidTable;

    private void insetDataToTable(String data, int tableGridRow, int tableGridCol){
        Label label=new Label(data);
        gridTable.add(label, tableGridCol,tableGridRow);
    }

    private void initializeTable(int totalDays, int totalHours){
        gridTable.setMinSize(100,100);
        gridTable.setMaxSize(100,100);

        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                Label label=new Label("dar eini");
                TextField txt =new TextField("dar eini");
                txt.setMinSize(300,300);
                label.setMinWidth(300);
              //  label.setPrefWidth(1000);
            //    label.setPrefHeight(1000);
                label.setMinHeight(300);
                gridTable.add(txt, i, j);
            }
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

    private void buildTable(String typeTitle, int typeIdTitle,HashMap<Integer,List<String>> tableContentLst , int totalDays, int totalHours) {
        int contentLstIndex=0;
        createDaysInTable(totalDays);
        createHoursInTable(totalHours);
        tableTitle.setText(typeTitle + "ID: " + typeIdTitle);
        for(int day=1; day<=totalDays;day++){
          List <String> lessonsInADay= tableContentLst.get(day);
          for(int i =0; i<lessonsInADay.size(); i++){
              insetDataToTable(lessonsInADay.get(i),i+1, day);
          }
}
        }


    public void showTable(String objectType, int objectId, Solution bestSolution, int totalDays, int totalHours){
        HashMap<Integer,List<String>> lessonsToAdd = new HashMap<>();
        if(objectType.equals("Teacher")){
            lessonsToAdd= getLessonsContent("Class", objectId, bestSolution, totalDays, totalHours);
        }
        else if (objectType.equals("Class")){
            lessonsToAdd= getLessonsContent("Teacher", objectId, bestSolution, totalDays, totalHours);
        }
        buildTable(objectType, objectId,lessonsToAdd,totalDays,totalHours);
        }


    private HashMap<Integer, List<String>> getLessonsContent(String objectType, int typeTableId, Solution <Lesson> allLessons, int totalDays, int totalHours){
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
                        lesson = String.format("%s: %d Subject: %d ", objectType, dayHourSolution.get(i).getTeacherId(), dayHourSolution.get(i).getSubjectId());

                    } else if (objectType.equals("Class")) {
                        lesson = String.format("%s: %d Subject: %d ", objectType, dayHourSolution.get(i).getClassId(), dayHourSolution.get(i).getSubjectId());

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
