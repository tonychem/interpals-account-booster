package ru.yandex.tonychem.interpalsviewbooster.engine.model;

public record UserSearchQuery(int ageStart, int ageEnd, Sex sex, Country country, boolean onlineOnly,
                              boolean visitPreviouslyViewedAccounts, int requestDelay) {
}
