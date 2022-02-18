package com.hiddenDimension.thoughtmapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hiddenDimension.thoughtmapper.backend.SentenceSearch;
import com.hiddenDimension.thoughtmapper.backgroud.CoreService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText et= null;
    Button act= null;


    TextToSpeech tts;  //class field

    private void textToSpeech(String text, Locale local) {
        final String toSpeak =text;
        final int mode = TextToSpeech.QUEUE_FLUSH;
        final HashMap<String, String> hashMap = new HashMap<String, String>();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                    tts.setLanguage(local);
                    tts.speak(toSpeak, mode, hashMap);
                }
            }
        });
    }



    public String cleanJpSentence(String sen){

       // "[01]"
        sen=sen.replaceAll("[\\[0-9+\\]]","");
       // sen=sen.replaceAll("\\(.*?\\)"," ");
      //  sen=sen.replaceAll("\\{.*?\\}"," ");
        sen=sen.replaceAll("\\("," ( ");
        sen=sen.replaceAll("\\)"," ) ");

        sen=sen.replaceAll("\\{"," ( ");
        sen=sen.replaceAll("\\}"," ) ");
        //sen=sen.replaceAll("\\{"," {");

        Log.d(">>>", sen);


        return sen;
    }
    private void showSentence(String en_sen, String jp_sen ) {

        jp_sen=cleanJpSentence(jp_sen);

        Fragment fragment = new SentenceView();

        Bundle bundle = new Bundle();
        //bundle.putString("state", "1");
        bundle.putString("jp_sen", jp_sen);
        bundle.putString("en_sen", en_sen);

        bundle.putString("words", uiDbFetcher.searchWord(jp_sen).toString());
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.svf, fragment).commitAllowingStateLoss();
    }


    UiDbFetcher uiDbFetcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uiDbFetcher= new UiDbFetcher(getApplicationContext());
        uiDbFetcher.openConnection();

        SentenceSearch sentenceSearch = new SentenceSearch(getApplicationContext());
        sentenceSearch.setSentenceSearchListener(new SentenceSearch.OnSentenceSearchFinished() {
            @Override
            public void onSearchFinished(JSONArray list) {

                for (int i = 0; i < list.length(); i++) {
                    try {


                        showSentence(list.getJSONObject(i).getString("en_sen"),list.getJSONObject(i).getString("jp_sen"));
                        Log.d("sen", list.getJSONObject(i).toString());
                      //  Toast.makeText(MainActivity.this, list.getJSONObject(i).toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });






       et= findViewById(R.id.editText);
       act= findViewById(R.id.btn_act);


        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //textToSpeech("hello there , how are you doing ", Locale.ENGLISH);
                sentenceSearch.getNextResult(et.getText().toString());

            }
        });

        act.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                sentenceSearch.getResult(et.getText().toString());
                return true;
            }
        });






    }

    public  void breakWords(String phrase){


        String tw="";
        for (int i = 0; i <phrase.length() ; i++) {

            tw+=phrase.charAt(i);

            if(uiDbFetcher.isWord(tw)){
                Log.d("isWord", tw);
                tw="";
            }



        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    //    recommendWords();
        breakWords("時間（じかん、羅: tempus 英: time）とは、出来事や変化を認識するための基礎的な概念である。芸術、哲学、自然科学、心理学などで重要なテーマとして扱われることもある。分野ごとに定義が異なる。");


    }


    public void recommendWords() {
        Intent intent = new Intent(getApplicationContext(), CoreService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}