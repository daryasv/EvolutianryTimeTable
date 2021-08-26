package gui.components.singlehistogram;

public interface HistogramData {
    String getWord();
    void setWord(String word);

    int getCount();
    void setCount(int count);
    void increase();
}
