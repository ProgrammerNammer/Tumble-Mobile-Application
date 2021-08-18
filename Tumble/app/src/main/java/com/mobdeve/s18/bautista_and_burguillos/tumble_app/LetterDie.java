package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.util.Log;
import android.view.View;

public class LetterDie {
    private final char myLetter;
    private boolean isFocusedOn;

    public LetterDie (char letter) {
        this.myLetter = letter;
        this.isFocusedOn = false;
    }

    public void toggleIsFocusedOn() {
        isFocusedOn = !isFocusedOn;
    }

    public void setFocusedOn(boolean newState) {
        this.isFocusedOn = newState;
    }

    public char getMyLetter() {
        return myLetter;
    }

    public boolean isFocusedOn() {
        return isFocusedOn;
    }
}
