package UI.models;

import UI.ValidationException;
import UI.models.evolution.*;
import UI.models.timeTable.*;
import engine.models.*;
import schema.models.ETTDescriptor;

import java.util.*;

import java.util.concurrent.ThreadLocalRandom;


public class TimeTableDataSet implements EvolutionDataSet<Lesson> {


    final private TimeTableMembers timeTableMembers;
    final private EvolutionConfig evolutionConfig;

    public TimeTableDataSet(ETTDescriptor descriptor) throws ValidationException {
        this.timeTableMembers = new TimeTableMembers(descriptor.getETTTimeTable());
        this.evolutionConfig = new EvolutionConfig(descriptor.getETTEvolutionEngine());
    }


    public TimeTableMembers getTimeTableMembers() {
        return timeTableMembers;
    }

    public EvolutionConfig getEvolutionConfig() {
        return evolutionConfig;
    }

    @Override
    public void mutation(Solution <Lesson> child) {

        List<Mutation> mutations = evolutionConfig.getMutations();
        double probability;
        int maxTuples;
        char component;
        for (int i = 0; i < mutations.size(); i++) {
            probability = mutations.get(i).getProbability();
            maxTuples = mutations.get(i).getMaxTupples();
            component = mutations.get(i).getComponent();
            double randomNum = ThreadLocalRandom.current().nextDouble(0.0, 100.0);


            if (mutations.get(i).getName().equals(Mutation.MutationOperators.FLIP_OPERATOR.getOperatorName())) {
                if (randomNum < probability)
                    runFlippingMutation(child, maxTuples, component);
            }
        }
    }

