package ru.egupov.risktestsystem.utils;

public enum TypeViewReview {

    FullReview("Полный обзор"),
    BriefReview("Краткий обзор"),
    WithoutReview("Без обзора"),
    StatusOnly("Только статус прохождения");

    private String title;

    TypeViewReview(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
