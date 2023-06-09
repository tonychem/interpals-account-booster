package ru.yandex.tonychem.interpalsviewbooster.engine.model;

public record Country(String country, String alphaCode) {
    @Override
    public String toString() {
        return country;
    }
}