    private void runFlippingMutation(Solution <Lesson> child, int maxTuples, char component){
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

    private void changeComponent(Lesson lesson, char component){
        if(component=='C'){
          int classCount=this.timeTableMembers.getGrades().size();
            Random rand = new Random();
            int randomIndex = rand.nextInt(classCount);
            int changedID= this.timeTableMembers.getGrades().get(randomIndex).getId();
            lesson.setClassId(changedID);
        }
        else if(component=='T'){
            int TeachersCount=this.timeTableMembers.getTeachers().size();
            Random rand = new Random();
            int randomIndex = rand.nextInt(TeachersCount);
            int changedID= this.timeTableMembers.getTeachers().get(randomIndex).getId();
            lesson.setTeacherId(changedID);
        }
        else if(component=='D'){
            int DaysCount=this.timeTableMembers.getDays();
            Random rand = new Random();
            int changedVal= rand.nextInt(DaysCount)+1;
            lesson.setDay(changedVal);
        }
        else if(component=='H'){
            int HoursCount=this.timeTableMembers.getHours();
            Random rand = new Random();
            int changedVal= rand.nextInt(HoursCount)+1;
            lesson.setHour(changedVal);
        }
        else if(component=='S'){
            int subjectsCount=this.timeTableMembers.getSubjects().size();
            Random rand = new Random();
            int randomIndex = rand.nextInt(subjectsCount);
            int changedVal= this.timeTableMembers.getSubjects().get(randomIndex).getId();
            lesson.setClassId(changedVal);
        }
    }

    @Override
    public int getPopulationSize() {
        return evolutionConfig.getInitialPopulation();
    }

    //TODO: implement method
    @Override
    public int getGenerations() {
        return evolutionConfig.getGenerations();
    }

    @Override
    public int getGenerationInterval() {
        return evolutionConfig.getGenerationsInterval();
    }

    @Override
    public Solution<Lesson> getRandomSolution() {
        return timeTableMembers.generateRandomSolution();
    }

    @Override
    public int getHardRulesWeight() {
        return timeTableMembers.getHardRulesWeight();
    }

    @Override
    public List<IRule> getRules() {
        return new ArrayList<>(timeTableMembers.getRules());
    }

    @Override
    public ICrossoverData getCrossoverData()
    {
        return evolutionConfig.getCrossover();
    }

    @Override
    public double getFitness(Solution<Lesson> solution, IRule rule) {
        double fails = 0;
        List<Lesson> lessons = solution.getList();
        RuleId ruleId = RuleId.valueOf(rule.getName());
        switch (ruleId) {
            case TeacherIsHuman:
                HashMap<HourInDay, List<Integer>> teachersByHour = new HashMap<>();
                for (Lesson l : lessons) {
                    if (l.getTeacherId() != -1) {
                        HourInDay hourInDay = new HourInDay(l.getDay(), l.getHour());
                        if (teachersByHour.containsKey(hourInDay)) {
                            if (teachersByHour.get(hourInDay).contains(l.getTeacherId())) {
                                fails++;
                                continue;
                            }
                        } else {
                            teachersByHour.put(hourInDay, new ArrayList<>());
                        }
                        teachersByHour.get(hourInDay).add(l.getTeacherId());
                    }
                }
                return (1 - (fails / lessons.size())) * 100;
            case Singularity:
                HashMap<HourInDay, List<Integer>> classesByHour = new HashMap<>();
                for (Lesson l : lessons) {
                    HourInDay hourInDay = new HourInDay(l.getDay(), l.getHour());
                    if (classesByHour.containsKey(hourInDay)) {
                        if (classesByHour.get(hourInDay).contains(l.getClassId())) {
                            fails++;
                            continue;
                        }
                    } else {
                        classesByHour.put(hourInDay, new ArrayList<>());
                    }
                    classesByHour.get(hourInDay).add(l.getClassId());
                }
                return (1 - (fails / lessons.size())) * 100;
            case Knowledgeable:
                for (Lesson lesson : lessons) {
                    if (lesson.getSubjectId() != -1) {
                        Teacher teacher = timeTableMembers.getTeachers().get(lesson.getTeacherId());
                        if (teacher!=null) {
                            if (!teacher.getSubjectsIdsList().contains(lesson.getSubjectId())) {
                                fails++;
                            }
                        } else {
                            fails++;
                        }
                    }
                }
                return (1 - (fails / lessons.size())) * 100;
            case Satisfactory:

                HashMap<Integer, HashMap<Integer, Integer>> hoursByClass = new HashMap<>();

                //set actual
                for (Lesson l : lessons) {
                    if (l.getSubjectId() == -1)
                        continue;
                    int grade = l.getClassId();
                    HashMap<Integer, Integer> subjectsCount;
                    if (!hoursByClass.containsKey(grade)) {
                        subjectsCount = new HashMap<>();
                        subjectsCount.put(l.getSubjectId(), 1);
                    } else {
                        subjectsCount = hoursByClass.get(grade);
                        int hours = 1;
                        if (subjectsCount.containsKey(l.getSubjectId())) {
                            hours += subjectsCount.get(l.getSubjectId());
                        }
                        subjectsCount.put(l.getSubjectId(), hours);
                    }

                    hoursByClass.put(l.getClassId(), subjectsCount);
                }

                int totalSubjectsExpect = 0;
                for (Grade grade : this.timeTableMembers.getGrades().values()) {
                    int gradeId = grade.getId();
                    if (hoursByClass.containsKey(gradeId)) {
                        //go over all subjects in grade
                        //check if they exist in actual
                        //mark all the subjects that are invalid hours
                        for (Integer subjectId : grade.getRequirements().keySet()) {
                            int expect = grade.getRequirements().get(subjectId);
                            int actual = 0;
                            if (hoursByClass.get(gradeId).containsKey(subjectId)) {
                                actual = hoursByClass.get(gradeId).get(subjectId);
                            }
                            if (expect != actual) {
                                fails++;
                            }
                        }

                        //check on actual if there is subjects that not relevant to the class
                        for(Integer subjectId: hoursByClass.get(gradeId).keySet()) {
                            if (!grade.getRequirements().containsKey(subjectId)) {
                                fails++;
                                totalSubjectsExpect++;
                            }
                        }
                    } else {
                        fails += grade.getRequirements().size();
                    }
                    totalSubjectsExpect += grade.getRequirements().size();
                }
                return (1 - (fails / totalSubjectsExpect)) * 100;
            default:
                break;
        }
        return 0;
    }

    @Override
    public Solution<Lesson> sort(Solution<Lesson> solution, String operator) {
        CrossoverSortType sortType = CrossoverSortType.valueOfLabel(operator);
        Solution<Lesson> sorted = new Solution<Lesson>();
        sorted.setList(new ArrayList<>(solution.getList())); //duplicate the solution
        sorted.getList().sort(new LessonComparator(sortType));

        return sorted;
    }

    @Override
    public ISelectionData getSelectionData() {
        return getEvolutionConfig().getSelection();
    }
}
