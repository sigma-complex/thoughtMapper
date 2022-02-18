package com.hiddenDimension.thoughtmapper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class SentenceView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String JP_SEN = "jp_sen";
    private static final String EN_SEN = "en_sen";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    JSONArray words;

    RecyclerView recyclerView;
    TextView jp_sen, en_sen ;



    public SentenceView() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(JP_SEN);
            mParam2 = getArguments().getString(EN_SEN);
            try {
                words= new JSONArray(getArguments().getString("words"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sentence_view, container, false);


        jp_sen= v.findViewById(R.id.tv_jp_sen);
        en_sen=v.findViewById(R.id.tv_en_sen);

        jp_sen.setTextIsSelectable(true);
        en_sen.setTextIsSelectable(true);

        jp_sen.setText(mParam1);
        en_sen.setText(mParam2);

        jp_sen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeechJp(mParam1);
            }
        });

        en_sen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeechEn(mParam2);
            }
        });

        recyclerView = v.findViewById(R.id.elv);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        final List<String> myDataset = new ArrayList<>();

        for (int i = 0; i <words.length() ; i++) {

            try {
                Log.d("words", words.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                myDataset.add( words.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



       RecyclerViewOfListAdapter rvla=  new RecyclerViewOfListAdapter(myDataset);

        rvla.setOnItemClickListener(new RecyclerViewOfListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, String s) {
//                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    textToSpeechEn(new JSONObject(s).getString("3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("rv", s);
            }

            @Override
            public void onKanjiClick(String kanji) {

                textToSpeechJp(kanji);
            }
        });
        recyclerView.setAdapter(rvla);

        return v;
    }

    TextToSpeech ttsEn;  //class field
    TextToSpeech ttsJp;  //class field

    private void textToSpeechEn(String text) {
        final String toSpeak =text;
        final int mode = TextToSpeech.QUEUE_FLUSH;
        final HashMap<String, String> hashMap = new HashMap<String, String>();

        ttsEn = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                    ttsEn.setLanguage(Locale.US);
                    ttsEn.speak(toSpeak, mode, hashMap);



                }


            }
        });
    }

    private void textToSpeechJp(String text) {
        final String toSpeak =text;
        final int mode = TextToSpeech.QUEUE_FLUSH;
        final HashMap<String, String> hashMap = new HashMap<String, String>();

        ttsJp = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                    ttsJp.setLanguage(Locale.JAPAN);
                    ttsJp.speak(toSpeak, mode, hashMap);


                }
            }
        });
    }
}