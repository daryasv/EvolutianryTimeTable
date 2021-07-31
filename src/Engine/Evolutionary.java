package Engine;

import Engine.models.Lesson;
import Engine.models.Solution;
import schema.models.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolutionary {

    private final ETTDescriptor descriptor;
    public Evolutionary(ETTDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<Solution> generatePopulation(){
        int sizeOfPopulation = descriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
        List<Solution> solutions = new ArrayList<>();
        for (int i = 0; i < sizeOfPopulation; i++)
        {
            solutions.add(generateSolution());
        }

        return solutions;
    }

    private Solution generateSolution()
    {
        ETTTimeTable timeTable = descriptor.getETTTimeTable();

        int days = timeTable.getDays();
        int hours = timeTable.getHours();

        Solution solution = new Solution(days, hours);

        List<Integer> teachers = descriptor.getETTTimeTable().getETTTeachers().getETTTeacher()
                .stream().map(ETTTeacher::getId).collect(Collectors.toList());

        List<Integer> subjects = descriptor.getETTTimeTable().getETTSubjects().getETTSubject()
                .stream().map(ETTSubject::getId).collect(Collectors.toList());
        subjects.add(-1);

        Random rand = new Random();

        timeTable.getETTClasses().getETTClass().forEach(ettClass -> {
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
