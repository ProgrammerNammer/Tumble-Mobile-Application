package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class LetterDiceGenerator {
    private ArrayList<Object[]> LetterDieConfigurations = new ArrayList<>();
    private boolean hasSuccessfullyLoadedCSVFile;

    public LetterDiceGenerator(final Context context){
        loadConfigurationFile(context);
    }

    private void loadConfigurationFile(final Context context) {
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(context.getResources().openRawResource(R.raw.dice_configuration)));
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                ArrayList<Character> letterRange = new ArrayList<>();

                for (String letter : nextLine) {
                    letterRange.add(letter.charAt(0));
                }

                Object[] configuration = new Object[2];
                configuration[0] = letterRange;
                configuration[1] = false;
                 LetterDieConfigurations.add(configuration);
            }

            this.hasSuccessfullyLoadedCSVFile = true;
        } catch (Exception e) {
            Log.e("[Issue]", "Letter configuration CSV file not found! Using built-in RNG Function.");

            this.hasSuccessfullyLoadedCSVFile = false;
        }
    }

    private char generateLetter(final int DIMENSIONS) {
        Random random = new Random();

        if (hasSuccessfullyLoadedCSVFile) {

            for (int i = 0; i < DIMENSIONS; i++) {
                boolean hasSelectedSomething = false;

                Object[] selectedRollConfiguration = LetterDieConfigurations.get(random.nextInt(DIMENSIONS + 1));
                ArrayList<Character> letterRange = (ArrayList<Character>) selectedRollConfiguration[0];
                Boolean hasBeenSelected = (Boolean) selectedRollConfiguration[1];

                while (!hasSelectedSomething) {
                    if (!hasBeenSelected) {
                        char letterSelected = letterRange.get(random.nextInt(letterRange.size()));

                        hasBeenSelected = true;
                        hasSelectedSomething = true;

                        return letterSelected;
                    }
                }
            }
        } else {
            return (char) (random.nextInt(26) + 'A');
        }

        return 'X';
    }

    public ArrayList<ArrayList<LetterDie>> generateTileGrid(final int DIMENSTIONS) {
        ArrayList<ArrayList<LetterDie>> letterDiceGrid = new ArrayList<>();

        for (int row = 0; row < DIMENSTIONS; row++) {
            ArrayList<LetterDie> letterDiceRow = new ArrayList<>();

            for (int column = 0; column < DIMENSTIONS; column++) {
                letterDiceRow.add(new LetterDie(row, column, this.generateLetter(DIMENSTIONS)));
            }

            letterDiceGrid.add(letterDiceRow);
        }

        return letterDiceGrid;
    }
}
