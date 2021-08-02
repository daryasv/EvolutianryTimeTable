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
    public static void main(String[] args) {
        ProgramManager programInstance = new ProgramManager();
        programInstance.manageProgram();
    }
}

