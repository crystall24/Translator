package com.example.translator;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.example.translator.api.TranslateApiService;
import com.example.translator.model.TranslateItem;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input_text) EditText translateInput;

    @BindView(R.id.translated_text) TextView translatedText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
    }

    @OnClick(R.id.button_translate)
     protected void translateText() {
        String textToTranslate = translateInput.getText().toString();
        System.out.println("test inputted: " + textToTranslate);
        LoadTranslation trans = new LoadTranslation();
        trans.execute(textToTranslate);
    }

    private class LoadTranslation extends AsyncTask<String, Void, TranslateItem > {
        ProgressDialog pDialog;

        @Override
        protected TranslateItem doInBackground(String... text) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://translate.yandex.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TranslateApiService service = retrofit.create(TranslateApiService.class);

            Call<TranslateItem> translation = service.getTranslation(text[0]);
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
            pDialog.setMessage("Loading");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(TranslateItem tr) {
            translatedText.setText(tr.getText()[0]);
            pDialog.hide();
            Log.d("MAINACTIVITY",tr.getText()[0]);
        }
    }

}
