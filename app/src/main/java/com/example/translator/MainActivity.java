package com.example.translator;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translator.api.TranslateApiService;
import com.example.translator.mapper.SupportedLanguagesResponseMapper;
import com.example.translator.model.Language;
import com.example.translator.model.SupportedLanguagesResponse;
import com.example.translator.model.TranslateItem;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private final static String EMPTY_STRING = "";

    Retrofit retrofit;
    TranslateApiService service;

    @BindView(R.id.translated_text) TextView translateView;
    @BindView(R.id.input_text) EditText translateInput;
    @BindView(R.id.fromLanguage) Spinner fromLanguageSpinner;
    @BindView(R.id.toLanguage) Spinner toLanguageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(TranslateApiService.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        LoadLanguages loadLanguages = new LoadLanguages();
        loadLanguages.execute("ru");
    }

    @OnClick(R.id.button_copy)
    protected void copyToClipboard() {
        String textToCopy = translateView.getText().toString();
        Toast.makeText(MainActivity.this, textToCopy+ " скопированно в буфер", Toast.LENGTH_SHORT)
                .show();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Main", textToCopy);
        clipboard.setPrimaryClip(clip);
    }

    @OnClick(R.id.button_translate)
    protected void translateText() {
        String textToTranslate = translateInput.getText().toString();
        System.out.println("test inputted: " + textToTranslate);

        String fromLanguage = getSelectedLanguageCode(fromLanguageSpinner);
        String toLanguage = getSelectedLanguageCode(toLanguageSpinner);

        System.out.println("from lang: " + fromLanguage);
        System.out.println("to lang: " + toLanguage);

        TranslateSettings translateSettings =
                new TranslateSettings(textToTranslate, fromLanguage, toLanguage);

        LoadTranslation transLationLoader = new LoadTranslation(); //
        transLationLoader.execute(translateSettings);
    }

    @OnClick(R.id.changeDirection)
    protected void swipeLanguages() {
        int idFromLanguage = fromLanguageSpinner.getSelectedItemPosition();
        int idToLanguage = toLanguageSpinner.getSelectedItemPosition();

        fromLanguageSpinner.setSelection(idToLanguage, true);
        toLanguageSpinner.setSelection(idFromLanguage, true);
    }

    private class LoadTranslation extends AsyncTask<TranslateSettings, Void, TranslateItem> {
        ProgressDialog pDialog;

        @Override
        protected TranslateItem doInBackground(TranslateSettings... params) {
            Call<TranslateItem> translation = service.getTranslation(params[0].getTextToTranslate(), params[0].getDirs());

            TranslateItem item = new TranslateItem();
            try {
                item = translation.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return item;
        }

        @Override protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Загружаем перевод");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(TranslateItem tr){
            pDialog.hide();

            if(tr != null) {
                translateView.setText(tr.getText()[0]);
            } else {
                translateView.setText("Нечего переводить");
            }
        }
    }

    private class LoadLanguages extends AsyncTask<String, Void, List<Language>> {
        @Override
        protected  List<Language> doInBackground(String... params) {
            Call<SupportedLanguagesResponse> langs = service.getLanguages(params[0]);
            SupportedLanguagesResponseMapper mapper = new SupportedLanguagesResponseMapper();
            List<Language> langsList = null;

            SupportedLanguagesResponse langsResponse;
            try {
                langsResponse = langs.execute().body();
                langsList = mapper.apply(langsResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return langsList;
        }

        @Override
        protected void onPostExecute(List<Language> langList){
            LanguageAdapter languageAdapter = new LanguageAdapter(MainActivity.this, R.layout.simple_spinner_item, langList);
            languageAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

            fromLanguageSpinner.setAdapter(languageAdapter);
            toLanguageSpinner.setAdapter(languageAdapter);

            int russianLangIndex = 0; // default if wouldn't be found
            int engLangIndex = 0;
            // find Russian lang in the lang list
            for (Language candidate: langList) {
                if (candidate.getTitle().equals("Русский"))
                    russianLangIndex = langList.indexOf(candidate);
                if(candidate.getTitle().equals("Английский"))
                    engLangIndex = langList.indexOf(candidate);
            }

            fromLanguageSpinner.setSelection(engLangIndex, true);
            toLanguageSpinner.setSelection(russianLangIndex);
        }
    }

    private String getSelectedLanguageCode(Spinner spinner) {
        Language selected = ((Language) spinner.getSelectedItem());

        if (selected != null) {
            return selected.getLanguageCode();
        }

        return EMPTY_STRING;
    }

}
