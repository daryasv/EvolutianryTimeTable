package gui.tasks.evolutinary;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.awt.*;

public class EvolutionaryTaskEvent extends Event {

    public static final EventType<EvolutionaryTaskEvent> PAUSE = new EventType<>(ANY, "PAUSE");
    public static final EventType<EvolutionaryTaskEvent> STOP = new EventType<>(ANY, "STOP");
    public static final EventType<EvolutionaryTaskEvent> RESUME = new EventType<>(ANY, "RESUME");

    public EvolutionaryTaskEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
