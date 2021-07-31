package UI.timeTable.models;

import schema.models.ETTTeacher;

public class Teacher extends Component{

    public Teacher(ETTTeacher ettTeacher) throws ValidationException {
        setId(ettTeacher.getId());
        setName(ettTeacher.getETTName());
    }
}
