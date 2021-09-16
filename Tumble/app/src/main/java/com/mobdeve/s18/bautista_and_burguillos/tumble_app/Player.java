package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Player {
    private final ArrayList<String> validWordsSubmitted;
    private final Stack<LetterDie> diceCurrentlySelected;
    private final int powerUpActivationThreshold;
    private int score;
    private int powerUpProgress;
    private boolean isPowerUpAvailable;

    public Player(int powerUpActivationThreshold) {
        this.score = 0;
        this.powerUpProgress = 0;
        this.powerUpActivationThreshold = powerUpActivationThreshold;
        this.validWordsSubmitted = new ArrayList<>();
        this.diceCurrentlySelected = new Stack<>();
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

    public int getPowerUpProgress() {
        return powerUpProgress;
    }

    public void addPowerUpProgress(int p) {
        this.powerUpProgress += p;
        if (powerUpProgress >= powerUpActivationThreshold) {
            setPowerUpAvailable(true);
        }
    }

    public void activatePowerUp() {
        setPowerUpAvailable(false);
        this.powerUpProgress = 0;
    }


    public void setPowerUpAvailable(boolean b) {
        isPowerUpAvailable = b;
    }

    public boolean getPowerUpAvailable() {
        return isPowerUpAvailable;
    }
}
