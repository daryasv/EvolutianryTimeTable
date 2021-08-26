package gui.components.singlehistogram;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BasicHistogramData implements HistogramData {

    protected SimpleStringProperty word;
    protected SimpleIntegerProperty count;

    public BasicHistogramData(String word, int count) {
        this.word = new SimpleStringProperty(word);
        this.count = new SimpleIntegerProperty(count);
    }

    @Override
    public String getWord() {
        return word.get();
    }

    @Override
    public void setWord(String word) {
        this.word.set(word);
    }

    @Override
    public int getCount() {
        return count.get();
    }

    @Override
    public void setCount(int count) {
        this.count.set(count);
    }

    @Override
    public void increase() {
        count.set(count.get() + 1);
    }

    @Override
    public String toString() {
        return word + " : " + count;
    }
}
