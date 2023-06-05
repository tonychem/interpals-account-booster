package ru.yandex.tonychem.interpalsviewbooster.engine;

import ru.yandex.tonychem.interpalsviewbooster.engine.exceptions.IncorrectCredentialsException;

import java.io.IOException;
import java.util.Set;

public interface CrawlEngine {
    void authorize(String username, String password) throws IncorrectCredentialsException, IOException;
    Set<Account> gatherAccounts(UserSearchQuery userSearchQuery);
}
