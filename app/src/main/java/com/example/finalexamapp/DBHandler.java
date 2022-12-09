package com.example.finalexamapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHandler extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    private static final String DB_NAME = "products.db";
    private static String DB_PATH = "/data/user/0/com.example.finalexamapp/databases/";
    SQLiteDatabase myDatabase;
    private final Context mContext;
    private static final String PRODUCTS_TABLE = "PRODUCTS_TABLE";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_QUANTITY = "PRODUCT_QUANTITY";
    private static final String PRODUCT_PRICE = "PRODUCT_PRICE";


    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + PRODUCTS_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCT_NAME + "TEXT, " + PRODUCT_QUANTITY + " INT, " + PRODUCT_PRICE + "TEXT)";
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private boolean checkDatabase(){
        try {
            final String mPath = DB_PATH + DB_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            return false;
        }catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }

    private void copyDatabase() throws IOException{
        try {
            InputStream mInputStream = mContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH +DB_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);

            byte [] buffer = new byte[1024];
            int length;

            while((length = mInputStream.read(buffer)) > 0){
                mOutputStream.write(buffer, 0, length);
            }

            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createDatabase() throws  IOException {
        boolean mDatabaseExists = checkDatabase();
        if (!mDatabaseExists){
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
            }catch (IOException mIOException){
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            }finally {
                this.close();
            }
        }
    }
    @Override
    public synchronized  void close(){
        if(myDatabase != null){
            myDatabase.close();
        }
    }

    public boolean addProduct(product productToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_NAME, productToAdd.name);
        cv.put(PRODUCT_QUANTITY, productToAdd.quantity);
        cv.put(PRODUCT_PRICE, productToAdd.price);

        long insert = db.insert(PRODUCTS_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        return true;
    }
}
