package ru.yandex.tonychem.interpalsviewbooster.engine.model;

public record Country(String country, String alphaCode) implements Comparable<Country> {
    @Override
    public int compareTo(Country o) {
        return this.country.compareToIgnoreCase(o.country);
    }

    @Override
    public String toString() {
        return country;
    }
}
