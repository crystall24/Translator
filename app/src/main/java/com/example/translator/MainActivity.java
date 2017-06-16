package com.example.translator;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window.Callback;

import com.example.translator.api.TranslateApiService;
import com.example.translator.model.TranslateItem;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadTranslation trans = new LoadTranslation();

        trans.execute();





        setContentView(R.layout.activity_main);

    }

    private class LoadTranslation extends AsyncTask<Void, Void, TranslateItem > {
        @Override
        protected TranslateItem doInBackground(Void... params) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://translate.yandex.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TranslateApiService service = retrofit.create(TranslateApiService.class);

            Call<TranslateItem> translation = service.getTranslation();

//            String item[] = null;
            TranslateItem item = new TranslateItem();
            try {
                item = translation.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return item;
        }
        /**@Override protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("");
        pDialog.show();

        }**/

        @Override
        protected void onPostExecute(TranslateItem tr){
            Log.d("MAINACTIVITY",tr.getText()[0]);
        }
    }

}
