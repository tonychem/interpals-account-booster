package ru.yandex.tonychem.interpalsviewbooster.search;

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
        double epsilon = 1.0E-6;

        while (Math.abs(progress.get() - 1.0d) > epsilon) {
            bar.setProgress(progress.get());
        }

        return null;
    }
}
