package com.example.trieuphu.view.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.trieuphu.R;
import com.example.trieuphu.intf.OnActionClick;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.util.Constant;
import com.example.trieuphu.view.fragment.HighScoreFragment;
import com.example.trieuphu.view.fragment.HomeFragment;
import com.example.trieuphu.view.fragment.PlayFragment;
import com.example.trieuphu.view.fragment.SettingFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnActionClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setActionClick(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
    }

    public void tranFragment(int layouID,
                             Fragment fragment,
                             boolean addToBackStack,
                             boolean replaceAction,
                             int animEnter, int animExit, int animPopEnter, int animPopExit){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animEnter,animExit,animPopEnter,animPopExit);
        if(replaceAction){
            transaction.replace(layouID,fragment);
        }else {
            transaction.add(layouID,fragment);
        }
        if(addToBackStack) {
            transaction.addToBackStack("add");
        }
        transaction.commit();
    }

    @Override
    public void onClick(int key, Object data) {
        switch (key){
            case Constant.KEY_TO_HIGH_SCORE:{
                HighScoreFragment highScoreFragment = new HighScoreFragment();
                tranFragment(R.id.main_container,highScoreFragment,true,false,
                        R.anim.floating_from_the_bottom_up,0,0,R.anim.dive_to_the_bottom);
                break;
            }
            case Constant.KEY_TO_SETTING:{
                SettingFragment settingFragment = new SettingFragment();
                tranFragment(R.id.main_container,settingFragment,true,false,
                        R.anim.floating_from_the_bottom_up,0,0,R.anim.dive_to_the_bottom);
                break;
            }
            case Constant.KEY_TO_PLAY:{
                PlayFragment playFragment = new PlayFragment();
                playFragment.setActionClick(this);
                playFragment.passPlayers((List<Player>) data);
                tranFragment(R.id.main_container,playFragment,false,true,
                        R.anim.enter_from_left,R.anim.slow_disappear,0,R.anim.dive_to_the_bottom);
                break;
            }
            case Constant.KEY_TO_HOME:{
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setActionClick(this);
                if (data != null){
                    homeFragment.setRankList((List<Player>) data);
                }
                tranFragment(R.id.main_container,homeFragment,false,true,
                        R.anim.floating_from_the_bottom_up,0,0,0);
            }
        }
    }
}