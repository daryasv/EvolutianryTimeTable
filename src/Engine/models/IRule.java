package Engine.models;

public interface IRule<T> {

    public boolean isHard();
    public int getFitness(Solution <T> solution);
}
