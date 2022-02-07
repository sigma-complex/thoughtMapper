package com.hiddenDimension.thoughtmapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.hiddenDimension.thoughtmapper.databaseImporter.KnowledgeBaseAdapter;

import java.util.Random;

public class UiDbFetcher {

    private final Context mContext;
    public KnowledgeBaseAdapter mDbHelper;

    Random random;

    public UiDbFetcher(Context mContext) {
        this.mContext = mContext;

        mDbHelper = new KnowledgeBaseAdapter(mContext);
        mDbHelper.createDatabase();
        random= new Random(3000);



    }

    public void openConnection(){
        try {
            mDbHelper.open();
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void closeConnection(){

            mDbHelper.close();

    }

    public void setLatestOffset(int offset) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("offset", random.nextInt(3000));
        editor.apply();
    }

    public int getLatestOffset() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getInt("offset", 0);
    }

    public String getTodayWord() {


        Cursor word = mDbHelper.getTodayWord(getLatestOffset());
        String words = "";
        while (word.moveToNext()) {
            words = words + word.getString(1) + " ";
            //  Toast.makeText(this, ""+words, Toast.LENGTH_SHORT).show();
        }


        words = words.replaceAll("\\(.*?\\)", " ");
        words = words.replaceAll("\\{.*?\\}", " ");
        words = words.replaceAll("\\[.*?\\]", " ");
        //inp.setText(words);
        return words;
    }

    String tv_wrd="";
    public String searchWord() {
        Cursor x = mDbHelper.searchWordsByInput(getTodayWord());
        tv_wrd="";
        while (x.moveToNext()) {

//            tv_wrd+=("" + x.getString(0));
//            tv_wrd+=(System.lineSeparator());

            tv_wrd+=("" + x.getString(1));
          //  tv_wrd+=(System.lineSeparator());
//            tv_wrd+=("" + x.getString(2));
//            tv_wrd+=(System.lineSeparator());

            tv_wrd+=(" : " + x.getString(3));
            tv_wrd+=(System.lineSeparator());

//            tv_wrd+=("" + x.getString(4));
//            tv_wrd+=(System.lineSeparator());
//
//            tv_wrd+=("" + x.getString(5));
//            tv_wrd+=(System.lineSeparator());
//
//            tv_wrd+=("" + x.getString(6));
//            tv_wrd+=(System.lineSeparator());

        }

        return tv_wrd;
    }


}
