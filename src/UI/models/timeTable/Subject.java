package UI.models.timeTable;

import UI.ValidationException;
import schema.models.ETTSubject;

import java.util.List;

public class Subject extends Component {

    public Subject(ETTSubject ettSubject) throws ValidationException {
        super();
        setId(ettSubject.getId());
        setName(ettSubject.getName());
    }
}
