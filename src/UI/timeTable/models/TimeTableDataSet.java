package UI.timeTable.models;

import engine.models.EvolutionDataSet;
import engine.models.Solution;
import schema.models.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TimeTableDataSet implements EvolutionDataSet
{
    private int days;
    private int hours;
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
        for (ETTTeacher ettTeacher : ettTeachers) {
            try {
                Teacher teacher = new Teacher(ettTeacher);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    //TODO : add validation on "Subject" class constructor (in the set function) - Example in "Teacher" class
    public void setSubjects(List<ETTSubject> ettSubjects) {
        for (ETTSubject ettTeacher : ettSubjects) {
            //try {
                Subject subject = new Subject(ettSubjects);
            //}
            //catch (ValidationException e) {
            //    e.printStackTrace();
            //}
        }    }

    public List<Grade> getGrades() {
        return grades;
    }

    //TODO : add validation on "Grade" class constructor (in the set function) - Example in "Teacher" class
    public void setGrades(List<ETTClass> ettClasses) {
        for (ETTClass ettClass : ettClasses) {
            //try {
            Grade grade = new Grade(ettClass);
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
            for (ETTRule ettRule : ettRules) {
                //try {
                Rule rule = new Rule(ettRule);
                //}
                //catch (ValidationException e) {
                //    e.printStackTrace();
                //}
            }
        }

    @Override
    public Solution<Lesson> getRandomSolution() {

        Solution<Lesson> solution = new Solution<>();

        List<Integer> teachersIds = teachers.stream().map(Teacher::getId).collect(Collectors.toList());
        List<Integer> subjectsIds = subjects.stream().map(Subject::getId).collect(Collectors.toList());
        subjectsIds.add(-1); //add option of empty lesson

        Random rand = new Random();
        grades.forEach(grade -> {
            for (int d = 1; d <= days; d++) {
                for (int h = 1; h < hours; h++) {
                    int randomSubject = subjectsIds.get(rand.nextInt(subjectsIds.size()));
                    int randomTeacher = randomSubject == -1 ? -1 : teachersIds.get(rand.nextInt(teachersIds.size()));

                    Lesson lesson = new Lesson(grade.getId(), randomTeacher, randomSubject, d, h);
                    solution.addLesson(lesson);
                }
            }
        });
        return solution;
    }
}
