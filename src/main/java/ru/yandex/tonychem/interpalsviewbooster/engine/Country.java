package ru.yandex.tonychem.interpalsviewbooster.engine;

public class Country {
    private final String country;
    private final String alphaCode;

    public Country(String country, String alphaCode) {
        this.country = country;
        this.alphaCode = alphaCode;
    }

    public String getCountry() {
        return country;
    }

    public String getAlphaCode() {
        return alphaCode;
    }

    @Override
    public String toString() {
        return country;
    }
}
