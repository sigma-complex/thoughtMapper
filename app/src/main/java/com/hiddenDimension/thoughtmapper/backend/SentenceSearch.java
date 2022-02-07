package com.hiddenDimension.thoughtmapper.backend;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.hiddenDimension.thoughtmapper.databaseImporter.KnowledgeBaseAdapter;
import com.hiddenDimension.thoughtmapper.model.Sentence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SentenceSearch {


    public static final int delay = 100;
    JSONArray list;
    Handler handler;
    KnowledgeBaseAdapter mDbHelper;


    int limit=1, skip=0;



    public SentenceSearch(Context context){

        handler=new Handler();
        mDbHelper = new KnowledgeBaseAdapter(context);
        mDbHelper.createDatabase();

        try {
            mDbHelper.open();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }




    public void getResult(String searchQuery, int lim , int skip){

        list= new JSONArray();

        if (searchQuery.toString().length() <= 2)
            handler.removeCallbacksAndMessages(null);

        final Runnable r = new Runnable() {
            public void run() {

                try {
                    Cursor testdata = mDbHelper.searchSentencesByInput(searchQuery.toString() , skip);
                    while (testdata.moveToNext()) {
                        {
                            JSONObject sen = new JSONObject();
                            try {
                                sen.put("jpsen", testdata.getString(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                sen.put("ensen", testdata.getString(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(list.length()<lim)
                            list.put(sen);
                            else{
                                break;
                            }
                        }
                    }


                    if(onSentenceSearch!=null)
                    onSentenceSearch.onSearchFinished(list);
                    else{
                        throw new Exception("Please set onSentenceSearch listener before calling this.");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handler.postDelayed(r, delay);
    }


    public void getResult(String searchQuery){
        getResult(searchQuery,limit,skip);
    }

    public void getNextResult(String searchQuery){

        getResult(searchQuery,limit,++skip);
    }

    public void getPrevResult(String searchQuery){
        getResult(searchQuery,limit,--skip);
    }


    public  OnSentenceSearchFinished onSentenceSearch;
    public interface OnSentenceSearchFinished{

        public void onSearchFinished( JSONArray list);

    }

    public  void setSentenceSearchListener(OnSentenceSearchFinished ssf){
        onSentenceSearch=ssf;
    }



}
