package UI;

import UI.models.Lesson;
import UI.models.LessonSortType;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.*;
import engine.Evolutionary;
import engine.models.IRule;
import engine.models.Solution;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ProgramManager {
    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    List<Solution<Lesson>> population;
    Solution<Lesson> timeTableSolution;
    Evolutionary<Lesson> evolutionary = null;

    public static enum systemSetting{
        IS_FILE_LOADED(false),SOLUTION_FOUND(false);
        boolean status;
        systemSetting(boolean status){
            this.status=status;
        }
    }
    public static String FILE_NAME;
    //public static String FILE_NAME = "src/resources/EX1-small.xml";

    public void manageProgram(){
        System.out.println("Welcome!");
        UserMenu.Commands.EXIT.setStatus(false);
        while(!UserMenu.Commands.EXIT.getStatus()){
            UserMenu menu = new UserMenu();
            menu.getUserInput();
            runCommand();
        }
        this.exitProgram();

    }
    private void exitProgram(){
        System.out.println("The program closed");
    }

    private void runCommand(){
        if(UserMenu.Commands.LOAD_TABLE_DETAILS.getStatus()){
            UserMenu.Commands.LOAD_TABLE_DETAILS.setStatus(false);
            loadXMLFile();
        }
        else if(UserMenu.Commands.SHOW_TABLE_SETTINGS.getStatus()){
            UserMenu.Commands.SHOW_TABLE_SETTINGS.setStatus(false);
            printSystemDetails();

        }
        else if(UserMenu.Commands.RUN_ALGORITHM.getStatus()){
            UserMenu.Commands.RUN_ALGORITHM.setStatus(false);

            System.out.println("Enter num of generations: ");
            Scanner scanner  = new Scanner(System.in);
            int generations = scanner.nextInt();
            System.out.println("Enter interval of generations print: ");
            int interval = scanner.nextInt();
            runAlgorithm(generations,interval);
        }
        else if(UserMenu.Commands.SHOW_BEST_SOLUTION.getStatus()){
            UserMenu.Commands.SHOW_BEST_SOLUTION.setStatus(false);
            int totalDays= timeTable.getTimeTableMembers().getDays();
            int totalHours= timeTable.getTimeTableMembers().getHours();
            printSolution(totalDays,totalHours);


        }
        else if(UserMenu.Commands.SHOW_ALGORITHM_PROC.getStatus()){
            UserMenu.Commands.SHOW_ALGORITHM_PROC.setStatus(false);
        }
    }
    private void loadXMLFile(){
        Scanner sc = new Scanner(System.in);
        FILE_NAME = sc.nextLine();
        try{
            //load xml file into ETT classes
            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
            updateDataSets(descriptor);
            systemSetting.IS_FILE_LOADED.status=true;
            System.out.println("File Loaded Successfully!\n");

        } catch (JAXBException e) {
            systemSetting.IS_FILE_LOADED.status=false;
            System.out.println("failed to load file, please try again");
        }
    }

    private void runAlgorithm(int generations,int interval){
        if(checkIfFileLoaded()){
            try{
                evolutionary = new Evolutionary();
                timeTable.setGenerations(generations);
                timeTable.setGenerationsInterval(interval);
                evolutionary.run(timeTable);
                timeTableSolution = evolutionary.getGlobalBestSolution().getSolution();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            systemSetting.SOLUTION_FOUND.status=true;
        }
    }

    private void updateDataSets(ETTDescriptor descriptor){
        try{
            timeTable = new TimeTableDataSet(descriptor);
            evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

    public void printSolution(int totalDays, int totalHours){
        
        if(checkIfFileLoaded()){
            printBestSolutionDetails();
            if(UserMenu.Commands.PRINT_RAW.getStatus()){
                UserMenu.Commands.PRINT_RAW.setStatus(false);
               timeTableSolution= timeTable.sort(timeTableSolution,LessonSortType.DayTimeOriented.name);
                printRaw();
            }
            else if(UserMenu.Commands.PRINT_PER_CLASS.getStatus()){
                UserMenu.Commands.PRINT_PER_CLASS.setStatus(false);
                timeTableSolution= timeTable.sort(timeTableSolution,LessonSortType.CLASS_ORIENTED.name);
                printPerClass();
            }
            else if (UserMenu.Commands.PRINT_PER_TEACHER.getStatus()){
                UserMenu.Commands.PRINT_PER_TEACHER.setStatus(false);
                timeTableSolution= timeTable.sort(timeTableSolution,LessonSortType.TEACHER_ORIENTED.name);
              //1  printPerTeacher();
            }
            else{
                System.out.println("unknown printing type");
            }
        }

    }
    public void reviewRules(){
        System.out.println("\nRule's:\n");
        HashMap<IRule, Double> rulesFitness =evolutionary.getGlobalBestSolution().getRulesFitness();
        for (Map.Entry<IRule, Double> entry : rulesFitness.entrySet()){
            System.out.println(String.format("Rule name: %s ", entry.getKey().getName()));
            if(entry.getKey().isHard())
              System.out.println("Rule type: hard");
            else{
                System.out.println("Rule type: soft");
            }
            System.out.println(String.format("Rule grade: %,.1f ", entry.getValue()));
        }
    }

    public void printBestSolutionDetails(){
        double fitnessValue =evolutionary.getGlobalBestSolution().getFitness();
        double softRulesAVG = evolutionary.getGlobalBestSolution().getSoftRulesAvg();
        double hardRulesAVG = evolutionary.getGlobalBestSolution().getHardRulesAvg();
        System.out.println("\nSolution's Details:\n");
        System.out.println(String.format("The fitness value of this solution is: %,.2f", fitnessValue));
        reviewRules();
        System.out.println(String.format("The soft rules avg is: %,.1f", softRulesAVG));
        System.out.println(String.format("The hard rules avg is: %,.1f\n", hardRulesAVG));
    }

    public static boolean checkIfSolutionFound(){
        if(systemSetting.SOLUTION_FOUND.status){
            return true;
        }
        System.out.println("solution has not found\n");
        return false;
    }

    public static boolean checkIfFileLoaded(){
        if(systemSetting.IS_FILE_LOADED.status){
            return true;
        }
        System.out.println("There is no file loaded in the system\n");
        return false;
    }
    private void printRaw(){
        for(int i=0; i< timeTableSolution.getList().size(); i++){
            int classId = timeTableSolution.getList().get(i).getClassId();
            int teacher =timeTableSolution.getList().get(i).getTeacherId();
            int subject =timeTableSolution.getList().get(i).getSubjectId();
            int day =timeTableSolution.getList().get(i).getDay();
            int hour =timeTableSolution.getList().get(i).getHour();
            if(teacher!=-1&&subject!=-1)
                System.out.println(String.format("Day:%d, hour:%d, classID:%d, teacherID:%d, subject:%d",day,hour, classId, teacher,subject));
        }
    }


    private void printClassStatus(int classID){
        System.out.println(String.format("\nClass ID: %s", classID));
    }

    private void printPerTeacher(){
        int totalDays= timeTable.getTimeTableMembers().getDays();
        int totalHours= timeTable.getTimeTableMembers().getHours();
        for(int TeacherIndex=0; TeacherIndex<timeTableSolution.getList().size(); TeacherIndex++){
            int teacherID = timeTableSolution.getList().get(TeacherIndex).getTeacherId();
            Solution TeacherSolution= getClassSolution(teacherID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(TeacherSolution,curDay,curHour);

                    if((curDay==0)&&(curHour!=0)){
                        printLesson(DayHourSol, curHour,"Hour");
                    }
                    else if(((curDay==0)&&(curHour==0))){
                        System.out.println("       ");
                    }
                    else if((curDay!=0)&&(curHour==0)){
                        printLesson(DayHourSol, curDay,"Day");
                    }
                    else{
                        printLesson(DayHourSol, teacherID,"Class");
                    }
                }
            }
            while (teacherID==timeTableSolution.getList().get(TeacherIndex+1).getTeacherId()){
                TeacherIndex++;
            }
        }
    }
    public Solution<Lesson> getClassSolution(int classId){
        Solution<Lesson> solutionPerClass= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getClassId()==classId){
                solutionPerClass.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerClass;
    }

    public Solution<Lesson> getTeacherSolution(int teacherId){
        Solution<Lesson> solutionPerTeacher= new Solution<Lesson>();
        for(int i=0; i<timeTableSolution.getList().size(); i++){
            if(timeTableSolution.getList().get(i).getTeacherId()==teacherId){
                solutionPerTeacher.getList().add(timeTableSolution.getList().get(i));
            }
        }
        return solutionPerTeacher;
    }
    public Solution<Lesson> getDayHourSolution(Solution<Lesson> solution,int day, int hour){
        Solution<Lesson> solutionPerTime= new Solution<Lesson>();
        for(int i=0; i<solution.getList().size(); i++){
            if((solution.getList().get(i).getDay()==day)&&(solution.getList().get(i).getHour()==hour)){
                solutionPerTime.getList().add(solution.getList().get(i));
            }
        }
        return solutionPerTime;
    }

    private void printPerClass()
    {
        int totalDays= timeTable.getTimeTableMembers().getDays();
        int totalHours= timeTable.getTimeTableMembers().getHours();

        for(int classIndex=0; classIndex<timeTableSolution.getList().size(); classIndex++){
            int classID = timeTableSolution.getList().get(classIndex).getClassId();
            printClassStatus(classID);
            Solution classSolution= getClassSolution(classID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(classSolution,curDay,curHour);
                    if((curDay==0)&&(curHour!=0)){
                        printLesson(DayHourSol, curHour,"Hour");
                    }
                    else if(((curDay==0)&&(curHour==0))){
                        System.out.printf("%-9s","");
                    }
                    else if((curDay!=0)&&(curHour==0)){
                        printLesson(DayHourSol, curDay,"Day");
                    }
                    else{
                        printLesson(DayHourSol, classID,"Teacher");
                    }

                }
                System.out.printf("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            }

            while ((classIndex<timeTableSolution.getList().size()-1)&&(classID==timeTableSolution.getList().get(classIndex+1).getClassId())){
                classIndex++;
            }
        }
    }
    public void printLesson(Solution<Lesson> lesson, int ID, String type){

        System.out.printf(String.format("%s","|"));
        boolean isValidLesson=true;
        if(lesson.getList().size()==0){
            String msg="";
            if((type.equals("Day"))||(type.equals("Hour"))){
                 msg = String.format("%s: %d", type,ID);
            }
            else{
                isValidLesson=false;
            }
            if(type.equals("Hour")){
                System.out.printf("%-8s",msg);
            }
            else{
                System.out.printf("%-60s",msg);
            }
        }
        else{
            String msg="";
            String allLessons="";
            for(int i=0; i<lesson.getList().size(); i++){
                msg="";
                if((lesson.getList().get(i).getSubjectId()==-1)||(lesson.getList().get(i).getTeacherId()==-1)){
                    if(lesson.getList().size()>1){
                        msg="-1";
                    }
                }
                else{
                    if(type=="Teacher"){
                        msg=String.format("%s: %d Subject: %d ",type,lesson.getList().get(i).getTeacherId(),lesson.getList().get(i).getSubjectId());

                    }
                    else if(type=="Class"){
                        msg=String.format("%s: %d Subject: %d ",type,lesson.getList().get(i).getClassId(),lesson.getList().get(i).getSubjectId());

                    }

                }
                if(!msg.equals("-1")){
                    if((lesson.getList().size()>1)&&(i!=0)){
                        allLessons+=",";
                    }
                    allLessons+=msg;
                }
            }
            if(isValidLesson)
                System.out.printf("%-60s",allLessons);

        }
        if(isValidLesson){
            System.out.printf(String.format("%s","|"));
        }
    }



    public void printSystemDetails(){
        System.out.println("\n***Time Table Details***\n");
        HashMap<Integer, Subject> subjects = timeTable.getTimeTableMembers().getSubjects();
        HashMap<Integer, Teacher> teachers= timeTable.getTimeTableMembers().getTeachers();
        HashMap<Integer, Grade> grades = timeTable.getTimeTableMembers().getGrades();
        List<Rule> rules = timeTable.getTimeTableMembers().getRules();
        String selectionType = evolutionEngineDataSet.getSelection().getType().name;
        int populationCount= evolutionEngineDataSet.getInitialPopulation();
        System.out.println("-Subjects-");
        for(Map.Entry<Integer, Subject > entry : subjects.entrySet()){
            System.out.println(String.format("ID:%d, Name:%s", entry.getKey(),entry.getValue().getName()));
        }
        System.out.printf("\n");
        printTeachers(teachers,subjects);
        printGrades(grades,subjects);
        printRules(rules);
        System.out.println("\n***evolution Details***\n");
        System.out.println(String.format("\npopulation count:%d", populationCount));
        System.out.println(String.format("selection type:%s", selectionType));
        printMutationDetails();
        printCrossoverDetails();


    }
    private void printTeachers(HashMap<Integer, Teacher> teachers,HashMap<Integer, Subject> subjects){
        System.out.println("-Teachers-");
        for(Map.Entry<Integer, Teacher > entry : teachers.entrySet()){
            System.out.println(String.format("Teacher ID:%d", entry.getKey()));
            System.out.println(String.format("Teaching subjects:"));
            for(int i=0; i<entry.getValue().getSubjectsIdsList().size();i++){
                int subjectID=entry.getValue().getSubjectsIdsList().get(i);
                System.out.printf(String.format("subject ID:%d ", subjectID));
                System.out.println(String.format("subject name:%s", subjects.get(subjectID).getName()));
            }
            System.out.printf("\n");
        }
    }

    private void printGrades(HashMap<Integer, Grade> grades,HashMap<Integer, Subject> subjects){
        System.out.println("-Grades-");
    for(Map.Entry<Integer, Grade> entry : grades.entrySet()){
    System.out.println(String.format("\nGrade ID:%d", entry.getKey()));

    for(Map.Entry<Integer, Integer> required : entry.getValue().getRequirements().entrySet()){
        System.out.printf(String.format("Subject ID:%d, name: %s ", required.getKey(),subjects.get(required.getKey()).getName()));
        System.out.println(String.format(", required hours:%d", required.getValue()));
    }

}

    System.out.println("\n");
    }
    private void printRules(List<Rule> rules){
        System.out.println("-Rules-");
        for(int i=0; i<rules.size();i++){
            if(rules.get(i).isHard())
                 System.out.println(String.format("rule name:%s, type:Hard", rules.get(i).getName()));
            else{
                System.out.println(String.format("rule name:%s, type:Soft", rules.get(i).getName()));
            }
        }
    }

    private void printMutationDetails(){
        System.out.println("\n-Mutations-");
        for(int i=0; i<evolutionEngineDataSet.getMutations().size();i++){
            System.out.println(String.format("mutation type:%s, probability:%.2f, component:%c", evolutionEngineDataSet.getMutations().get(i).getName(),evolutionEngineDataSet.getMutations().get(i).getProbability(),evolutionEngineDataSet.getMutations().get(i).getComponent()));
        }
    }

    private void printCrossoverDetails(){
        System.out.println("\n-Crossover-");
        String crossoverType= evolutionEngineDataSet.getCrossover().getName().name;
       int cuttingPoints= evolutionEngineDataSet.getCrossover().getCuttingPoints();
       System.out.println(String.format("Crossover type:%s, num of cutting points:%d",crossoverType,cuttingPoints));
    }
}

