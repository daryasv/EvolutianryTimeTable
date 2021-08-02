package engine.models;

public interface IRule<T> {

    boolean isHard();
    int getFitness(Solution <T> solution);

}
