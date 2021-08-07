
package UI;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.Evolutionary;
import engine.models.Solution;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static final String FILE_NAME = "src/resources/EX1-small.xml";

    public static void main(String[] args) {
        ProgramManager programInstance = new ProgramManager();
        programInstance.manageProgram();
        //test();

    }

    private static void test() {
        try {
            //load xml file into ETT classes
            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            //create population test demo
            TimeTableDataSet timeTable = new TimeTableDataSet(descriptor);
            Evolutionary evolutionary = evolutionary = new Evolutionary();
            EvolutionConfig evolutionEngineDataSet = new EvolutionConfig(descriptor.getETTEvolutionEngine());
            List<Solution<Lesson>> population = evolutionary.generatePopulation(evolutionEngineDataSet.getInitialPopulation(), timeTable);

            //demo for the best solution
            Solution<Lesson> solution = population.get(0);
            HashMap<List<Solution<Lesson>>, Integer> fitnessMap = evolutionary.fitnessEvaluation(population, timeTable.getRules(), 70, timeTable);
            evolutionary.run(timeTable);
            boolean a = true;
        } catch (JAXBException | ValidationException e) {
            e.printStackTrace();
        }
    }
}
