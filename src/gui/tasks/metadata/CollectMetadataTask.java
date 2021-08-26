package gui.tasks.metadata;

import gui.common.HistogramsUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class CollectMetadataTask extends Task<Boolean> {

    private String fileName;
    private Consumer<Long> totalWordsDelegate;
    private Consumer<Long> totalLinesDelegate;

    private final int SLEEP_TIME = 0;

    public CollectMetadataTask(String fileName, Consumer<Long> totalWordsDelegate, Consumer<Long> totalLinesDelegate) {
        this.fileName = fileName;
        this.totalWordsDelegate = totalWordsDelegate;
        this.totalLinesDelegate = totalLinesDelegate;
    }

    @Override
    protected Boolean call() throws Exception {
        final long totalLines;
        final long totalWords;

        try {

            updateMessage("Fetching file...");
            BufferedReader reader = Files.newBufferedReader(
                    Paths.get(fileName),
                    StandardCharsets.UTF_8);


            updateMessage("Counting lines...");
            updateProgress(0,1);
            totalLines = reader
                    .lines()
                    .count();

            updateProgress(1,1);
            // update total lines in UI
            Platform.runLater(
                    () -> totalLinesDelegate.accept(totalLines)
            );

            HistogramsUtils.sleepForAWhile(SLEEP_TIME);

            // we need to reopen the stream since it reached it's end on the above pass
            reader = Files.newBufferedReader(
                    Paths.get(fileName),
                    StandardCharsets.UTF_8);

            updateMessage("Counting words...");
            final int[] currentLineNumber = {0};
            totalWords = reader
                    .lines()
                    .mapToInt(line -> {
                        // ui updates
                        currentLineNumber[0]++;
                        updateProgress(currentLineNumber[0], totalLines);
                        HistogramsUtils.sleepForAWhile(SLEEP_TIME);

                        // actual work...
                        return line.split(" ").length;
                    })
                    .sum();

            // update in UI
            Platform.runLater(
                    () -> totalWordsDelegate.accept(totalWords)
            );

            updateMessage("Done...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;
    }

}