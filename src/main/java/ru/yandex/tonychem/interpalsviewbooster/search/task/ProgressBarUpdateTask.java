package ru.yandex.tonychem.interpalsviewbooster.search.task;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import java.util.concurrent.atomic.AtomicReference;

public class ProgressBarUpdateTask extends Task<Void> {
    private final ProgressBar bar;
    private final AtomicReference<Double> progress;

    public ProgressBarUpdateTask(ProgressBar bar, AtomicReference<Double> progress) {
        this.bar = bar;
        this.progress = progress;
    }

    @Override
    protected Void call() throws Exception {
        Thread.currentThread().setName("Worker-progress-bar-updater");

        double epsilon = 1.0E-6;

        while (Math.abs(progress.get() - 1.0d) > epsilon) {
            bar.setProgress(progress.get());
        }

        return null;
    }
}
