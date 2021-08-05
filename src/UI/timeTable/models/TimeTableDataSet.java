package UI.timeTable.models;

import UI.evolutionEngine.models.EvolutionEngineDataSet;
import UI.evolutionEngine.models.Mutation;
import engine.models.EvolutionDataSet;
import engine.models.Solution;
import schema.models.*;

import java.util.ArrayList;
import java.util.EventListenerProxy;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;


public class TimeTableDataSet implements EvolutionDataSet
{
    private final int days;
    private final int hours;
    private List<Teacher> teachers;
    private List<Subject> subjects;
    private List<Grade> grades;
    private List<Rule> rules;
    private ETTTimeTable timeTableMembers;

    public TimeTableDataSet(ETTTimeTable timeTableMembers) throws ValidationException {
        days = timeTableMembers.getDays();
        hours = timeTableMembers.getHours();
        setTeachers(timeTableMembers.getETTTeachers().getETTTeacher());
        setGrades(timeTableMembers.getETTClasses().getETTClass());
        setSubjects(timeTableMembers.getETTSubjects().getETTSubject());
        setRules(timeTableMembers.getETTRules().getETTRule());
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<ETTTeacher> ettTeachers) throws ValidationException {
        this.teachers = new ArrayList<Teacher>();
        for (ETTTeacher ettTeacher : ettTeachers) {
            try {
                Teacher teacher = new Teacher(ettTeacher);
                this.teachers.add(teacher);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runMutation(Solution <Lesson> child){
        List <Mutation> mutations = new EvolutionEngineDataSet().getMutation();
        double probability;
        int maxTupples;
        char component;
        for(int i=0; i<mutations.size();i++){
            probability=mutations.get(i).getProbability();
            maxTupples=mutations.get(i).getMaxTupples();
            component=mutations.get(i).getComponent();
            double randomNum = ThreadLocalRandom.current().nextDouble(0.0, 100.0);


            if(mutations.get(i).getName().equals(Mutation.MutationOperators.FLIP_OPERATOR.getOperatorName())){
                if(randomNum< probability)
                    runFlippingMutation(child,maxTupples, component);
            }
        }
    }

    public static void runFlippingMutation(Solution <Lesson> child, int maxTuples, char component){
        Random rand = new Random();
        int randomTuplesNum = rand.nextInt();
        for(int i=0; i<randomTuplesNum; i ++){
            if (i>maxTuples)
                break;
            else{
                int tupleIndex = rand.nextInt(child.getList().size());
                changeComponent(child.getList().get(tupleIndex), component);
            }
        }
    }

    public static void changeComponent(Lesson lesson, char component){
        int val=0;
        if(component=='C'){
            lesson.setClassId(val);
        }
        else if(component=='T'){
            lesson.setTeacherId(val);
        }
        else if(component=='D'){
            lesson.setDay(val);
        }
        else if(component=='H'){
            lesson.setHour(val);
        }
        else if(component=='S'){
            lesson.setSubjectId(val);
        }
    }
    public List<Subject> getSubjects() {
        return subjects;
    }

    //TODO : add validation on "Subject" class constructor (in the set function) - Example in "Teacher" class
    public void setSubjects(List<ETTSubject> ettSubjects) {
        this.subjects = new ArrayList<Subject>();
        for (ETTSubject ettTeacher : ettSubjects) {
            //try {
                Subject subject = new Subject(ettSubjects);
                this.subjects.add(subject);
            //}
            //catch (ValidationException e) {
            //    e.printStackTrace();
            //}
        }    }

    public List<Grade> getGrades() {
        return grades;
    }

    //TODO : add validation on "Grade" class constructor (in the set function) - Example in "Teacher" class
    public void setGrades(List<ETTClass> ettClasses) throws ValidationException {
        this.grades = new ArrayList<Grade>();
        for (ETTClass ettClass : ettClasses) {
            //try {
            Grade grade = new Grade(ettClass);
            this.grades.add(grade);
            //}
            //catch (ValidationException e) {
            //    e.printStackTrace();
            //}
        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    //TODO : add validation on "Rules" class constructor (in the set function) - Example in "Teacher" class
        private void setRules(List<ETTRule> ettRules)
        {
            this.rules = new ArrayList<Rule>();
            for (ETTRule ettRule : ettRules) {
                //try {
                Rule rule = new Rule(ettRule);
                this.rules.add(rule);
                //}
                //catch (ValidationException e) {
                //    e.printStackTrace();
                //}
            }
        }

    @Override
    public Solution<Lesson> getRandomSolution() {

        Solution<Lesson> solution = new Solution<>();

        List<Integer> teachersIds = this.teachers.stream().map(Teacher::getId).collect(Collectors.toList());
        List<Integer> subjectsIds = this.subjects.stream().map(Subject::getId).collect(Collectors.toList());
        subjectsIds.add(-1); //add option of empty lesson

        Random rand = new Random();
        grades.forEach(grade -> {
            for (int d = 1; d <= days; d++) {
                for (int h = 1; h <= hours; h++) {
                    int randomSubject = subjectsIds.get(rand.nextInt(subjectsIds.size()));
                    int randomTeacher = randomSubject == -1 ? -1 : teachersIds.get(rand.nextInt(teachersIds.size()));

                    Lesson lesson = new Lesson(grade.getId(), randomTeacher, randomSubject, d, h);
                    solution.addItemToList(lesson);
                }
            }
        });
        return solution;
    }

}
