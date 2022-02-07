package com.hiddenDimension.thoughtmapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hiddenDimension.thoughtmapper.backend.SentenceSearch;
import com.hiddenDimension.thoughtmapper.backgroud.CoreService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SentenceSearch sentenceSearch= new SentenceSearch(getApplicationContext());
        sentenceSearch.setSentenceSearchListener(new SentenceSearch.OnSentenceSearchFinished() {
            @Override
            public void onSearchFinished(JSONArray list)  {

                for (int i = 0; i <list.length() ; i++) {
                    try {

                        Log.d("sen", list.getJSONObject(i).toString());
                        Toast.makeText(MainActivity.this, list.getJSONObject(i).toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sentenceSearch.getNextResult("a");


    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    public void recommendWords(){
        Intent intent= new Intent(getApplicationContext(), CoreService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else{
            startService(intent);
        }
    }
}