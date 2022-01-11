package com.example.trieuphu.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trieuphu.model.Level;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataRepository {
    private static DataRepository instance;
    private static Context mContext;
    private static List<Level> mLevelList;
    private static List<Level> mReverseLevelList;
    private static List<List<Question>> mQuestionList;
    private static SharedPreferences mPreferences;
    private static List<Player> mPlayerList;

    private DataRepository(){

    }

    public static DataRepository getInstance(Context context){
        if(instance == null){
            instance = new DataRepository();
            mContext = context.getApplicationContext();
            getData();
        }
        return instance;
    }

    private static void getData() {
        Database database = new Database(mContext);
        mLevelList = database.getLevels();
        mReverseLevelList = (List<Level>) new ArrayList<>(mLevelList).clone();
        Collections.reverse(mReverseLevelList);
        mQuestionList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            List<Question> list = database.getQuestions(i);
            mQuestionList.add(list);
        }
        mPreferences = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        mPlayerList = new ArrayList<>();
        for (int i = 1; i <= 10 ; i++) {
            String key = i+"";
            if (mPreferences.contains(key)){
                String line = mPreferences.getString(key,Constant.NO_INFO);
                String[] playerData = line.split("_");
                Player player = new Player(playerData[0],Integer.parseInt(playerData[1]));
                mPlayerList.add(player);
            }else {
                break;
            }
        }
    }

    public List<Level> getLevelList(){
        return mLevelList;
    }

    public List<Level> getReverseLevelList(){
        return mReverseLevelList;
    }

    public List<List<Question>> getQuestionList() {
        return mQuestionList;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public List<Player> getPlayerList() {
        return mPlayerList;
    }
}
