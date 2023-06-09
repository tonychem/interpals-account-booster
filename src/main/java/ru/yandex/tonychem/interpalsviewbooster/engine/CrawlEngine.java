package ru.yandex.tonychem.interpalsviewbooster.engine;

import ru.yandex.tonychem.interpalsviewbooster.engine.exceptions.IncorrectCredentialsException;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public interface CrawlEngine {
    String cookies();

    void authorize(String username, String password) throws IncorrectCredentialsException, IOException;

    Set<Account> gatherAccounts(UserSearchQuery userSearchQuery);

    void crawl(Collection<Account> accounts, UserSearchQuery userSearchQuery);
}
