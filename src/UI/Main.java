package UI;

import Engine.Evolutionary;
import Engine.models.Solution;
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

            Evolutionary evolutionary = new Evolutionary(descriptor);
            List<Solution> population = evolutionary.generatePopulation();

            boolean a = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}

