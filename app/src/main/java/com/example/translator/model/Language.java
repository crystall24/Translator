package com.example.translator.model;

public class Language {
    public final String title;
    public final String languageCode;

    public Language(String title, String language) {
        this.title = title;
        this.languageCode = language;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    @Override
    public String toString() {
        return title;
    }
}
