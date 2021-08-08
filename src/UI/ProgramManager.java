package UI;

import UI.models.Lesson;
import UI.models.LessonSortType;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.TimeTableMembers;
import engine.Evolutionary;
import engine.models.Solution;
import engine.models.SolutionFitness;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ProgramManager {
    TimeTableDataSet timeTable;
    EvolutionConfig evolutionEngineDataSet;
    List<Solution<Lesson>> population;
    Solution<Lesson> timeTableSolution;
    Evolutionary<Lesson> evolutionary = null;

    public static enum systemSetting{
        IS_FILE_LOADED(false),SOLUTION_FOUND(true);
        boolean status;
        systemSetting(boolean status){
            this.status=status;
        }
    }
    public static String FILE_NAME;
    //public static String FILE_NAME = "src/resources/EX1-small.xml";

    public void manageProgram(){
        System.out.println("WELCOME!");
        UserMenu.Commands.EXIT.setStatus(false);
        while(!UserMenu.Commands.EXIT.getStatus()){
            UserMenu menu = new UserMenu();
            menu.getUserInput();
            runCommand();
        }
        this.exitProgram();

    }
    private void exitProgram(){
        System.out.println("THE PROGRAM CLOSED. SEE YOU SOON");
    }

    private void runCommand(){
        try {
            if (UserMenu.Commands.LOAD_TABLE_DETAILS.getStatus()) {
                UserMenu.Commands.LOAD_TABLE_DETAILS.setStatus(false);
                loadXMLFile();
            } else if (UserMenu.Commands.SHOW_TABLE_SETTINGS.getStatus()) {
                UserMenu.Commands.SHOW_TABLE_SETTINGS.setStatus(false);


            } else if (UserMenu.Commands.RUN_ALGORITHM.getStatus()) {
                UserMenu.Commands.RUN_ALGORITHM.setStatus(false);
                System.out.println("START: RUN ALGORITHM");
                System.out.println("Enter num of generations: ");
                Scanner scanner = new Scanner(System.in);
                int generations = scanner.nextInt();
                timeTable.setGenerations(generations);System.out.println("Enter interval of generations print: ");
                int interval = scanner.nextInt();
                timeTable.setGenerationsInterval(interval);
                runAlgorithm();
                System.out.println("END: RUN ALGORITHM");
            } else if (
                    UserMenu.Commands.SHOW_BEST_SOLUTION.getStatus()) {
                UserMenu.Commands.SHOW_BEST_SOLUTION.setStatus(false);
                int totalDays = timeTable.getTimeTableMembers().getDays();
                int totalHours = timeTable.getTimeTableMembers().getHours();
                printSolution(totalDays, totalHours);


            } else if (UserMenu.Commands.SHOW_ALGORITHM_PROC.getStatus()) {
                UserMenu.Commands.SHOW_ALGORITHM_PROC.setStatus(false);
                if(evolutionary!=null)
                    printAlgorithmProgress();
                else
                    System.out.println("NO ALGORITHM RUN YET");
            }
        }
        catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    private void loadXMLFile() throws ValidationException {
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
            throw new ValidationException("failed to load file, please try again");
        }
    }

    private void runAlgorithm() throws ValidationException {
        if (checkIfFileLoaded()) {
            evolutionary = new Evolutionary();

            evolutionary.run(timeTable);
            timeTableSolution = evolutionary.getGlobalBestSolution().getSolution();
        }
    }

    private void printAlgorithmProgress()
    {
        int genInterval = timeTable.getGenerationInterval();
        List<SolutionFitness<Lesson>> allSolutions = evolutionary.getBestSolutions();
        SolutionFitness<Lesson> currSolution = null;
        SolutionFitness<Lesson> prevSolution = null;
        System.out.println("START: SHOW ALGORITHM PROCESS (EVERY " +genInterval+ "GENERATIONS) :");
        for (int i=0; i< allSolutions.size();i++)
        {
            currSolution = allSolutions.get(i);
            if(prevSolution == null)
            {
                System.out.println("In Generation #1 Best Fitness is "+(currSolution.getFitness())+"");
            }
            else
            {
                double fitnessDiff = prevSolution.getFitness() - currSolution.getFitness();
                System.out.println("In Generation #"+(i*genInterval)+" Best Fitness is "+(currSolution.getFitness())+"," +
                        ", Distance of "+(fitnessDiff)+ "from the previous.");
            }
            prevSolution = currSolution;
        }
        System.out.println("END: SHOW ALGORITHM PROCESS");
    }

    private void updateDataSets(ETTDescriptor descriptor) throws ValidationException {
        timeTable = new TimeTableDataSet(descriptor);
        evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }

    public void printSolution( int totalDays, int totalHours){
        if(checkIfFileLoaded()){
            if(UserMenu.Commands.PRINT_RAW.getStatus()){
                UserMenu.Commands.PRINT_RAW.setStatus(false);
                 timeTableSolution=  timeTable.sort(timeTableSolution, LessonSortType.DayTimeOriented.name);
                printRaw();
            }
            else if(UserMenu.Commands.PRINT_PER_CLASS.getStatus()){
                UserMenu.Commands.PRINT_PER_CLASS.setStatus(false);
                timeTableSolution=  timeTable.sort(timeTableSolution, LessonSortType.TEACHER_ORIENTED.name);
                printPerClass();
            }
            else if (UserMenu.Commands.PRINT_PER_TEACHER.getStatus()){
                UserMenu.Commands.PRINT_PER_TEACHER.setStatus(false);
                timeTableSolution=  timeTable.sort(timeTableSolution, LessonSortType.CLASS_ORIENTED.name);
                printPerTeacher();
            }
            else{
                System.out.println("unknown printing type");
            }
        }

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
            if(teacher!=-1)
                System.out.println(String.format("Day:%d, hour:%d, classID:%d, teacherID:%d, subject:%d",day,hour, classId, teacher,subject));
        }
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
                    else if((curDay!=0)&&(curHour==0)){
                        printLesson(DayHourSol, curDay,"Day");
                    }
                    else{
                        printLesson(DayHourSol, classID,"Teacher");
                    }
                }
                System.out.printf("\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            }

            while ((classIndex<timeTableSolution.getList().size()-1)&&(classID==timeTableSolution.getList().get(classIndex+1).getClassId())){
                classIndex++;
            }
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
    public void printLesson(Solution<Lesson> lesson, int ID, String type){
        if(lesson.getList().size()==0){
            if(type.equals("Day")){
                System.out.printf(String.format("|%s: %d                      |", type,ID));
            }
            else if(type.equals("Hour")){
                System.out.printf(String.format("|%s: %d                     |", type,ID));
            }
            else{
                System.out.printf("|                            |");
            }
        }
        for(int i=0; i<lesson.getList().size(); i++){
            if(lesson.getList().get(i).getSubjectId()==-1){
                System.out.printf("|                            |");
            }
            else{
                if(type=="Teacher"){
                    System.out.printf(String.format("|%s: %d Subject: %d       |",type,lesson.getList().get(i).getTeacherId(),lesson.getList().get(i).getSubjectId()));
                }
                else if(type=="Class"){
                    System.out.printf(String.format("|%s: %d Subject: %d       |",type,lesson.getList().get(i).getClassId(),lesson.getList().get(i).getSubjectId()));
                }

            }
        }
    }
}

