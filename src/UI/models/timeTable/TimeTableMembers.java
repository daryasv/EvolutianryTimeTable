package UI.models.timeTable;

import UI.ValidationException;
import UI.models.Lesson;
import engine.models.Solution;
import schema.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TimeTableMembers
{
    private int days;
    private int hours;
    private HashMap<Integer,Teacher> teachers;
    private HashMap<Integer,Subject> subjects;
    private HashMap<Integer,Grade> grades;
    private List<Rule> rules;
    private int hardRulesWeight;

    public TimeTableMembers(ETTTimeTable timeTableMembers) throws ValidationException {
        setDays(timeTableMembers.getDays());
        setHours(timeTableMembers.getHours());
        setTeachers(timeTableMembers.getETTTeachers().getETTTeacher());
        setGrades(timeTableMembers.getETTClasses().getETTClass());
        setSubjects(timeTableMembers.getETTSubjects().getETTSubject());
        setRules(timeTableMembers.getETTRules().getETTRule());
        setHardRulesWeight(timeTableMembers.getETTRules().getHardRulesWeight());
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    public void setHardRulesWeight(int hardRulesWeight) {
        this.hardRulesWeight = hardRulesWeight;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public HashMap<Integer,Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<ETTTeacher> ettTeachers) throws ValidationException {
        this.teachers = new HashMap<>();
        for (ETTTeacher ettTeacher : ettTeachers) {
            try {
                Teacher teacher = new Teacher(ettTeacher);
                this.teachers.put(teacher.getId(),teacher);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<Integer,Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<ETTSubject> ettSubjects) throws ValidationException {
        this.subjects = new HashMap<>();
        for (ETTSubject ettSubject : ettSubjects) {
            Subject subject = new Subject(ettSubject);
            this.subjects.put(subject.getId(),subject);
        }
    }

    public HashMap<Integer,Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<ETTClass> ettClasses) throws ValidationException {
        this.grades = new HashMap<>();
        for (ETTClass ettClass : ettClasses) {
            Grade grade = new Grade(ettClass);
            this.grades.put(grade.getId(),grade);
        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    private void setRules(List<ETTRule> ettRules) throws ValidationException {
        this.rules = new ArrayList<Rule>();
        for (ETTRule ettRule : ettRules) {
            Rule rule = new Rule(ettRule);
            this.rules.add(rule);
        }
    }

    public Solution<Lesson> generateRandomSolution() {

        Solution<Lesson> solution = new Solution<>();

        List<Integer> teachersIds = new ArrayList<>(this.teachers.keySet());
        List<Integer> subjectsIds = new ArrayList<>(this.subjects.keySet());;
        subjectsIds.add(-1); //add option of empty lesson

        Random rand = new Random();
        grades.keySet().forEach(grade -> {
            for (int d = 1; d <= days; d++) {
                for (int h = 1; h <= hours; h++) {
                    int randomSubject = subjectsIds.get(rand.nextInt(subjectsIds.size()));
                    int randomTeacher = randomSubject == -1 ? -1 : teachersIds.get(rand.nextInt(teachersIds.size()));

                    Lesson lesson = new Lesson(grade, randomTeacher, randomSubject, d, h);
                    solution.addItemToList(lesson);
                }
            }
        });
        return solution;
    }
}
