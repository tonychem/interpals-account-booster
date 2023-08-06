package ru.yandex.tonychem.interpalsviewbooster.engine;

import ru.yandex.tonychem.interpalsviewbooster.engine.exception.IncorrectCredentialsException;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public interface CrawlEngine {
    String cookies();

    void authorize(String username, String password) throws IncorrectCredentialsException, IOException;

    Set<Account> gatherAccounts(UserSearchQuery userSearchQuery);

    void crawl(Collection<Account> accounts, UserSearchQuery userSearchQuery, CacheManager cacheManager,
               Consumer<Double> progressUpdater, LinkedBlockingQueue<Object> loggingQueue) throws InterruptedException;
}
