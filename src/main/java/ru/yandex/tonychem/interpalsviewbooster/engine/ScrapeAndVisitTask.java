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

    private volatile ConcurrentLinkedQueue<Object> loggingQueue;

    public ScrapeAndVisitTask(CrawlEngine engine, UserSearchQuery userSearchQuery,
                              AtomicReference<Double> visitedAccountsProgress,
                              ConcurrentLinkedQueue<Object> loggingQueue) {
        this.engine = engine;
        this.userSearchQuery = userSearchQuery;
        this.visitedAccountsProgress = visitedAccountsProgress;
        this.loggingQueue = loggingQueue;
    }

    @Override
    protected Void call() throws Exception {
        Set<Account> accounts = engine.gatherAccounts(userSearchQuery, null);
        engine.crawl(accounts, userSearchQuery, visitedAccountsProgress, loggingQueue);
        return null;
    }
}
