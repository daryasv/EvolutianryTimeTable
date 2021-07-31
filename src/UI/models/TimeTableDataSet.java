package UI.models;

import Engine.models.EvolutionDataSet;
import Engine.models.Solution;
import schema.models.ETTSubject;
import schema.models.ETTTeacher;
import schema.models.ETTTimeTable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TimeTableDataSet implements EvolutionDataSet
{

    private final ETTTimeTable timeTableMembers;

    public TimeTableDataSet(ETTTimeTable timeTable) {
        this.timeTableMembers = timeTable;
    }

    @Override
    public Solution<Lesson> getRandomSolution() {

        int days = timeTableMembers.getDays();
        int hours = timeTableMembers.getHours();

        Solution<Lesson> solution = new Solution<>();

        List<Integer> teachers = timeTableMembers.getETTTeachers().getETTTeacher()
                .stream().map(ETTTeacher::getId).collect(Collectors.toList());

        List<Integer> subjects = timeTableMembers.getETTSubjects().getETTSubject()
                .stream().map(ETTSubject::getId).collect(Collectors.toList());
        subjects.add(-1);

        Random rand = new Random();

        timeTableMembers.getETTClasses().getETTClass().forEach(ettClass -> {
            for (int day = 0; day < days; day++)
            {
                for (int hour = 0; hour < hours; hour++)
                {
                    int randomSubject = subjects.get(rand.nextInt(subjects.size()));
                    int randomTeacher = randomSubject==-1?-1: teachers.get(rand.nextInt(teachers.size()));

                    Lesson lesson = new Lesson(ettClass.getId(),randomTeacher,randomSubject,day, hour);
                    solution.addLesson(lesson);
                }
            }
        });
        return solution;
    }
}
