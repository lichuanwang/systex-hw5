package com.systex.hw4.model;


public class GuessGame {
    private int remains;
    private int luckyNumber;

    public GuessGame(int range, int remains) {
        this.luckyNumber = (int) Math.ceil(Math.random() * range);
        this.remains = remains;
    }

    public boolean guess(int number) {
        this.remains--;
        return number == luckyNumber;
    }

    public int getRemains() {
        return this.remains;
    }

    public int getLuckyNumber() {
        return this.luckyNumber;
    }
}
