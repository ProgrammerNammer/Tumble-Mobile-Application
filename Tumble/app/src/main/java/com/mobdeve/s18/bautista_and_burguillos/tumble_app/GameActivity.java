package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class GameActivity extends AppCompatActivity implements LetterDieAdapter.IHandleTouchEvent {
    private final int DIMENSIONS = 4;
    private Button btn_new_game;
    private CountDownTimer cdt_timer;
    private ProgressBar pb_progressbar_1;
    private ProgressBar pb_progressbar_2;
    private TableLayout tl_game_grid;
    private TextView tv_word_formed;

    private ArrayList<ArrayList<LetterDie>> letterDiceGrid;
    private LetterDiceGenerator letterDiceGenerator;
    private Player player;
    private int progress;

    //  TODO: Cleanup, progress bar still going even after finish
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
        initTimer();
        generateGameBoard();

        cdt_timer.start();
    }

    private void init() {
        btn_new_game = findViewById(R.id.AppCompatButton);
        tv_word_formed = (TextView) findViewById(R.id.tv_word_formed);
        tl_game_grid = findViewById(R.id.tl_game_grid);

        letterDiceGenerator = new LetterDiceGenerator(this);
        letterDiceGrid = new ArrayList<>();
        player = new Player();

        this.btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GameActivity.class);
                finish();
                startActivity(i);
            }
        });

        //   Clearing default value
        tv_word_formed.setText("");
    }

    private void initTimer() {
        progress = 0;

        pb_progressbar_1 = (ProgressBar) findViewById(R.id.progressbar);
        pb_progressbar_2 = (ProgressBar) findViewById(R.id.progressbar2);
        pb_progressbar_1.setProgress(progress);
        pb_progressbar_2.setProgress(progress);

        //  Load the custom progress bar
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.custom_timer);
        pb_progressbar_1.setProgressDrawable(drawable);

        cdt_timer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Time", "Tick of Progress"+ i+ millisUntilFinished);
                progress++;
                pb_progressbar_1.setProgress((int) progress * 100 / (180000 / 1000));
                pb_progressbar_2.setProgress((int) progress * 100 / (180000 / 1000));
            }

            @Override
            public void onFinish() {
                progress++;
                pb_progressbar_1.setProgress(100);
                pb_progressbar_2.setProgress(100);
            }
        };
    }

    private void generateGameBoard() {
        //  Set table's weight sum to properly scale its rows evenly
        tl_game_grid.setWeightSum(DIMENSIONS);

        //  First generate the letter dice
        letterDiceGrid = letterDiceGenerator.generateTileGrid(DIMENSIONS);

        //  Now, render the board's contents
        for (int row = 0; row < letterDiceGrid.size(); row++) {
            LetterDieAdapter letterDieAdapter = new LetterDieAdapter(this, letterDiceGrid.get(row), this);
            LinearLayout tableRow = new LinearLayout(this);

            tableRow.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                            tableRow.setLayoutParams(layoutParams);
                            tableRow.setGravity(Gravity.CENTER);
                            tableRow.setWeightSum(DIMENSIONS);
                        }
                    }
            );

            for (int column = 0; column < letterDiceGrid.size(); column++) {
                View view = letterDieAdapter.getView(column, null, (ViewGroup) getWindow().getDecorView().getRootView());
                view.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                                layoutParams.setMargins(30, 30, 30, 30);
                                view.setLayoutParams(layoutParams);
                            }
                        }
                );

                tableRow.addView(view);
            }

            tl_game_grid.addView(tableRow);
        }
    }

    //  TODO: Documentation
    //  Returns whether or not tiles are neighbors
    @Override
    public boolean handleTileClick(LetterDie letterDie, char letterTile) {
        String stringLetterTile = Character.toString(letterTile);
        String currentWordText = tv_word_formed.getText().toString();

        if (player.isThisTileAlreadySelected(letterDie)) {
            return false;
        }

        if (player.isEmptyDiceCurrentlySelected()) {
            currentWordText += stringLetterTile;
            tv_word_formed.setText(currentWordText);

            player.pushDiceCurrentlySelected(letterDie);

            return true;
        } else {
            LetterDie lastLetterDie = player.peekDiceCurrentlySelected();

            if (lastLetterDie.isThisTileMyNeighbor(letterDie)) {
                currentWordText += stringLetterTile;
                tv_word_formed.setText(currentWordText);

                player.pushDiceCurrentlySelected(letterDie);

                return true;
            } else {
                return false;
            }
        }
    }

    /*
    to check if a word is a word call  "new CallBackTask().execute(inflections(insertwordhere)) ";
    callback returns a string "404" if it didn't find the word in the dictionary but if it does find a word it prints to log a String
    in JSON format that contains the word, the word's definition, and info about the word;
    */
    private String inflections(String s) {
        final String url = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        final String word = s;
        return url + word;
    }

    private class CallBackTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "404";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("onPostExecute: " + result);
        }
    }
}
