package com.example.translator.api;

import com.example.translator.model.TranslateItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TranslateApiService {

    static final String KEY = "trnsl.1.1.20170613T063947Z.9b9d3d9cde672ca9.9f08452e8d861abb9a31b2fe6f8c650f316c398c";
    @GET("/api/v1.5/tr.json/translate?text=hello&lang=en-fr&key=" + KEY)
    Call<TranslateItem> getTranslation();

}
