package com.example.trieuphu.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.lifecycle.Observer;

import com.example.trieuphu.R;
import com.example.trieuphu.databinding.FragmentHomeBinding;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.util.Constant;
import com.example.trieuphu.viewmodel.HomeViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeViewModel,FragmentHomeBinding> {
    private List<Player> rankList;
    private boolean isStarted = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected Class<HomeViewModel> getClassVM() {
        return HomeViewModel.class;
    }

    @Override
    protected void init() {
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.bgmusic);
        mediaPlayer.setLooping(true);
        dataBinding.layoutAl.layoutHomeAfterLoad.setVisibility(View.GONE);
        viewModel.setContext(context);
        if (rankList != null){
            viewModel.setRankList(rankList);
        }
        showHome();

        //---on click
        dataBinding.layoutAl.btHighscore.setOnClickListener(v ->{
            actionClick.onClick(Constant.KEY_TO_HIGH_SCORE,null);
        });

        dataBinding.layoutAl.btSetting.setOnClickListener(v -> actionClick.onClick(Constant.KEY_TO_SETTING,null));

        dataBinding.layoutAl.btAbout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_about);
            dialog.show();
        });

        dataBinding.layoutAl.btPlay.setOnClickListener(v -> {
            mediaPlayer.stop();
            actionClick.onClick(Constant.KEY_TO_PLAY,viewModel.getRankList().getValue());
        });

        //---Observe the data
        viewModel.getSwitchMusicBGState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                } else {
                    if (isStarted) {
                        mediaPlayer.start();
                    }
                }
            }
        });

    }

    @Override
    public void onStop() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        if(isStarted && viewModel.getSwitchMusicBGState().getValue()){
            mediaPlayer.start();
        }
        super.onResume();
    }

    private void showHome(){
        new Handler().postDelayed(() -> {
            dataBinding.layoutAl.layoutHomeAfterLoad.setVisibility(View.VISIBLE);
            dataBinding.progressBar.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_circle);
            dataBinding.circleRound.startAnimation(animation);
            Animation animation1 = AnimationUtils.loadAnimation(getContext(),R.anim.home_show_after_load);
            dataBinding.layoutAl.layoutHomeAfterLoad.startAnimation(animation1);
            viewModel.setSwitchMusicBGState(viewModel.getPreferences().getBoolean(Constant.KEY_STATE_MUSIC_BG,true));
            isStarted = true;
        },2000);
    }

    public void setRankList(List<Player> players){
        rankList = players;
    }

}
