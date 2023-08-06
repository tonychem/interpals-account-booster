package ru.yandex.tonychem.interpalsviewbooster.task;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleUpdateTask extends Task<Void> {
    private final TextArea textArea;
    private volatile ConcurrentLinkedQueue<Object> logQueue;

    public ConsoleUpdateTask(TextArea textArea, ConcurrentLinkedQueue<Object> logQueue) {
        this.textArea = textArea;
        this.logQueue = logQueue;
    }

    @Override
    public Void call() throws Exception {
        Thread.currentThread().setName("Worker-console-updater");

        while (!isCancelled()) {
            Object message = logQueue.poll();
            if (message != null) {

                if (message instanceof String s) {
                    if (s.equals(AppUtils.QUEUE_POISON_PILL)) {
                        break;
                    }

                    if (s.contains(AppUtils.DENIAL_CLIENT_RESPONSE)) {
                        textArea.appendText(AppUtils.DENIAL_CLIENT_RESPONSE + "\n");
                    } else {
                        textArea.appendText(s + "\n");
                    }
                } else if (message instanceof Account account) {
                    textArea.appendText("Successfully visited " + account.username() + "\n");
                }
            }
        }

        return null;
    }
}
