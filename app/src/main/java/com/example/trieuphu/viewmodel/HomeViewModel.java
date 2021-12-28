package com.example.trieuphu.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trieuphu.model.Level;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.model.Question;
import com.example.trieuphu.util.Constant;
import com.example.trieuphu.util.Database;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Boolean> switchMusicBGState;
    private MutableLiveData<List<Player>> rankList;
    private SharedPreferences preferences;
    private Context context;
    private boolean isCreated = false;

    public HomeViewModel(){
        switchMusicBGState = new MutableLiveData<>();
        rankList = new MutableLiveData<>();
    }

    public LiveData<Boolean> getSwitchMusicBGState() {
        return switchMusicBGState;
    }

    public LiveData<List<Player>> getRankList(){
        return rankList;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setSwitchMusicBGState(boolean state){
        switchMusicBGState.postValue(state);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constant.KEY_STATE_MUSIC_BG,state);
        editor.commit();
    }

    public void setContext(Context context) {
        this.context = context;
        if (!isCreated){
            initData();
        }
    }

    public void setRankList(List<Player> list){
        rankList.postValue(list);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < list.size(); i++) {
            String data = list.get(i).getName()+"_"+list.get(i).getScore();
            editor.putString(String.valueOf(i+1),data);
            editor.commit();
        }
    }

    private void initData(){
        Database database = new Database(context);
        preferences = context.getSharedPreferences(Constant.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        List<Player> list = new ArrayList<>();
        for (int i = 1; i <= 10 ; i++) {
            String key = i+"";
            if (preferences.contains(key)){
                String line = preferences.getString(key,Constant.NO_INFO);
                String[] playerData = line.split("_");
                Player player = new Player(playerData[0],Integer.parseInt(playerData[1]));
                list.add(player);
            }else {
                break;
            }
        }
        setRankList(list);
        isCreated = true;
    }

}
