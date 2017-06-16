package com.example.translator.model;

public class TranslateItem {
    private int code;
    private String[] text;
    private String lang;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String[] getText() {
        return this.text;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
