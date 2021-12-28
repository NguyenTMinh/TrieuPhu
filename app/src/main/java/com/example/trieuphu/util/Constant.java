package com.example.trieuphu.util;

import android.content.Context;

import com.example.trieuphu.R;

public class Constant {

    public static final int KEY_TO_HOME = 0;
    public static final int KEY_TO_HIGH_SCORE = 1;
    public static final int KEY_TO_SETTING = 2;
    public static final int KEY_TO_PLAY = 3;

    public static final String KEY_STATE_MUSIC_BG = "KEY_STATE_MUSIC_BG";
    public static final String SHARED_PREF_NAME = "data";
    public static final String NO_INFO = "NO_INFO";

    public static final String[] LABEL_ANSWER = {"A. ","B. ","C. ","D. "};
    public static final int[] ANSWER_ID = {R.id.ans_0,R.id.ans_1,R.id.ans_2,R.id.ans_3};
    public static final int[] ANSWER_SOUND = {R.raw.ans_a,R.raw.ans_b,R.raw.ans_c,R.raw.ans_d};
    public static final int[] ANSWER_SOUND_TRUE = {R.raw.true_a,R.raw.true_b,R.raw.true_c,R.raw.true_d2};
    public static final int[] ANSWER_SOUND_FALSE = {R.raw.lose_a,R.raw.lose_b,R.raw.lose_c,R.raw.lose_d};
    public static final int PLAY_TIME = 30;  //second
    public static int PLAY_TIME_UNTIL_END = PLAY_TIME;

}
