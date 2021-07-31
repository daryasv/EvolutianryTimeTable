package UI;

import UI.evolutionEngine.models.EvolutionEngineDataSet;
import engine.Evolutionary;
import engine.models.Solution;
import UI.timeTable.models.Lesson;
import UI.timeTable.models.TimeTableDataSet;
import UI.timeTable.models.ValidationException;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static final String FILE_NAME = "src/resources/EX1-small.xml";

    public static void main(String[] args) {
        //ProgramManager.manageProgram();

        try {
            //load xml file into ETT classes
            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            //create population test demo
            TimeTableDataSet timeTable = new TimeTableDataSet(descriptor.getETTTimeTable());
            Evolutionary evolutionary = evolutionary = new Evolutionary();
            EvolutionEngineDataSet evolutionEngineDataSet = new EvolutionEngineDataSet(descriptor.getETTEvolutionEngine());
            List<Solution<Lesson>> population = evolutionary.generatePopulation(evolutionEngineDataSet.getInitialPopulation(), timeTable);

            //demo for the best solution
            Solution<Lesson> solution = population.get(0);
            List<Lesson> lessons = solution.getList().stream().filter(l->l.getTeacherId() == 1).collect(Collectors.toList());
            boolean a = true;
        } catch (JAXBException | ValidationException e ) {
            e.printStackTrace();
        }
    }
}

