package myapp.util;

import java.time.Duration;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

/**
 * @author Dieter Holz
 */
public class EventSilencer {
    private final Duration delay;

    private Timer timer;

    public EventSilencer(Duration delay) {
        this.delay = delay;
    }

    public synchronized void batch(Runnable toDo) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(toDo);
                timer.cancel();
                timer = null;
            }
        };

        timer.schedule(task, delay.toMillis());
    }
}

