package UI.models.timeTable;

import UI.ValidationException;
import UI.models.Lesson;
import engine.models.IRule;
import engine.models.Solution;
import schema.models.ETTRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rule implements IRule {

    protected RuleId id;
    protected String configuration;
    protected RuleType type;

    public Rule(ETTRule ettRule) throws ValidationException {
      setId(ettRule.getETTRuleId());
      setConfiguration(ettRule.getETTConfiguration());
      setRuleType(ettRule.getType());
    }

    @Override
    public boolean isHard() {
        return type == RuleType.HARD;
    }

    @Override
    public String getName() {
        return this.id.toString();
    }

    public RuleId getId() {
        return id;
    }

    public void setId(RuleId id) throws ValidationException {
        if(id == null){
            throw new ValidationException("Invalid rule id");
        }
        this.id = id;
    }

    public void setId(String id) throws ValidationException {
        RuleId ruleId = RuleId.valueOfLabel(id);
        setId(ruleId);
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

    public void setType(RuleType type) throws ValidationException {
        if(type == null){
            throw new ValidationException("Invalid rule type");
        }
        this.type = type;
    }

    public void setRuleType(String ruleType) throws ValidationException {
        RuleType type = RuleType.valueOfLabel(ruleType);
        setType(type);
    }
}

