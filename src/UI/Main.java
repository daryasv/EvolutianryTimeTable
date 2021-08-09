
package UI;

import UI.models.Lesson;
import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.Evolutionary;
import engine.models.Solution;
import engine.models.SolutionFitness;
import schema.models.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static final String FILE_NAME = "src/resources/EX1-small.xml";

    public static void main(String[] args) {
        ProgramManager programInstance = new ProgramManager();
        programInstance.manageProgram();
//        test();

    }

    private static void test() {
        try {
            //load xml file into ETT classes
            File file = new File(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor descriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            //create population test demo
            try {
                Evolutionary evolutionary = new Evolutionary();

                TimeTableDataSet timeTable = new TimeTableDataSet(descriptor, 100, 10);
                evolutionary.run(timeTable);

                FileOutputStream fileOut = new FileOutputStream("test.tt");
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(timeTable);
                objectOut.writeObject(evolutionary.getGlobalBestSolution());
                objectOut.writeObject(evolutionary.getBestSolutions());
                objectOut.close();

                FileInputStream fi = new FileInputStream(new File("test.tt"));
                ObjectInputStream oi = new ObjectInputStream(fi);
                // Read objects
                TimeTableDataSet loadTimeTable = (TimeTableDataSet) oi.readObject();
                //List<SolutionFitness<Lesson>> bestSolutions = (List<SolutionFitness<Lesson>>) oi.readObject();
                SolutionFitness<Lesson> solution = (SolutionFitness<Lesson>) oi.readObject();

                oi.close();
                fi.close();

                int a = 1;
            }catch (Exception e){
                throw new ValidationException("failed to write file");
            }

//            Evolutionary evolutionary = new Evolutionary();
//            List<Solution<Lesson>> population = evolutionary.generatePopulation(timeTable.getEvolutionConfig().getInitialPopulation(), timeTable);
//
//            //demo for the best solution
//            Solution<Lesson> solution = population.get(0);
//          //  HashMap<List<Solution<Lesson>>, Integer> fitnessMap = evolutionary.fitnessEvaluation(population, timeTable.getRules(), 70, timeTable);
//            evolutionary.run(timeTable);
            boolean a = true;
        } catch (JAXBException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
