package ru.yandex.tonychem.interpalsviewbooster.engine.model;

public enum Sex {
    BOTH(""), MALE("male"), FEMALE("female");

    private String denotation;

    Sex(String denotation) {
        this.denotation = denotation;
    }

    public String getDenotation() {
        return denotation;
    }
}
