
package UI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import UI.models.TimeTableDataSet;
import UI.models.evolution.EvolutionConfig;
import engine.Evolutionary;
import engine.models.Solution;
import UI.models.Lesson;
import UI.models.timeTable.TimeTableMembers;
import schema.models.ETTDescriptor;

public class Main {
    public static void main(String[] args) {
        ProgramManager programInstance = new ProgramManager();
        programInstance.manageProgram();
    }
    }

