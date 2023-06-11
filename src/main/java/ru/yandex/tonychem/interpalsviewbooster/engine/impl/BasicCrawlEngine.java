package ru.yandex.tonychem.interpalsviewbooster.engine.impl;

import ru.yandex.tonychem.interpalsviewbooster.engine.CacheManager;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.ResourceLocators;
import ru.yandex.tonychem.interpalsviewbooster.engine.UserAgentFactory;
import ru.yandex.tonychem.interpalsviewbooster.engine.exception.IncorrectCredentialsException;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.Account;
import ru.yandex.tonychem.interpalsviewbooster.engine.model.UserSearchQuery;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicCrawlEngine implements CrawlEngine {

    private final HttpClient client;
    private final UserAgentFactory userAgentFactory;
    private final String badCredentialsFlag = AppUtils.BAD_CREDENTIALS_FLAG;

    private final String denialResponse = AppUtils.DENIAL_SERVER_RESPONSE;

    private final int denialWaitMillis = AppUtils.DENIAL_WAIT;

    private String cookies;

    public BasicCrawlEngine() {
        this.client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        this.userAgentFactory = new UserAgentFactory();
    }

    @Override
    public void authorize(String username, String password) throws IncorrectCredentialsException, IOException {
        String csrfToken;
        String authorizationRequestBody;

        HttpRequest csrfTokenRequest = HttpRequest.newBuilder()
                .uri(ResourceLocators.MAIN.uri())
                .GET()
                .build();


        HttpResponse<String> csrfTokenResponse = null;

        try {
            csrfTokenResponse = client.send(csrfTokenRequest, HttpResponse.BodyHandlers.ofString());

            cookies = prepareCookie(csrfTokenResponse);
            csrfToken = prepareCSRFToken(csrfTokenResponse);

            authorizationRequestBody =
                    String.format("username=%s&password=%s&csrf_token=%s", username, password, csrfToken);

            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(ResourceLocators.AUTH.uri())
                    .header("user-agent", userAgentFactory.getAgent())
                    .header("referer", "https://www.interpals.net")
                    .header("cookie", cookies)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(authorizationRequestBody))
                    .build();

            HttpResponse<String> authorizationRequest = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            if (authorizationRequest.body().contains(badCredentialsFlag)) {
                throw new IncorrectCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Account> gatherAccounts(UserSearchQuery userSearchQuery, AtomicReference<Double> progressCallBack) {
        String baseSearchUrl = prepareBaseSearchUrl(userSearchQuery);
        Set<Account> accounts = new HashSet<>(100);

        Set<Account> accountsFoundOnSearchPage;
        int offset = 0;

        do {
            URI searchUrl = URI.create(baseSearchUrl + "&offset=" + offset);

            HttpRequest searchResultPageRequest =
                    HttpRequest.newBuilder()
                            .uri(searchUrl)
                            .header("user-agent", userAgentFactory.getAgent())
                            .header("referer", "https://www.interpals.net")
                            .header("cookie", cookies)
                            .header("content-type", "application/x-www-form-urlencoded")
                            .build();

            try {
                HttpResponse<String> searchResponse = client.send(searchResultPageRequest,
                        HttpResponse.BodyHandlers.ofString());
                accountsFoundOnSearchPage = extractAccountNames(searchResponse);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            accounts.addAll(accountsFoundOnSearchPage);
            offset += 10;

        } while (accountsFoundOnSearchPage.size() != 0);

        return accounts;
    }

    @Override
    public void crawl(Collection<Account> accounts, UserSearchQuery userSearchQuery, CacheManager cacheManager,
                      AtomicReference<Double> progressCallBack, ConcurrentLinkedQueue<Object> loggingQueue) {
        if (!userSearchQuery.visitPreviouslyViewedAccounts()) {
            Set<Account> previouslyViewedAccounts = cacheManager.getSeen();
            if (previouslyViewedAccounts != null) {
                accounts.removeAll(cacheManager.getSeen());
            }
        }

        int totalSize = accounts.size();

        if (totalSize == 0) {
            loggingQueue.offer("No users satisfy the request");
            loggingQueue.offer(AppUtils.QUEUE_POISON_PILL);
            progressCallBack.set(1.0d);
            return;
        }

        double actuallyVisited = 0.0d;

        for (Account account : accounts) {
            HttpRequest userVisitRequest = HttpRequest.newBuilder()
                    .uri(ResourceLocators.MAIN.uri().resolve(account.username()))
                    .header("user-agent", userAgentFactory.getAgent())
                    .header("referer", "https://www.interpals.net")
                    .header("cookie", cookies)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .build();

            try {
                HttpResponse<String> response = client.send(userVisitRequest, HttpResponse.BodyHandlers.ofString());

                if (requestDenied(response)) {
                    loggingQueue.offer(AppUtils.DENIAL_CLIENT_RESPONSE);
                    Thread.sleep(denialWaitMillis);
                } else {
                    Thread.sleep(userSearchQuery.requestDelay());
                }

                actuallyVisited++;
                progressCallBack.set(actuallyVisited / totalSize);

                cacheManager.markSeen(account);
                loggingQueue.offer(account);

            } catch (IOException | InterruptedException e) {
                cacheManager.flush();
                loggingQueue.offer(AppUtils.QUEUE_POISON_PILL);
                throw new RuntimeException(e);
            }
        }
        cacheManager.flush();
        loggingQueue.offer(AppUtils.QUEUE_POISON_PILL);
    }

    @Override
    public String cookies() {
        return cookies;
    }

    private String prepareCookie(HttpResponse<String> initialResponse) {
        String interpalsSessid = null;
        String __ubic1 = null;
        String csrfCookieV2 = null;

        for (String cookie : initialResponse.headers().allValues("set-cookie")) {
            String[] splittedCookie = cookie.split(";");

            for (String keyValueString : splittedCookie) {
                if (keyValueString.contains("interpals_sessid")) {
                    interpalsSessid = keyValueString.substring("interpals_sessid".length() + 1);
                    break;
                } else if (keyValueString.contains("__ubic1")) {
                    __ubic1 = keyValueString.substring("__ubic1".length() + 1);
                    break;
                } else if (keyValueString.contains("csrf_cookieV2")) {
                    csrfCookieV2 = keyValueString.substring("csrf_cookieV2".length() + 1);
                    break;
                }
            }
        }

        return String.format("interpals_sessid=%s; __ubic1=%s; csrf_cookieV2=%s;",
                interpalsSessid, __ubic1, csrfCookieV2);
    }

    private String prepareCSRFToken(HttpResponse<String> initialResponse) {
        final String csrfPattern = "<meta name=\"csrf_token\" content=\"(.*?)\"";

        Pattern pattern = Pattern.compile(csrfPattern);
        Matcher matcher = pattern.matcher(initialResponse.body());

        String csrfToken = null;

        while (matcher.find()) {
            csrfToken = matcher.group(1);
            break;
        }

        return csrfToken;
    }

    private Set<Account> extractAccountNames(HttpResponse<String> searchResultResponse) {
        final String usernamePattern = "<a class=\"sResThumb\" href=\"/(.+?)\\?";

        Set<Account> matches = new HashSet<>();
        Pattern p = Pattern.compile(usernamePattern);
        Matcher m = p.matcher(searchResultResponse.body());

        while (m.find()) {
            matches.add(new Account(m.group(1)));
        }

        return matches;
    }

    private String prepareBaseSearchUrl(UserSearchQuery userSearchQuery) {
        StringBuilder searchUrl = new StringBuilder(ResourceLocators.SEARCH.url());

        searchUrl.append("&age1=" + userSearchQuery.ageStart());
        searchUrl.append("&age2=" + userSearchQuery.ageEnd());
        searchUrl.append("&countries%5B%5D=" + userSearchQuery.country().alphaCode());
        searchUrl.append("&sex%5B%5D=" + userSearchQuery.sex().getDenotation());

        if (userSearchQuery.onlineOnly()) {
            searchUrl.append("&online=1");
        }

        return searchUrl.toString();
    }

    private boolean requestDenied(HttpResponse<String> response) {
        if (response.body().contains(denialResponse)) {
            return true;
        }
        return false;
    }
}
