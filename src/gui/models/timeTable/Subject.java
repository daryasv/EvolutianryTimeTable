package gui.models.timeTable;

import gui.exception.ValidationException;
import schema.models.ETTSubject;

public class Subject extends Component {

    public Subject(ETTSubject ettSubject) throws ValidationException {
        super();
        setId(ettSubject.getId());
        setName(ettSubject.getName());
    }
}
