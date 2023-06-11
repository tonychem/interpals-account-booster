package ru.yandex.tonychem.interpalsviewbooster.engine;

import org.apache.commons.lang3.SerializationUtils;
import ru.yandex.tonychem.interpalsviewbooster.beans.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CacheManager {
    private final String login;
    private final Map<String, Set<Account>> cache;

    public CacheManager(String login) {
        this.login = login;
        this.cache = BeansHolder.cache();
    }

    public void markSeen(Account user) {
        cache.computeIfAbsent(login, k -> new HashSet<>());
        cache.get(login).add(user);
    }

    public Set<Account> getSeen() {
        return cache.get(login);
    }

    public void deleteCache() {
        cache.remove(login);
    }

    public void flush() {
        try (FileOutputStream fos = new FileOutputStream(AppUtils.CACHE_FILE_PATH.toFile())) {
            SerializationUtils.serialize((Serializable) cache, fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
