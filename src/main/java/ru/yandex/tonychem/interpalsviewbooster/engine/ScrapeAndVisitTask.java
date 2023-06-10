package ru.yandex.tonychem.interpalsviewbooster.engine;

import javafx.concurrent.Task;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ScrapeAndVisitTask extends Task<Void> {

    private final CrawlEngine engine;
    private final UserSearchQuery userSearchQuery;
    private final AtomicReference<Double> visitedAccountsProgress;

    public ScrapeAndVisitTask(CrawlEngine engine, UserSearchQuery userSearchQuery,
                              AtomicReference<Double> visitedAccountsProgress) {
        this.engine = engine;
        this.userSearchQuery = userSearchQuery;
        this.visitedAccountsProgress = visitedAccountsProgress;
    }

    @Override
    protected Void call() throws Exception {
        Set<Account> accounts = engine.gatherAccounts(userSearchQuery, null);
        engine.crawl(accounts, userSearchQuery, visitedAccountsProgress);
        return null;
    }
}
