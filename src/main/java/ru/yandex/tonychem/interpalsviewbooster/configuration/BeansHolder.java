package ru.yandex.tonychem.interpalsviewbooster.configuration;

import ru.yandex.tonychem.interpalsviewbooster.engine.BasicCrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;

public class BeansHolder {
    private static final CrawlEngine engine = new BasicCrawlEngine();

    public static CrawlEngine engine() {
        return engine;
    }
}
