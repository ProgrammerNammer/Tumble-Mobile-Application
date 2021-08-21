package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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


public class GameActivity extends AppCompatActivity {
    private final int DIMENSIONS = 4;
    private Button btn_new_game;
    private CountDownTimer cdt_timer;
    private ProgressBar pb_progressbar_1;
    private ProgressBar pb_progressbar_2;
    private TableLayout tl_game_grid;
    private TextView tv_word_formed;

    private ArrayList<ArrayList<LetterDie>> letterDiceGrid;
    private LetterDiceGenerator letterDiceGenerator;
    private ArrayList<LetterDieAdapter> letterDieAdapters;
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
        letterDieAdapters = new ArrayList<>();
        player = new Player();

        this.btn_new_game.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), GameActivity.class);
            finish();
            startActivity(i);
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

    @SuppressLint("ClickableViewAccessibility")
    private void generateGameBoard() {
        //  Set table's weight sum to properly scale its rows evenly
        tl_game_grid.setWeightSum(DIMENSIONS);

        //  First generate the letter dice
        letterDiceGrid = letterDiceGenerator.generateTileGrid(DIMENSIONS);

        //  Now, render the board's contents
        for (int row = 0; row < letterDiceGrid.size(); row++) {
            LetterDieAdapter letterDieAdapter = new LetterDieAdapter(this, letterDiceGrid.get(row));
            LinearLayout tableRow = new LinearLayout(this);
            letterDieAdapters.add(letterDieAdapter);

            tableRow.post(
                    () -> {
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        tableRow.setLayoutParams(layoutParams);
                        tableRow.setGravity(Gravity.CENTER);
                        tableRow.setWeightSum(DIMENSIONS);
                    }
            );

            for (int column = 0; column < letterDiceGrid.size(); column++) {
                View view = letterDieAdapter.getView(column, null, (ViewGroup) getWindow().getDecorView().getRootView());
                view.post(
                        () -> {
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                            layoutParams.setMargins(30, 30, 30, 30);

                            view.setLayoutParams(layoutParams);
                        }
                );

                tableRow.addView(view);
            }

            tl_game_grid.addView(tableRow);
        }

        tl_game_grid.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    handleDiceSelect((int) event.getRawX(), (int) event.getRawY());
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL: {
                    handleDiceDeselect();
                }
                break;
                default: {
                    Log.d("MyTag", "" + event.getAction());
                }
            }

            return true;
        });
    }

    private void handleDiceDeselect() {
        //  TODO: Connect w/ API & Scoring System
        //  example: if (validWord() && player.isUniqueValidWord) {player.addValidWord()}

        tv_word_formed.setText("");
        player.clearDiceCurrentlySelected();
    }

    public void handleDiceSelect(final int RAW_X, final int RAW_Y) {
        for (int i = 0; i < tl_game_grid.getChildCount(); i++) {
            if (isPressingOverThisView(tl_game_grid.getChildAt(i), RAW_X, RAW_Y)) {
                LinearLayout rowChildren = (LinearLayout) tl_game_grid.getChildAt(i);

                for (int j = 0; j < rowChildren.getChildCount(); j++) {
                    if (isPressingOverThisView(rowChildren.getChildAt(j).findViewById(R.id.iv_hitbox), RAW_X, RAW_Y)) {
                        LetterDie letterDie = letterDiceGrid.get(i).get(j);
                        String stringLetterTile = Character.toString(letterDie.getMyLetter());
                        String currentWordText = tv_word_formed.getText().toString();

                        if (player.isEmptyDiceCurrentlySelected() ||
                                (!player.isThisTileAlreadySelected(letterDie)
                                        && player.peekDiceCurrentlySelected().isThisTileMyNeighbor(letterDie))) {
                            currentWordText += stringLetterTile;
                            tv_word_formed.setText(currentWordText);

                            player.pushDiceCurrentlySelected(letterDie);
                        }

                        //  TODO: Connect to custom adapter
                        // letterDiceGrid.get(i).get(j).setFocusedOn(true);
                        // letterDieAdapters.get(i).notifyDataSetChanged();
                        break;
                    }

                }

                break;
            }
        }
    }

    private boolean isPressingOverThisView(View view, int rawX, int rawY) {
        final int AREA_DECREASE = 100;
        Rect rect = new Rect();
        int[] location = new int[2];

        view.getDrawingRect(rect);

//        rect.top -= AREA_DECREASE;
//        rect.left -= AREA_DECREASE;
//        rect.bottom -= AREA_DECREASE;
//        rect.right -= AREA_DECREASE;

        view.getLocationOnScreen(location);
        rect.offset(location[0], location[1]);
        return rect.contains(rawX, rawY);
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
