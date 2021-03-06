package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.util.Log;
import android.view.View;

public class LetterDie {
    private final char myLetter;
    private final int row;
    private final int column;

    public LetterDie(int row, int column, char letter) {
        this.row = row;
        this.column = column;
        this.myLetter = letter;
    }

    public boolean isThisTileMyNeighbor(LetterDie letterDie) {
        final int letterDieRow = letterDie.getRow();
        final int letterColumnRow = letterDie.getColumn();

        return (row == letterDieRow + 1 && column == letterColumnRow - 1) ||
                (row == letterDieRow + 1 && column == letterColumnRow) ||
                (row == letterDieRow + 1 && column == letterColumnRow + 1) ||
                (row == letterDieRow && column == letterColumnRow - 1) ||
                (row == letterDieRow && column == letterColumnRow) ||
                (row == letterDieRow && column == letterColumnRow + 1) ||
                (row == letterDieRow - 1 && column == letterColumnRow - 1) ||
                (row == letterDieRow - 1 && column == letterColumnRow) ||
                (row == letterDieRow - 1 && column == letterColumnRow + 1);
    }

    public char getMyLetter() {
        return myLetter;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
