package ru.yandex.tonychem.interpalsviewbooster.engine;

import javafx.concurrent.Task;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ScrapeAndVisitTask extends Task<Void> {

    private final CrawlEngine engine;
    private final UserSearchQuery userSearchQuery;
    private final AtomicReference<Double> visitedAccountsProgress;

    private final CacheManager cacheManager;


    private volatile ConcurrentLinkedQueue<Object> loggingQueue;

    public ScrapeAndVisitTask(CrawlEngine engine, UserSearchQuery userSearchQuery,
                              CacheManager cacheManager,
                              AtomicReference<Double> visitedAccountsProgress,
                              ConcurrentLinkedQueue<Object> loggingQueue) {
        this.engine = engine;
        this.userSearchQuery = userSearchQuery;
        this.cacheManager = cacheManager;
        this.visitedAccountsProgress = visitedAccountsProgress;
        this.loggingQueue = loggingQueue;
    }

    @Override
    protected Void call() throws Exception {
        Thread.currentThread().setName("Worker-page-visitor");
        Set<Account> accounts = engine.gatherAccounts(userSearchQuery, null);
        engine.crawl(accounts, userSearchQuery, cacheManager, visitedAccountsProgress, loggingQueue);
        return null;
    }
}
