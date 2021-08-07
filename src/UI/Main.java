package UI;

public class Main {

    public static final String FILE_NAME = "src/resources/EX1-small.xml";

    public static void main(String[] args) {
        ProgramManager programInstance = new ProgramManager();
        programInstance.manageProgram();

//            //create population test demo
//            TimeTableDataSet timeTable = new TimeTableDataSet(descriptor);
//            Evolutionary evolutionary = evolutionary = new Evolutionary();
//            EvolutionConfig evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
//            List<Solution<Lesson>> population = evolutionary.generatePopulation(evolutionEngineDataSet.getInitialPopulation(), timeTable);
//
//            //demo for the best solution
//            Solution<Lesson> solution = population.get(0);
//
//            boolean a = true;
//        } catch (JAXBException | ValidationException e) {
//            e.printStackTrace();
//        }

    }
}