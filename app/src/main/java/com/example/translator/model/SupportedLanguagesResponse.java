package com.example.translator.model;

import java.util.HashMap;
import java.util.Map;

public class SupportedLanguagesResponse {
    private String[] dirs;
    Map<String, String> langs = new HashMap<>();

    public String[] getDirs() {
        return this.dirs;
    }

    public void setDirs(String[] dirs) {
        this.dirs = dirs;
    }

    public Map<String, String> getLangs() {
        return this.langs;
    }

    public void setLangs(Map<String, String> langs) {
        this.langs = langs;
    }
}

