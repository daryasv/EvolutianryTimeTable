package UI.models.timeTable;

import UI.ValidationException;
import schema.models.ETTClass;

import java.util.Map;

public class Grade extends Component {

    //TODO : getter & setter to this Map
    private Map<String, Integer> RequiredStudyHoursForGrade;

    //TODO: with set (set methods will do the validation, Example in "Teacher" class)
    public Grade(ETTClass ettClass) throws ValidationException {
        setName(ettClass.getETTName());
        setId(ettClass.getId());
    }


}
