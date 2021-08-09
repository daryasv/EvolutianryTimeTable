package UI.models.evolution;

import UI.Utils;
import UI.ValidationException;
import UI.models.Lesson;
import engine.models.IMutation;
import engine.models.SelectionType;
import engine.models.Solution;
import schema.models.ETTMutation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mutation implements IMutation<Lesson> , Serializable
{
    private final List<Character> ALLOWED_COMPONENTS = new ArrayList<>(Arrays.asList('C','S','T','H','D'));

    private String name;
    private double probability;
    private int maxTupples;
    private char component;

    public static enum MutationOperators{
        FLIP_OPERATOR("Flipping"),
        SIZE_OPERATOR("Sizer");
        String operator;

        public String getOperatorName() {
            return operator;
        }

        MutationOperators(String operator){
            this.operator=operator;
        }

        public static MutationOperators valueOfLabel(String label) {
            for (MutationOperators e : values()) {
                if (e.operator.equals(label)) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    public Mutation(ETTMutation ettMutation) throws ValidationException {
        setName(ettMutation);
        setProbability(ettMutation);
        setMaxTupples(ettMutation);
        setComponent(ettMutation);
    }

    public String getName() {
        return name;
    }

    //TODO : validation
    public void setName(ETTMutation ettMutation) throws ValidationException {
        if(ettMutation.getName()==null || ettMutation.getName().isEmpty()){
            throw new ValidationException("Mutation name can't be empty");
        }
        this.name = ettMutation.getName();
    }

    @Override
    public double getProbability() {
        return probability;
    }


    //TODO : validation
    public void setProbability(ETTMutation ettMutation) throws ValidationException {
        if(ettMutation.getProbability() > 0) {
            this.probability = ettMutation.getProbability();
        }else{
            throw new ValidationException("Probability cant be under 0");
        }
    }

    public int getMaxTupples() {
        return maxTupples;
    }

    public void setMaxTupples(ETTMutation ettMutation) throws ValidationException {
        if(ettMutation.getConfiguration().isEmpty()){
            throw new ValidationException("Mutation configuration cannot be null");
        }
        if(ettMutation.getConfiguration().split(",")[0].split("=").length != 2){
            throw new ValidationException("Invalid Tupple mutation configuration");
        }

        String tuppleType = ettMutation.getConfiguration().split(",")[0].split("=")[0];
        if(!tuppleType.equals("TotalTupples") && !tuppleType.equals("MaxTupples")){
            throw new ValidationException("Tupple type - " + tuppleType + " - does not exists");
        }
        Integer tupple = Utils.tryParse(ettMutation.getConfiguration().split(",")[0].split("=")[1]);
        if(tupple ==null) {
            throw new ValidationException("Invalid tupple type " + ettMutation.getConfiguration().split(",")[0].split("=")[1]);
        }
        this.maxTupples = tupple;
    }

    public char getComponent() {
        return component;
    }

    public void setComponent(ETTMutation ettMutation) throws ValidationException {
        String[] configs = ettMutation.getConfiguration().split(",");
        if(configs.length > 1){
            this.component = ettMutation.getConfiguration().charAt(ettMutation.getConfiguration().length()-1);
            if(!ALLOWED_COMPONENTS.contains(component)) {
                throw new ValidationException("Invalid mutation component - " + this.component);
            }
        }
    }

}
