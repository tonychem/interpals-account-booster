package ru.yandex.tonychem.interpalsviewbooster.engine;

import ru.yandex.tonychem.interpalsviewbooster.engine.exception.IncorrectCredentialsException;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public interface CrawlEngine {
    String cookies();

    void authorize(String username, String password) throws IncorrectCredentialsException, IOException;

    Set<Account> gatherAccounts(UserSearchQuery userSearchQuery, AtomicReference<Double> progressCallBack);

    void crawl(Collection<Account> accounts, UserSearchQuery userSearchQuery, CacheManager cacheManager,
               AtomicReference<Double> progressCallBack, ConcurrentLinkedQueue<Object> loggingQueue);
}
