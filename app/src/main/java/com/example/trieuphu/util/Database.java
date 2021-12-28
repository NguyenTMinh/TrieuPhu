package com.example.trieuphu.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.trieuphu.model.Level;
import com.example.trieuphu.model.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Question.db";
    private Context mContext;
    private SQLiteDatabase mdataBase;
    private final String DATABASE_LOCATION;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
        DATABASE_LOCATION = mContext.getFilesDir().getPath().replace("files","databases/");
        copyDataBase();
    }

    public void openDataBase(){
        String dbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
        if(mdataBase != null && mdataBase.isOpen()){
            return;
        }
        mdataBase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if(mdataBase != null){
            mdataBase.close();
        }
    }

    public List<Question> getQuestions(int level){
        List<Question> list = new ArrayList<>();
        openDataBase();
        int level_sub = level+1;
        String sqlQueryTable = "SELECT * FROM question"+level_sub;
        Cursor cursor = mdataBase.rawQuery(sqlQueryTable,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            List<String> list1 = new ArrayList<>();
            for (int i = 2; i < 6 ; i++) {
                list1.add(cursor.getString(i));
            }
            Question question = new Question(level,cursor.getString(1),list1,cursor.getInt(6));
            list.add(question);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return list;
    }

    public List<Level> getLevels(){
        List<Level> list = new ArrayList<>();
        openDataBase();
        Cursor cursor = mdataBase.rawQuery("SELECT*FROM money_level",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Level level = new Level(cursor.getInt(0),cursor.getInt(1));
            list.add(level);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return list;
    }


    public void copyDataBase(){
        File file = mContext.getApplicationContext().getDatabasePath(DATABASE_NAME);
        if(!file.exists()){
            this.getReadableDatabase();
            try {
                InputStream inputStream = mContext.getAssets().open(DATABASE_NAME);
                String fileName = DATABASE_LOCATION + DATABASE_NAME;
                OutputStream outputStream = new FileOutputStream(fileName);
                byte[] buff = new byte[1024];
                int length;
                while ((length = inputStream.read(buff)) > 0){
                    outputStream.write(buff,0,length);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
