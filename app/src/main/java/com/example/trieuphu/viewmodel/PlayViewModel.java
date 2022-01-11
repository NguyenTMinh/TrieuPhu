package com.example.trieuphu.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trieuphu.model.Level;
import com.example.trieuphu.model.Player;
import com.example.trieuphu.model.Question;
import com.example.trieuphu.util.Constant;
import com.example.trieuphu.util.DataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayViewModel extends ViewModel {
    private MutableLiveData<Question> questionMutableLiveData;
    private MutableLiveData<Integer> timeMutableLiveData;
    private MutableLiveData<Integer> playerMoneyLiveData;
    private List<List<Question>> listQuestions;
    private List<Level> levels;
    private List<Level> listClone;
    private List<Player> players;
    private int playerCurrentAnswer;
    private int mediaCurrentPlaying;
    private int currentLevel = 0;
    private boolean[] help;
    private DataRepository repository;

    public PlayViewModel(){
        playerMoneyLiveData = new MutableLiveData<>();
        questionMutableLiveData = new MutableLiveData<>();
        timeMutableLiveData = new MutableLiveData<>();
        help = new boolean[4];
    }

    public int getMediaCurrentPlaying() {
        return mediaCurrentPlaying;
    }

    public void setMediaCurrentPlaying(int mediaCurrentPlaying) {
        this.mediaCurrentPlaying = mediaCurrentPlaying;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerCurrentAnswer() {
        return playerCurrentAnswer;
    }

    public void setPlayerCurrentAnswer(int playerCurrentAnswer) {
        int answer_pos = 0;
        for (int i = 0; i < Constant.ANSWER_ID.length; i++) {
            if(playerCurrentAnswer==Constant.ANSWER_ID[i]){
                answer_pos = i;
                break;
            }
        }
        this.playerCurrentAnswer = answer_pos;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void nextLevel() {
        this.currentLevel += 1;
    }

    public LiveData<Integer> getTimeLiveData(){
        return timeMutableLiveData;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void postTime(int time){
        timeMutableLiveData.postValue(time);
    }

    public LiveData<Question> getQuestionLiveData(){
        return questionMutableLiveData;
    }

    public void postQuestionValue(int level){
        int rand = new Random().nextInt(listQuestions.get(level).size());
        Question question = listQuestions.get(level).get(rand);
        questionMutableLiveData.postValue(question);
    }

    public LiveData<Integer> getPlayerMoneyLiveData(){
        return playerMoneyLiveData;
    }

    public void postPlayerMoneyValue(int money){
        playerMoneyLiveData.postValue(money);
    }

    public void setHelpUsed(int pos){
        help[pos] = true;
    }

    public boolean[] getHelp() {
        return help;
    }

    public List<Level> getReverseLevels(){
        return listClone;
    }

    public void setContext(Context context) {
        repository = DataRepository.getInstance(context);
        levels = repository.getLevelList();
        listClone = repository.getReverseLevelList();
        listQuestions = repository.getQuestionList();
        players = repository.getPlayerList();
    }

    public boolean checkAnswer(int answer){
        return answer == questionMutableLiveData.getValue().getTrueCase();
    }

    public void increaseMoney(){
        int money = getPlayerMoneyLiveData().getValue() + getLevels().get(getCurrentLevel()).getPriceAsNum();
        postPlayerMoneyValue(money);
    }

    public void saveFinishGame(String playerName){
        Player player = new Player(playerName,playerMoneyLiveData.getValue());
        if (players.size() <= 10){
            if(players.size() == 10){
                if(player.getScore() >= players.get(9).getScore()){
                    players.set(9,player);
                }
            }else {
                players.add(player);
            }
            for (int i = players.size()-1; i > 0 ; i--) {
                if (players.get(i).getScore() >= players.get(i-1).getScore()){
                    Player temp = players.get(i);
                    players.set(i,players.get(i-1));
                    players.set(i-1,temp);
                }else {
                    break;
                }
            }
        }
    }

    public boolean checkNameValid(String name){
        return !name.equals("") && !name.contains("_");
    }

    public int getAnswerRemainAfter5050(){
        int rand = new Random().nextInt(4);
        while (rand == getQuestionLiveData().getValue().getTrueCase()){
            rand = new Random().nextInt(4);
        }
        return rand;
    }

    public int[] getAudienceChoice(){
        int[] arr = new int[4];
        int x = getQuestionLiveData().getValue().getTrueCase();
        int count = 3;
        int rand = new Random().nextInt(40);
        arr[x] = 30 + rand;
        int percent = 100 - arr[x];
        for (int i = 0; i < 4; i++) {
            if (i != x){
                if (count == 1){
                    arr[i] = percent;
                    break;
                }else {
                    arr[i] = new Random().nextInt(percent);
                    percent = percent - arr[i];
                    count--;
                }
            }
        }
        return arr;
    }

    public String parseTrueCaseToText(){
        return Constant.LABEL_ANSWER[getQuestionLiveData().getValue().getTrueCase()];
    }
}
