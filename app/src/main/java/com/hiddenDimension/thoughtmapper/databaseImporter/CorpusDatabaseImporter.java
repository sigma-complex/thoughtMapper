package com.hiddenDimension.thoughtmapper.databaseImporter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CorpusDatabaseImporter extends SQLiteOpenHelper
{
        private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
        private static String DB_NAME="corpus_fts4"; // Database name
        private static int DB_VERSION = 1; // Database version
        private final File DB_FILE;
        private SQLiteDatabase mDataBase;
        private final Context mContext;

        public CorpusDatabaseImporter(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            DB_FILE = context.getDatabasePath(DB_NAME);
            this.mContext = context;
        }

        public void createDataBase() throws IOException {
            // If the database does not exist, copy it from the assets.
            boolean mDataBaseExist = checkDataBase();
            if(!mDataBaseExist) {
                this.getReadableDatabase();
                this.close();
                try {
                    // Copy the database from assests
                    copyDataBase();
                    Log.e(TAG, "createDatabase database created");
                } catch (IOException mIOException) {
                    throw new Error("ErrorCopyingDataBase");
                }
            }
        }

        // Check that the database file exists in databases folder
        private boolean checkDataBase() {
            return DB_FILE.exists();
        }

        // Copy the database from assets
        private void copyDataBase() throws IOException {
            InputStream mInput = mContext.getAssets().open(DB_NAME);
            OutputStream mOutput = new FileOutputStream(DB_FILE);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                //Log.d(TAG, "copyDataBase: "+mBuffer);
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
        }

        // Open the database, so we can query it
        public boolean openDataBase() {
             Log.d("DB_PATH", DB_FILE.getAbsolutePath());
            mDataBase = SQLiteDatabase.openDatabase(DB_FILE.getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
          //   mDataBase = SQLiteDatabase.openDatabase(DB_FILE.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            return mDataBase != null;
        }

        @Override
        public synchronized void close() {
            if(mDataBase != null) {
                mDataBase.close();
            }
            super.close();
        }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}