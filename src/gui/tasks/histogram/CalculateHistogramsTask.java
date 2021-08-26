package gui.tasks.histogram;

import gui.common.HistogramsUtils;
import gui.components.singlehistogram.BasicHistogramData;
import gui.components.singlehistogram.HistogramData;
import gui.components.main.UIAdapter;
import gui.exception.TaskIsCanceledException;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/*
concerns:
 1. I had to give some sleep time for the task so it will give a chance for the UI thread to run - the task attempted to over utilize the CPU
    one option is to use Thread.Yield() - that will cause this thread to release CPU on it's own, but since it's only an hint to the scheduler it didn't really helped
    another option to avoid that was to reduce the times I am attempting to update the UI: instead of doing that
    per word, perhaps do it per line
 2. why not using an observable map in which the task thread will update its data (the histograms) and on which
    the UI will observe and build / update the tiles from ?
    since every update / add done to this observable map that is shared and binded to UI components that was made using the
    task thread (this thread) caused illegal state exception since it wasn't performed on the EDT thread.
    moving all the updates to the EDT made no sense since that is the most of the work of this task so why using it at all in separate thread
    if the most of it will happen on the EDT ??
    to solve and avoid the problem, I had to use a different data structure to which the UI is binded and a different one that is for internal use of this task
 3. all ui adapter actions runs within platform invoke later style since they attempt to change UI stuff
 */
public class CalculateHistogramsTask extends Task<Boolean> {

    private final long SLEEP_TIME = 5;

    private String fileName;
    private long totalWords;
    private Map<String, HistogramData> histograms;
    private UIAdapter uiAdapter;
    private Consumer<Runnable> onCancel;

    public CalculateHistogramsTask(String fileName, long totalWords, UIAdapter uiAdapter, Consumer<Runnable> onCancel) {
        this.fileName = fileName;
        this.uiAdapter = uiAdapter;
        this.totalWords = totalWords;
        this.onCancel = onCancel;

        this.histograms = new HashMap<>();
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Fetching file...");
        updateProgress(0, totalWords);
        BufferedReader reader = Files.newBufferedReader(
                Paths.get(fileName),
                StandardCharsets.UTF_8);

        updateMessage("Traversing words...");
        final int[] wordCount = {1};
        try {
            reader
                    .lines()
                    .forEach(aLine -> {
                        if (isCancelled()) {
                            throw new TaskIsCanceledException();
                        }
                        String[] words = aLine.split(" ");
                        Arrays.stream(words)
                                .forEach(word -> {
                                    updateProgress(wordCount[0]++, totalWords);
                                    HistogramData histogramData = histograms.get(word);
                                    if (histogramData == null) {
                                        histogramData = new BasicHistogramData(word, 1);
                                        histograms.put(word, histogramData);
                                        uiAdapter.addNewWord(histogramData);
                                    } else {
                                        histogramData.increase();
                                        uiAdapter.updateExistingWord(histogramData);
                                    }

                                    //Thread.yield(); // doesn't work so good...

                                    HistogramsUtils.sleepForAWhile(SLEEP_TIME);
                                });
                        uiAdapter.updateTotalProcessedWords(words.length);
                    });

        } catch (TaskIsCanceledException e) {
            HistogramsUtils.log("Task was canceled !");
            onCancel.accept(null);
        }
        updateMessage("Done...");
        return Boolean.TRUE;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
    }
}
