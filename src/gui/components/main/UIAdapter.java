package gui.components.main;

import gui.components.singlehistogram.HistogramData;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<HistogramData> introduceNewWord;
    private Consumer<HistogramData> updateExistingWord;
    private Runnable updateDistinct;
    private Consumer<Integer> updateTotalProcessedWords;

    public UIAdapter(Consumer<HistogramData> introduceNewWord, Consumer<HistogramData> updateExistingWord, Runnable updateDistinct, Consumer<Integer> updateTotalProcessedWords) {
        this.introduceNewWord = introduceNewWord;
        this.updateExistingWord = updateExistingWord;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedWords = updateTotalProcessedWords;
    }

    public void addNewWord(HistogramData histogramData) {
        Platform.runLater(
                () -> {
                    introduceNewWord.accept(histogramData);
                    updateDistinct.run();
                }
        );
    }

    public void updateExistingWord(HistogramData histogramData) {
        Platform.runLater(
                () -> updateExistingWord.accept(histogramData)
        );
    }

    public void updateTotalProcessedWords(int delta) {
        Platform.runLater(
                () -> updateTotalProcessedWords.accept(delta)
        );
    }

}
