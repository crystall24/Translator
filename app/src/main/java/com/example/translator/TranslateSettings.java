package com.example.translator;

public class TranslateSettings {
    private String textToTranslate;
    private String langFrom;
    private String langTo;

    public TranslateSettings(String textToTranslate, String langFrom, String langTo) {
        this.textToTranslate = textToTranslate;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public String getDirs() {
        return langFrom + "-" + langTo;
    }
}

