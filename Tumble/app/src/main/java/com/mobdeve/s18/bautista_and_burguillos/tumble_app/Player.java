package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import java.util.ArrayList;

public class Player {
    private int score;
    private ArrayList<String> validWordsSubmitted;

    public Player() {
        this.score = 0;
        validWordsSubmitted = new ArrayList<>();
    }

    public void addValidWordSubmitted(String validWord) {
        this.validWordsSubmitted.add(validWord);
    }

    public boolean isUniqueValidWord(String validWord) {
        //  TODO: Apply trie algorithm, this is just temporary

        for (String word : validWordsSubmitted) {
            if (word.equalsIgnoreCase(validWord)) {
                return false;
            }
        }

        return true;
    }

    public void addScore(int scoreToAdd) {
        this.score += scoreToAdd;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
