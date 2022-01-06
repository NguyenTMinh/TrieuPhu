package com.example.trieuphu.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trieuphu.R;
import com.example.trieuphu.adapter.LevelAdapter;
import com.example.trieuphu.databinding.DialogReadyOrNotBinding;
import com.example.trieuphu.databinding.FragmentPlayBinding;
import com.example.trieuphu.intf.IRuntime;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.model.Question;
import com.example.trieuphu.util.Constant;
import com.example.trieuphu.util.SoundRepo;
import com.example.trieuphu.view.dialog.AudienceDialog;
import com.example.trieuphu.view.dialog.CallHelpDialog;
import com.example.trieuphu.view.dialog.ConfirmDialog;
import com.example.trieuphu.viewmodel.PlayViewModel;

import java.text.NumberFormat;
import java.util.List;

public class PlayFragment extends BaseFragment<PlayViewModel, FragmentPlayBinding> implements MediaPlayer.OnCompletionListener {
    private boolean isPausedGame = false;
    private boolean isGameEnd = false;
    private boolean isGameStarted = false;
    private boolean isSoundPlaying = false;
    private boolean onStartGame = false;
    private List<Player> players;
    private CountDownTimer timer;
    private CountDownTimer count;
    private SoundRepo soundRepo;
    private int i = 10;

    @Override
    protected int getLayout() {
        return R.layout.fragment_play;
    }

    @Override
    protected Class<PlayViewModel> getClassVM() {
        return PlayViewModel.class;
    }

