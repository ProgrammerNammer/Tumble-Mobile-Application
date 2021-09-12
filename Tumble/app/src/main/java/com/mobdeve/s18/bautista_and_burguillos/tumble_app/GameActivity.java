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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class GameActivity extends AppCompatActivity {
    private final int DIMENSIONS = 4;
    private Button btn_new_game;
    private Button btn_exit_game_activity;
    private CountDownTimer cdt_timer;
    private ProgressBar pb_progressbar_1;
    private ProgressBar pb_progressbar_2;
    private TableLayout tl_game_grid;
    private TextView tv_score_formed;
    private TextView tv_total_score;
    private TextView tv_word_formed;

    private ArrayList<View> selectedLetterDice;
    private ArrayList<ArrayList<LetterDie>> letterDiceGrid;
    private LetterDiceGenerator letterDiceGenerator;
    private Map<String, Boolean> memoizeWordResults;
    private Player player;
    private ScoreSystem scoreSystem;
    private int progress;
    private CountDownTimer fadeAnimation;

    //  TODO: Cleanup, progress bar still going even after finish
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initLayout();
        initTimer();
        generateGameBoard();

         cdt_timer.start();
    }

    private void initLayout() {
        btn_new_game = findViewById(R.id.btn_new_game);
        btn_exit_game_activity = findViewById(R.id.btn_exit_game_activity);
        tv_score_formed = findViewById(R.id.tv_score_formed);
        tv_total_score = findViewById(R.id.tv_total_score);
        tv_word_formed = findViewById(R.id.tv_word_formed);
        tl_game_grid = findViewById(R.id.tl_game_grid);

        letterDiceGenerator = new LetterDiceGenerator(this);
        letterDiceGrid = new ArrayList<>();
        memoizeWordResults = new HashMap<>();
        player = new Player();
        selectedLetterDice = new ArrayList<>();
        scoreSystem = new ScoreSystem(DIMENSIONS);

        this.btn_new_game.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), GameActivity.class);

            cdt_timer.cancel();
            
            finish();
            startActivity(i);
        });

        this.btn_exit_game_activity.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), MainActivity.class);

            cdt_timer.cancel();

            finish();
            startActivity(i);
        });

        //   Setting default value
        tv_score_formed.setText("");
        tv_total_score.setText(player.getScoreString());
        tv_word_formed.setText("");
    }

    private void initTimer() {
        progress = 0;

        this.pb_progressbar_1 = (ProgressBar) findViewById(R.id.progressbar);
        this.pb_progressbar_2 = (ProgressBar) findViewById(R.id.progressbar2);
        this.pb_progressbar_1.setProgress(progress);
        this.pb_progressbar_2.setProgress(progress);

        Intent i = new Intent(this, GameOverActivity.class);

        //  Load the custom progress bar
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.custom_timer);
        pb_progressbar_1.setProgressDrawable(drawable);

        int GAME_TIME_MILLISECONDS = 10000;

        cdt_timer = new CountDownTimer(GAME_TIME_MILLISECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Time", "Tick of Progress"+ i+ millisUntilFinished);
                progress++;
                pb_progressbar_1.setProgress((int) progress * 100 / (GAME_TIME_MILLISECONDS / 1000));
                pb_progressbar_2.setProgress((int) progress * 100 / (GAME_TIME_MILLISECONDS / 1000));
            }

            @Override
            public void onFinish() {
                progress++;
                pb_progressbar_1.setProgress(100);
                pb_progressbar_2.setProgress(100);

                i.putExtra(getResources().getString(R.string.key_final_score), player.getScore());

                startActivity(i);
                finish();
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
            LetterDieAdapter letterDieAdapter = new LetterDieAdapter(this, R.layout.letter_die_item, letterDiceGrid.get(row));
            LinearLayout tableRow = new LinearLayout(this);

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
                view.setVisibility(View.INVISIBLE);
                view.post(
                        () -> {
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                            layoutParams.setMargins(30, 30, 30, 30);
                            view.setLayoutParams(layoutParams);
                            view.setVisibility(View.VISIBLE);
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
                    if (fadeAnimation != null) {
                        tv_word_formed.setText("");
                        tv_score_formed.setText("");
                        fadeAnimation.cancel();
                        fadeAnimation = null;
                    }

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
        String WORD_FORMED = tv_word_formed.getText().toString();

        tv_score_formed.setVisibility(View.VISIBLE);

        for (View letterDie : selectedLetterDice) {
            letterDie.setBackground(getDrawable(R.drawable.letter_die));
        }

        selectedLetterDice.clear();

        if (WORD_FORMED.length() <= 2) {
            tv_score_formed.setText(getResources().getString(R.string.score_status_too_short));
        } else if (!player.isUniqueValidWord(WORD_FORMED)) {
            tv_score_formed.setText(getResources().getString(R.string.score_status_already_submitted));
        } else if (isValidWord(WORD_FORMED)) {
            //  Only valid option
            final int SCORE = scoreSystem.convertToScore(WORD_FORMED);
            final String SCORE_STRING = Integer.toString(SCORE);

            player.addValidWordSubmitted(WORD_FORMED);
            player.addScore(SCORE);

            tv_total_score.setText(player.getScoreString());
            tv_score_formed.setText("+ " + SCORE_STRING);

        } else {
            tv_score_formed.setText(getResources().getString(R.string.score_status_not_a_word));
        }

        fadeAnimation = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                tv_word_formed.setText("");
                tv_score_formed.setText("");
            }
        }.start();

        player.clearDiceCurrentlySelected();
    }

    private boolean isValidWord(String wordFormed) {
        CallBackTask task = new CallBackTask();

        try {
            boolean result = false;
            if (memoizeWordResults.containsKey(wordFormed)) {
                result = memoizeWordResults.get(wordFormed);
            } else {
                result = !task.execute(inflections(wordFormed)).get().equals("404");
                memoizeWordResults.put(wordFormed, result);
            }

            return result;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void handleDiceSelect(final int RAW_X, final int RAW_Y) {
        for (int row = 0; row < tl_game_grid.getChildCount(); row++) {
            if (isPressingOverThisView(tl_game_grid.getChildAt(row), RAW_X, RAW_Y)) {
                LinearLayout rowChildren = (LinearLayout) tl_game_grid.getChildAt(row);

                for (int column = 0; column < rowChildren.getChildCount(); column++) {
                    if (isPressingOverThisView(rowChildren.getChildAt(column).findViewById(R.id.iv_hitbox), RAW_X, RAW_Y) ||
                            (player.peekDiceCurrentlySelected() == null && isPressingOverThisView(rowChildren.getChildAt(column).findViewById(R.id.cl_letter_tile), RAW_X, RAW_Y))) {
                        LetterDie letterDie = letterDiceGrid.get(row).get(column);
                        String stringLetterTile = Character.toString(letterDie.getMyLetter());
                        String currentWordText = tv_word_formed.getText().toString();

                        if (player.isEmptyDiceCurrentlySelected() ||
                                (!player.isThisTileAlreadySelected(letterDie)
                                        && player.peekDiceCurrentlySelected().isThisTileMyNeighbor(letterDie))) {
                            currentWordText += stringLetterTile;
                            tv_word_formed.setText(currentWordText);

                            player.pushDiceCurrentlySelected(letterDie);

                            View sample = rowChildren.getChildAt(column);
                            sample.setBackground(getDrawable(R.drawable.letter_die_activated));
                            selectedLetterDice.add(sample);
                        }

                        break;
                    }

                }

                break;
            }
        }
    }

    private boolean isPressingOverThisView(View view, int rawX, int rawY) {
        Rect rect = new Rect();
        int[] location = new int[2];

        view.getDrawingRect(rect);

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

    private void gameOver(String score) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        user.put("score", score);
        db.collection("highscores")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }
}
