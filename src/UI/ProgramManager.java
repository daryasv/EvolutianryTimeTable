package UI;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import UI.models.timeTable.TimeTableMembers;
import engine.models.Solution;

import java.util.List;


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


    public void printSolution(int printType, Solution<Lesson> solution, int totalDays, int totalHours){
        if(printType== UserMenu.PRINT_RAW){
            //  sortDayTimeOriented(); USE DARIA's FUNCTION FROM CROSSOVER
            printRaw(solution);
        }
        else if (printType==UserMenu.PRINT_PER_TEACHER){
            //sortTeacherOriented()
            // printPerClass();
        }
        else if(printType==UserMenu.PRINT_PER_CLASS){
            //sortClassOriented()
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
    private void printPerClass(Solution <Lesson> solution, int totalDays, int totalHours, int totalClasses)  {
        for(int classIndex=0; classIndex<totalClasses; classIndex++){
            int classID = solution.getList().get(classIndex).getClassId();
            Solution classSolution= getClassSolution(solution,classID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(classSolution,curDay,curHour);
                   printLesson(DayHourSol, classID,"Class");
                }
            }
        }
    }
    private void printPerTeacher(Solution <Lesson> solution, int totalDays, int totalHours, int totalTeachers){
        for(int TeacherIndex=0; TeacherIndex<totalTeachers; TeacherIndex++){
            int teacherID = solution.getList().get(TeacherIndex).getTeacherId();
            Solution TeacherSolution= getClassSolution(solution,teacherID);
            for(int curHour=0; curHour<totalHours+1; curHour++){
                for(int curDay=0; curDay<totalDays+1; curDay++){
                    Solution DayHourSol=  getDayHourSolution(TeacherSolution,curDay,curHour);
                    printLesson(DayHourSol, teacherID,"Teacher");
                }
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
            System.out.println(String.format("%s: %d Subject: %d",type,ID,lesson.getList().get(i).getSubjectId()));
        }
        System.out.println("-----------------------------");
    }
}