    @Override
    protected void init() {
        soundRepo = SoundRepo.getInstance(context);
        viewModel.setContext(context);
        viewModel.setPlayers(players);
        dataBinding.includedLay.containerPlayView.setVisibility(View.GONE);
        dataBinding.drawerLayout.openDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(),dataBinding.drawerLayout,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(onStartGame){
                    onStartGame = false;
                    setStateOnView(true);
                    countTime(Constant.PLAY_TIME*1000);
                }
            }
        };
        dataBinding.drawerLayout.addDrawerListener(toggle);

        initLevelViews();
        setMediaPlayer(R.raw.luatchoi_n_ready,false);

        //on click
        dataBinding.navLevel.btHide.setOnClickListener(v -> dataBinding.drawerLayout.closeDrawers());
        dataBinding.includedLay.btProfile.setOnClickListener(v -> dataBinding.drawerLayout.openDrawer(GravityCompat.START));

        setUpHelper();

        //logic for right choice
        for (int i = 0; i < 4; i++) {
            TextView textView = (TextView) dataBinding.includedLay.layoutContainsAnswer.getChildAt(i);
            textView.setOnClickListener(v ->{
                isGameStarted = false;
                setStateOnView(false);
                timer.cancel();
                viewModel.setPlayerCurrentAnswer(v.getId());
                v.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_selected);
                mediaPlayer.stop();
                setMediaPlayer(Constant.ANSWER_SOUND[viewModel.getPlayerCurrentAnswer()],false);
            });
        }

        //observe data
        viewModel.getQuestionLiveData().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                String time = String.valueOf(Constant.PLAY_TIME);
                dataBinding.includedLay.tvTime.setText(time);
                String order = "Câu "+(question.getLevelQuestion()+1);
                dataBinding.includedLay.tvQuestionOrder.setText(order);
                dataBinding.includedLay.tvQuestionDisplay.setText(question.getQuestion());
                String answer;
                for (int i = 0; i < question.getChoice().size(); i++) {
                    TextView textView = (TextView) dataBinding.includedLay.layoutContainsAnswer.getChildAt(i);
                    answer = Constant.LABEL_ANSWER[i] + question.getChoice().get(i);
                    textView.setText(answer);
                }
            }
        });

        viewModel.getTimeLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String time = String.valueOf(integer);
                dataBinding.includedLay.tvTime.setText(time);
            }
        });

        viewModel.getPlayerMoneyLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                StringBuilder string = new StringBuilder(format.format(integer));
                string.deleteCharAt(string.length()-1);
                dataBinding.includedLay.tvMoney.setText(string);
            }
        });
    }

    public void showReadyDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogReadyOrNotBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_ready_or_not,null,false);
        dialog.setContentView(binding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        binding.btQuit.setOnClickListener(v -> {
		    isPausedGame = true;
		    dialog.cancel();
		    actionClick.onClick(Constant.KEY_TO_HOME,null);
	    });
        binding.btReady.setOnClickListener(v ->{
            dataBinding.drawerLayout.closeDrawers();
            dialog.cancel();
            setMediaPlayer(R.raw.gofind,false);
        } );
        dialog.show();
    }

    //rec
    private void initLevelViews() {
        LevelAdapter adapter = new LevelAdapter(viewModel.getReverseLevels(),context);
        dataBinding.navLevel.layoutLevel.setAdapter(adapter);
        dataBinding.navLevel.layoutLevel.setLayoutManager(new LinearLayoutManager(context));
        dataBinding.navLevel.layoutLevel.setHasFixedSize(false);
        count = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                dataBinding.navLevel.layoutLevel.getChildAt(i)
                        .setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
                if (i==5 || i == 0){
                    dataBinding.navLevel.layoutLevel.getChildAt(i+5).
                            setBackgroundResource(R.drawable.atp__activity_player_image_money_milestone);
                }
                i-=5;
            }

            @Override
            public void onFinish() {
                dataBinding.navLevel.layoutLevel.getChildAt(0).
                        setBackgroundResource(R.drawable.atp__activity_player_image_money_milestone);
            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count.start();
            }
        },1000);
    }

    private void setStateOnView(boolean state){
        dataBinding.includedLay.ans0.setClickable(state);
        dataBinding.includedLay.ans1.setClickable(state);
        dataBinding.includedLay.ans2.setClickable(state);
        dataBinding.includedLay.ans3.setClickable(state);
        dataBinding.includedLay.btGiveUp.setClickable(state);
        if (!viewModel.getHelp()[0]){
            dataBinding.includedLay.btChangeQuestion.setClickable(state);
        }
        if (!viewModel.getHelp()[1]){
            dataBinding.includedLay.bt5050.setClickable(state);
        }
        if (!viewModel.getHelp()[2]){
            dataBinding.includedLay.btAskAudience.setClickable(state);
        }
        if (!viewModel.getHelp()[3]){
            dataBinding.includedLay.btCallHelp.setClickable(state);
        }
    }

    private void setUpHelper(){
        dataBinding.includedLay.btGiveUp.setOnClickListener(v -> {
            View.OnClickListener listener = v1 ->{
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                timer.cancel();
                setMediaPlayer(R.raw.lose,false);
            };
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.init(getString(R.string.give_up),listener);
            confirmDialog.show();
        });
        dataBinding.includedLay.btChangeQuestion.setOnClickListener(v -> {
            View.OnClickListener listener = v1 -> {
                refreshPlayScreen();
                viewModel.setHelpUsed(0);
                viewModel.postQuestionValue(viewModel.getCurrentLevel());
                dataBinding.includedLay.btChangeQuestion.setImageResource(R.drawable.atp__activity_player_button_image_help_change_question_x);
                dataBinding.includedLay.btChangeQuestion.setClickable(false);
            };
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.init(getString(R.string.change_question),listener);
            confirmDialog.show();
        });
        dataBinding.includedLay.bt5050.setOnClickListener(v -> {
            View.OnClickListener listener = v1 -> {
                mediaPlayer.stop();
                timer.cancel();
                viewModel.setHelpUsed(1);
                dataBinding.includedLay.bt5050.setImageResource(R.drawable.atp__activity_player_button_image_help_5050_x);
                dataBinding.includedLay.bt5050.setClickable(false);
                setMediaPlayer(R.raw.sound5050_2,false);
            };
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.init(getString(R.string.remove_5050),listener);
            confirmDialog.show();
        });
        dataBinding.includedLay.btAskAudience.setOnClickListener(v -> {
            View.OnClickListener listener = v1 -> {
                mediaPlayer.stop();
                timer.cancel();
                viewModel.setHelpUsed(2);
                setMediaPlayer(R.raw.khan_gia,false);
                dataBinding.includedLay.btAskAudience.setImageResource(R.drawable.atp__activity_player_button_image_help_audience_x);
                dataBinding.includedLay.btAskAudience.setClickable(false);
            };
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.init(getString(R.string.audience_help),listener);
            confirmDialog.show();
        });
        dataBinding.includedLay.btCallHelp.setOnClickListener(v -> {
            View.OnClickListener listener = v1 -> {
                mediaPlayer.stop();
                timer.cancel();
                viewModel.setHelpUsed(3);
                setMediaPlayer(R.raw.help_call,false);
                dataBinding.includedLay.btCallHelp.setImageResource(R.drawable.atp__activity_player_button_image_help_call_x);
                dataBinding.includedLay.btCallHelp.setClickable(false);
            };
            ConfirmDialog confirmDialog = new ConfirmDialog(context);
            confirmDialog.init(getString(R.string.call_help),listener);
            confirmDialog.show();
        });
    }

    //game on
    public void startAGameSession(int level){
        setMediaPlayer(R.raw.background_music_while_playing,true);
        if(level != 0){
            refreshPlayScreen();
            updateLevelMoney(level-1);
        }
        dataBinding.drawerLayout.openDrawer(GravityCompat.START);
        dataBinding.navLevel.layoutLevel.getChildAt(14-level).setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
        viewModel.postQuestionValue(level);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dataBinding.includedLay.containerPlayView.getVisibility() == View.GONE){
                    dataBinding.includedLay.containerPlayView.setVisibility(View.VISIBLE);
                }
                if(dataBinding.drawerLayout.isDrawerOpen(GravityCompat.START) || !dataBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    dataBinding.drawerLayout.closeDrawers();
                }
            }
        },2000);
        onStartGame = true;
        isGameStarted = true;
    }

    public void onAfterSelectAnswer(){
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.right_answer);
        View v = dataBinding.includedLay.layoutContainsAnswer.getChildAt(viewModel.getPlayerCurrentAnswer());
        if(viewModel.checkAnswer(viewModel.getPlayerCurrentAnswer())){
            //player chooses right
            viewModel.increaseMoney();
            viewModel.nextLevel();
            setMediaPlayer(Constant.ANSWER_SOUND_TRUE[viewModel.getPlayerCurrentAnswer()],false);
            v.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
            v.startAnimation(animation);
        }else{
            //player chooses wrong
            setMediaPlayer(Constant.ANSWER_SOUND_FALSE[viewModel.getQuestionLiveData().getValue().getTrueCase()],false);
            v.setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_wrong);
            dataBinding.includedLay.layoutContainsAnswer
                    .getChildAt(viewModel.getQuestionLiveData().getValue().getTrueCase())
                    .setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_true);
            dataBinding.includedLay.layoutContainsAnswer
                    .getChildAt(viewModel.getQuestionLiveData().getValue().getTrueCase()).startAnimation(animation);
        }
    }

    public void onFinishGane(){
        Constant.PLAY_TIME_UNTIL_END = Constant.PLAY_TIME;
        viewModel.setCurrentLevel(0);
        isGameEnd = true;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_name);
        dialog.setCanceledOnTouchOutside(false);
        EditText editText = dialog.findViewById(R.id.et_name);
        Button buttonExit = dialog.findViewById(R.id.bt_quit_name);
        Button buttonOK = dialog.findViewById(R.id.bt_ok);
        buttonExit.setOnClickListener(v -> {
            dialog.cancel();
            actionClick.onClick(Constant.KEY_TO_HOME,players);
        });
        buttonOK.setOnClickListener(v -> {
            String name = editText.getText().toString();
            if(viewModel.checkNameValid(name)){
                dialog.cancel();
                viewModel.saveFinishGame(name);
                actionClick.onClick(Constant.KEY_TO_HOME,viewModel.getPlayers());
            }else {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Thông báo");
                alert.setMessage("Tên không hợp lệ");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        });
        dialog.show();
    }

    private void refreshPlayScreen(){
        Constant.PLAY_TIME_UNTIL_END = Constant.PLAY_TIME;
        for (int i = 0; i < 4; i++) {
            dataBinding.includedLay.layoutContainsAnswer.getChildAt(i).setVisibility(View.VISIBLE);
            dataBinding.includedLay.layoutContainsAnswer.getChildAt(i)
                    .setBackgroundResource(R.drawable.atp__activity_player_layout_play_answer_background_normal);
        }
    }

    private void updateLevelMoney(int pre_level){
        if(((pre_level+1)%5) == 0){
            dataBinding.navLevel.layoutLevel.getChildAt(14-pre_level).setBackgroundResource(R.drawable.atp__activity_player_image_money_milestone);
        }else{
            dataBinding.navLevel.layoutLevel.getChildAt(14-pre_level).setBackgroundResource(0);
        }
    }

    //media handler
    private void doOnCompleted(int mediaCurrentPlaying) {
        switch (mediaCurrentPlaying){
            case R.raw.luatchoi_n_ready:{
                showReadyDialog();
                break;
            }
            case R.raw.gofind:{
                viewModel.postPlayerMoneyValue(0);
                soundRepo.playSound(viewModel.getCurrentLevel());
                startAGameSession(viewModel.getCurrentLevel());
                break;
            }
            case R.raw.ans_a: case R.raw.ans_b: case R.raw.ans_c: case R.raw.ans_d:{
                onAfterSelectAnswer();
                break;
            }
            case R.raw.true_a: case R.raw.true_b: case R.raw.true_c: case R.raw.true_d2:{
                if(viewModel.getCurrentLevel()<15){
                    soundRepo.playSound(viewModel.getCurrentLevel());
                    startAGameSession(viewModel.getCurrentLevel());
                }else {
                    setMediaPlayer(R.raw.bgmusic,false);
                    setMediaPlayer(R.raw.best_player,false);
                    onFinishGane();
                }
                break;
            }
            case R.raw.out_of_time:case R.raw.lose_a: case R.raw.lose_b: case R.raw.lose_c: case R.raw.lose_d:{
                setMediaPlayer(R.raw.lose,false);
                break;
            }
            case R.raw.lose:{
                onFinishGane();
                break;
            }
            case R.raw.sound5050_2:{
                int ans = viewModel.getAnswerRemainAfter5050();
                for (int i = 0; i < 4; i++) {
                    if(i != viewModel.getQuestionLiveData().getValue().getTrueCase() && i != ans){
                        dataBinding.includedLay.layoutContainsAnswer.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                }
                setMediaPlayer(R.raw.background_music_while_playing,true);
                countTime(Constant.PLAY_TIME_UNTIL_END*1000);
                setStateOnView(true);
                break;
            }
            case R.raw.khan_gia:{
                AudienceDialog dialog = new AudienceDialog(context);
                dialog.init(viewModel.getAudienceChoice(), new IRuntime() {
                    @Override
                    public void doRuntime() {
                        countTime(Constant.PLAY_TIME_UNTIL_END*1000);
                    }
                });
                dialog.show();
                setStateOnView(true);
                setMediaPlayer(R.raw.background_music_while_playing,true);
                break;
            }
            case R.raw.help_call:{
                setMediaPlayer(R.raw.background_music_while_playing,true);
                CallHelpDialog callHelpDialog = new CallHelpDialog(context);
                callHelpDialog.init(viewModel.parseTrueCaseToText(), new IRuntime() {
                    @Override
                    public void doRuntime() {
                        setStateOnView(true);
                        countTime(Constant.PLAY_TIME_UNTIL_END*1000);
                    }
                });
                callHelpDialog.show();
                break;
            }
        }
    }

    public void setMediaPlayer(int resID, boolean loop){
        viewModel.setMediaCurrentPlaying(resID);
        mediaPlayer = MediaPlayer.create(context,viewModel.getMediaCurrentPlaying());
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setLooping(loop);
        mediaPlayer.start();
        isSoundPlaying = true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        new Handler().post(mp::release);
        isSoundPlaying = false;
        doOnCompleted(viewModel.getMediaCurrentPlaying());
    }

    //trans data
    public void passPlayers(List<Player> list){
        players = list;
    }

    public void countTime(long millstime){
         timer = new CountDownTimer(millstime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.postTime(Constant.PLAY_TIME_UNTIL_END--);
            }

            @Override
            public void onFinish() {
                Log.e("D","END");
                mediaPlayer.stop();
                setMediaPlayer(R.raw.out_of_time,false);
                setStateOnView(false);
                new Handler().postDelayed(()->viewModel.postTime(0),1000);
            }
         };
         timer.start();
    }

    @Override
    public void onPause() {
        if (!isPausedGame && !isGameEnd && isSoundPlaying){
            mediaPlayer.pause();
        }
        if (timer != null)
            timer.cancel();
        isPausedGame = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        if (!isGameEnd && isSoundPlaying){
            mediaPlayer.start();
        }
        if (isGameStarted && isPausedGame){
            countTime(Constant.PLAY_TIME_UNTIL_END* 1000L);
        }
        isPausedGame = false;
        super.onResume();
    }
}
