package UI.models.timeTable;

import UI.ValidationException;
import UI.models.Lesson;
import engine.models.IRule;
import engine.models.Solution;
import schema.models.ETTRule;

public class Rule implements IRule<Lesson> {

    protected String id;
    protected String configuration;
    protected RuleType type;


    //TODO: with set (set methods will do the validation, Example in "Teacher" class)
    public Rule(ETTRule ettRule) throws ValidationException {
      setRuleType(ettRule.getType());
    }

    @Override
    public boolean isHard() {
        return type == RuleType.HARD;
    }

    //TODO
    @Override
    public int getFitness(Solution<Lesson> solution) {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }

    public void setRuleType(String ruleType) throws ValidationException {
        RuleType type = RuleType.valueOfLabel(ruleType);
        if(type == null){
            throw new ValidationException("Invalid rule type");
        }
        this.type = type;
    }
}

