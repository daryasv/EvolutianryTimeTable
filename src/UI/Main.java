package UI;

import Engine.Evolutionary;
import Engine.models.Solution;
import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;


public class Main {
    public static final String FILE_NAME = "src/resources/EX1-small.xml";

    public static void main(String[] args) {
        //ProgramManager.manageProgram();

        try {
            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            TimeTableDataSet timeTable = new TimeTableDataSet(descriptor.getETTTimeTable());
            Evolutionary evolutionary = new Evolutionary();

            List<Solution<Lesson>> population = evolutionary.generatePopulation(descriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize(),timeTable);

            boolean a = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}

