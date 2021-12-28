package com.example.trieuphu.model;

import java.util.List;

public class Question {
    private int levelQuestion;
    private String question;
    private List<String> choice;
    private int trueCase;

    public Question(int levelQuestion,String question, List<String> choice, int trueCase) {
        this.levelQuestion = levelQuestion;
        this.question = question;
        this.choice = choice;
        this.trueCase = trueCase-1;
    }

    public int getLevelQuestion() {
        return levelQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    public int getTrueCase() {
        return trueCase;
    }

    public void setTrueCase(int trueCase) {
        this.trueCase = trueCase;
    }
}
