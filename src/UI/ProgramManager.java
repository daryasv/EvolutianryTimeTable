package UI;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.TimeTableMembers;
import engine.Evolutionary;
import engine.models.Solution;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ProgramManager {
    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    List<Solution<Lesson>> population;
    Solution<Lesson> timeTableSolution;

    public static enum systemSetting{
        IS_FILE_LOADED(false);
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


        }
        else if(UserMenu.Commands.RUN_ALGORITHM.getStatus()){
            UserMenu.Commands.RUN_ALGORITHM.setStatus(false);
            runAlgorithm();
        }
        else if(UserMenu.Commands.SHOW_BEST_SOLUTION.getStatus()){
            UserMenu.Commands.SHOW_BEST_SOLUTION.setStatus(false);
            int totalDays= timeTable.getTimeTableMembers().getDays();
            int totalHours= timeTable.getTimeTableMembers().getHours();
            if(UserMenu.Commands.PRINT_RAW.getStatus()){
                UserMenu.Commands.PRINT_RAW.setStatus(false);
                printSolution(UserMenu.Commands.PRINT_RAW.getCommandValue(), timeTableSolution,totalDays,totalHours);
            }

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

    private void runAlgorithm(){
        if(checkIfFileLoaded()){
            try{
                Evolutionary evolutionary = evolutionary = new Evolutionary();
                population = evolutionary.generatePopulation(evolutionEngineDataSet.getInitialPopulation(), timeTable);
                //demo for the best solution
                timeTableSolution = population.get(0);
                List<Lesson> lessons = timeTableSolution.getList().stream().filter(l -> l.getTeacherId() == 1).collect(Collectors.toList());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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

    public void printSolution(String printType, Solution<Lesson> solution, int totalDays, int totalHours){
        if(checkIfFileLoaded()){
            if(printType.equals( UserMenu.Commands.PRINT_RAW.getCommandValue())){
                //  sortDayTimeOriented(); USE DARIA's FUNCTION FROM CROSSOVER
                printRaw(solution);
            }
            else if (printType.equals(UserMenu.Commands.PRINT_PER_CLASS.getCommandValue())){
                //sortTeacherOriented()
                 printPerClass(solution);
            }
            else if(printType.equals(UserMenu.Commands.PRINT_PER_TEACHER.getCommandValue())){
                //sortClassOriented()
                 printPerTeacher(solution);
            }
            else{
                System.out.println("unknown print type");
            }
        }

    }

    public static boolean checkIfFileLoaded(){
        if(systemSetting.IS_FILE_LOADED.status){
            return true;
        }
        System.out.println("There is no file loaded in the system\n");
        return false;
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
    private void printPerClass(Solution <Lesson> solution)
    {
        int totalDays= timeTable.getTimeTableMembers().getDays();
        int totalHours= timeTable.getTimeTableMembers().getHours();

        for(int classIndex=0; classIndex<solution.getList().size(); classIndex++){
            int classID = solution.getList().get(classIndex).getClassId();
            Solution classSolution= getClassSolution(solution,classID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(classSolution,curDay,curHour);
                   printLesson(DayHourSol, classID,"Class");
                }
            }
            while (classID==solution.getList().get(classIndex+1).getTeacherId()){
                classIndex++;
            }
        }
    }

    private void printPerTeacher(Solution <Lesson> solution){
        int totalDays= timeTable.getTimeTableMembers().getDays();
        int totalHours= timeTable.getTimeTableMembers().getHours();
        for(int TeacherIndex=0; TeacherIndex<solution.getList().size(); TeacherIndex++){
            int teacherID = solution.getList().get(TeacherIndex).getTeacherId();
            Solution TeacherSolution= getClassSolution(solution,teacherID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(TeacherSolution,curDay,curHour);
                    printLesson(DayHourSol, teacherID,"Teacher");
                }
            }
            while (teacherID==solution.getList().get(TeacherIndex+1).getTeacherId()){
                TeacherIndex++;
            }
        }
    }
    public Solution<Lesson> getClassSolution(Solution<Lesson> solution,int classId){
        Solution<Lesson> solutionPerClass= new Solution<Lesson>();
        for(int i=0; i<solution.getList().size(); i++){
            if(solution.getList().get(i).getClassId()==classId){
                solutionPerClass.getList().add(solution.getList().get(i));
            }
        }
        return solutionPerClass;
    }

    public Solution<Lesson> getTeacherSolution(Solution<Lesson> solution,int teacherId){
        Solution<Lesson> solutionPerTeacher= new Solution<Lesson>();
        for(int i=0; i<solution.getList().size(); i++){
            if(solution.getList().get(i).getTeacherId()==teacherId){
                solutionPerTeacher.getList().add(solution.getList().get(i));
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
    public void printLesson(Solution<Lesson> lesson, int ID, String type){
        if(lesson.getList().size()==0){
            System.out.println("-----------------------------");
            System.out.println("|                            |");
        }
        for(int i=0; i<lesson.getList().size(); i++){
            System.out.println("-----------------------------");
            System.out.println(String.format("|%s: %d Subject: %d|",type,ID,lesson.getList().get(i).getSubjectId()));
        }
        System.out.println("-----------------------------");
    }
}

