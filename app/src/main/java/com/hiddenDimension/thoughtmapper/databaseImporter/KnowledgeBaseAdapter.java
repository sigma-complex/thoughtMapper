package com.hiddenDimension.thoughtmapper.databaseImporter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

public class KnowledgeBaseAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase cDb;
    private SQLiteDatabase dDb;
    private SQLiteDatabase wDb;



    private CorpusDatabaseImporter corpusDbHelper;
    private DictionaryDatabaseImporter dicDbHelper;
    private WordListDatabaseImporter wordDbHelper;

    public KnowledgeBaseAdapter(Context context) {
        this.mContext = context;
        corpusDbHelper = new CorpusDatabaseImporter(mContext);
        dicDbHelper = new DictionaryDatabaseImporter(mContext);
        wordDbHelper = new WordListDatabaseImporter(mContext);
    }

    public KnowledgeBaseAdapter createDatabase() throws SQLException {
        try {
            corpusDbHelper.createDataBase();
            dicDbHelper.createDataBase();
            wordDbHelper.createDataBase();

        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public KnowledgeBaseAdapter open() throws SQLException, java.sql.SQLException {
        try {
            corpusDbHelper.openDataBase();
            dicDbHelper.openDataBase();
            wordDbHelper.openDataBase();

            corpusDbHelper.close();
            dicDbHelper.close();
            wordDbHelper.close();

            cDb = corpusDbHelper.getReadableDatabase();
            dDb = dicDbHelper.getReadableDatabase();
            wDb = wordDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        corpusDbHelper.close();
        dicDbHelper.close();
        wordDbHelper.close();
    }




    public String buildQueryForDirectWord(String args){



        String sql ="";

            sql+= "SELECT * from jp_dic where eng match \""+args+"\" UNION ALL ";
            sql+= "SELECT * from jp_dic where kanji match \""+args+"\" UNION ALL ";
            sql+= "SELECT * from jp_dic where hiragana match \""+args+"\" limit 50  " ;


        return  sql;
    }


    public String buildQueryForSentences(String[] args){



        String sql ="";
        for (int i = 0; i <args.length ; i++) {
            sql+= "SELECT * from jp_dic where eng match \""+args[i]+"\" UNION ALL ";
            sql+= "SELECT * from jp_dic where eng match \""+args[i]+"%\" UNION ALL ";
            sql+= "SELECT * from jp_dic where eng match \"%"+args[i]+"%\" UNION ALL ";
        }


        for (int i = 0; i <args.length ; i++) {
            sql+= "SELECT * from jp_dic where kanji match \""+args[i]+"\" UNION ALL ";
            sql+= "SELECT * from jp_dic where kanji match \""+args[i]+"%\" UNION ALL ";
            sql+= "SELECT * from jp_dic where kanji match \"%"+args[i]+"%\" UNION ALL ";
        }

        for (int i = 0; i <args.length ; i++) {

            if(i<args.length-1) {
                sql += "SELECT * from jp_dic where hiragana match \"" + args[i] + "\" UNION ALL ";
                sql+= "SELECT * from jp_dic where hiragana match \""+args[i]+"%\" UNION ALL " ;
                sql+= "SELECT * from jp_dic where hiragana match \"%"+args[i]+"%\" UNION ALL " ;
            }
            else{
                sql += "SELECT * from jp_dic where hiragana match \"" + args[i] + "\" UNION ALL ";
                sql+= "SELECT * from jp_dic where hiragana match \""+args[i]+"%\" UNION ALL " ;
                sql+= "SELECT * from jp_dic where hiragana match \"%"+args[i]+"%\" limit 50  " ;
            }
        }

        return  sql;
    }
    public Cursor searchWordsByInput(String q) {
        try {
            String sql=buildQueryForSentences(q.split(" "));

            Log.d(">>>" , sql);
            Cursor mCur = dDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor searchWordsByDirectInput(String q) {
        try {
            String sql=buildQueryForDirectWord(q);

            Log.d(">>>" , sql);
            Cursor mCur = dDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor searchSentencesByInput(String q , int skip) {
        try {

//            Cursor mCur
//            =mDb.query(false, "corpus", new String[] { "jpsen",
//                            "ensen" }, "ensen" + " match ?",
//                     q.split(" "), null, null, null,
//                    "400");

            String sql ="SELECT * from corpus where ensen match \""+q+"\"  UNION ALL "
                       +"SELECT * from corpus where ensen match \""+q+"%\"  UNION ALL "
                       +"SELECT * from corpus where ensen match \"%"+q+"%\"  UNION ALL "
                       +"SELECT * from corpus where jpsen match \""+q+"\"  UNION ALL "
                       +"SELECT * from corpus where jpsen match \""+q+"%\"  UNION ALL "
                       +"SELECT * from corpus where jpsen match \"%"+q+"%\" "
                       +" limit " + skip +", 999"
                        ;

            Log.d("" , sql);
       //     mDb.setMaxSqlCacheSize(SQLiteDatabase.MAX_SQL_CACHE_SIZE);
            Cursor mCur = cDb.rawQuery(sql, null);

           // mCur.set
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


   public Cursor getTodayWord(int offset){

        try {
            String sql ="SELECT * from \"word_list\" LIMIT 2 offset "+offset;
            Cursor mCur = wDb.rawQuery(sql, null);

            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }

    }


}