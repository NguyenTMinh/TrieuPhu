package com.example.trieuphu.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

public class SoundRepo {
    private static SoundRepo instance;
    private static SoundPool soundPool;
    private static int[] poolIDs;
    private static Context mContext;

    private SoundRepo(){}

    public static SoundRepo getInstance(Context context){
        if (instance == null){
            mContext = context;
            instance = new SoundRepo();
            initPool();
        }
        return instance;
    }

    private static void initPool() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(10)
                    .build();
        }else{
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }
        poolIDs = new int[15];
        for (int i = 0; i < poolIDs.length; i++) {
            poolIDs[i] = soundPool.load(mContext
                    ,mContext.getResources().getIdentifier("ques"+(i+1),"raw",mContext.getPackageName())
                    ,1);
        }
    }

    public void playSound(int pos){
        soundPool.play(poolIDs[pos],1,1,1,0,1);
    }
}
