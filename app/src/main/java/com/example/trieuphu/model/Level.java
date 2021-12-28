package com.example.trieuphu.model;

import java.text.NumberFormat;

public class Level {
    private int level;
    private int price;

    public Level(int level, int price) {
        this.level = level;
        this.price = price;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPriceAsNum() {
        return price;
    }

    public String getPriceAsText(){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        StringBuilder string = new StringBuilder(format.format(price));
        string.deleteCharAt(string.length()-1);
        return string.toString();
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
