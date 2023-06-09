package ru.yandex.tonychem.interpalsviewbooster.util;

import java.net.URI;

public enum ResourceLocators {
    MAIN("https://www.interpals.net/"), AUTH("https://www.interpals.net/app/auth/login"),
    SEARCH("https://www.interpals.net/app/search?");

    private final String url;

    ResourceLocators(String url) {
        this.url = url;
    }

    public URI uri() {
        return URI.create(url);
    }

    public String url() {
        return url;
    }
}
