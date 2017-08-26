package com.example.translator.mapper;

import android.support.annotation.NonNull;

import com.example.translator.model.Language;
import com.example.translator.model.SupportedLanguagesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SupportedLanguagesResponseMapper {
    public List<Language> apply(@NonNull SupportedLanguagesResponse response) throws Exception {
        List<Language> result = new ArrayList<>();

        for (Map.Entry<String, String> mapEntry : response.getLangs().entrySet()) {
            result.add(new Language(mapEntry.getValue(), mapEntry.getKey()));
        }

        return result;
    }
}
