package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Player {
    private final ArrayList<String> validWordsSubmitted;
    private final Stack<LetterDie> diceCurrentlySelected;
    private int score;
    private int powerup_prog;
    private boolean powerup_available;
    public Player() {
        this.score = 0;
        this.powerup_prog=0;
        validWordsSubmitted = new ArrayList<>();
        diceCurrentlySelected = new Stack<>();
    }

    public boolean isUniqueValidWord(String validWord) {
        for (String word : validWordsSubmitted) {
            if (word.equalsIgnoreCase(validWord)) {
                return false;
            }
        }

        return true;
    }

    public void clearDiceCurrentlySelected() {
        diceCurrentlySelected.clear();
    }

    public void pushDiceCurrentlySelected(LetterDie letterDie) {
        diceCurrentlySelected.push(letterDie);
    }

    public LetterDie popDiceCurrentlySelected() {
        return diceCurrentlySelected.pop();
    }

    public LetterDie peekDiceCurrentlySelected() {
        try {
            return diceCurrentlySelected.peek();
        } catch (EmptyStackException error) {
            return null;
        }
    }

    public boolean isEmptyDiceCurrentlySelected() {
        return diceCurrentlySelected.isEmpty();
    }

    public boolean isThisTileAlreadySelected(LetterDie letterDie) {
        return diceCurrentlySelected.isEmpty() || diceCurrentlySelected.contains(letterDie);
    }


    public void addValidWordSubmitted(String validWord) {
        this.validWordsSubmitted.add(validWord);
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

    public String getScoreString() {
        return Integer.toString(score);
    }

    public int getPowerup_prog(){return powerup_prog;}

    public void setPowerup_prog(int p){
        powerup_prog += p;
        if (powerup_prog > 500  ){
            setPowerupAvail(true);
            powerup_prog -= 500;
        }

    }
    public void setPowerupAvail(boolean b){
        powerup_available = b;
    }
    public boolean getPowerupAvail(){
        return powerup_available;
    }
}
