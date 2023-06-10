package ru.yandex.tonychem.interpalsviewbooster.configuration;

import ru.yandex.tonychem.interpalsviewbooster.engine.BasicCrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeansHolder {
    private static final CrawlEngine ENGINE = new BasicCrawlEngine();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
    public static CrawlEngine engine() {
        return ENGINE;
    }

    public static ExecutorService executorService() { return EXECUTOR_SERVICE;
    }
}
