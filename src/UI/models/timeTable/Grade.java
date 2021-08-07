package UI.models.timeTable;

import UI.ValidationException;
import schema.models.ETTClass;
import schema.models.ETTRequirements;
import schema.models.ETTStudy;

import java.util.HashMap;
import java.util.Map;

public class Grade extends Component {

    private int id;
    private String name;
    private HashMap<Integer, Integer> requirements;


    public Grade(ETTClass ettClass) throws ValidationException {
        setId(ettClass.getId());
        setName(ettClass.getETTName());
        setRequirements(ettClass.getETTRequirements());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<Integer, Integer> requirements) {
        this.requirements = requirements;
    }

    public void setRequirements(ETTRequirements ettRequirements) {
        requirements = new HashMap<>();
        for (ETTStudy study : ettRequirements.getETTStudy()) {
            requirements.put(study.getSubjectId(), study.getHours());
        }
    }
}
