package ru.yandex.tonychem.interpalsviewbooster.task;

import javafx.concurrent.Task;
import ru.yandex.tonychem.interpalsviewbooster.engine.CacheManager;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class ScrapeAndVisitTask extends Task<Void> {

    private final CrawlEngine engine;
    private final UserSearchQuery userSearchQuery;
    private final CacheManager cacheManager;

    private volatile LinkedBlockingQueue<Object> loggingQueue;

    public ScrapeAndVisitTask(CrawlEngine engine, UserSearchQuery userSearchQuery,
                              CacheManager cacheManager,
                              LinkedBlockingQueue<Object> loggingQueue) {
        this.engine = engine;
        this.userSearchQuery = userSearchQuery;
        this.cacheManager = cacheManager;
        this.loggingQueue = loggingQueue;
    }

    @Override
    protected Void call() throws Exception {
        Thread.currentThread().setName("Worker-scraper-and-visitor");

        Consumer<Double> updateProgress = (progress) -> {
            updateProgress(progress, 1.0);
        };

        Set<Account> accounts = engine.gatherAccounts(userSearchQuery);
        engine.crawl(accounts, userSearchQuery, cacheManager, updateProgress, loggingQueue);
        return null;
    }
}
