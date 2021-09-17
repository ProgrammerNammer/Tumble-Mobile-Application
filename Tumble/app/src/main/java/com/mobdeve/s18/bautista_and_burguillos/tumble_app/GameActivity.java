package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private final double POWER_UP_THRESHOLD = 2500;
    private final int DIMENSIONS = 4;
    private final int ANIMATE_BOARD_MILLISECONDS = 2000;
    private final int FREEZE_SCREEN_MILLISECONDS = 4000;
    private final int GAME_TIME_MILLISECONDS = 180000;
    private final int POWER_UP_MILLISECONDS = 10000;
    private final int TUMBLE_FIRE_MILLISECONDS = 3000;
    private final int WORD_FADE_MILLISECONDS = 3000;
    private SensorManager mSensorManager;
    private LinearLayout ll_game_bottom;
    private LinearLayout ll_game_over;
    private LinearLayout ll_game_top;
    private LinearLayout ll_tumble_down;
    private ProgressBar pb_left_wing;
    private ProgressBar pb_right_wing;
    private ProgressBar pb_power_up;
    private RelativeLayout rl_game;
    private TableLayout tl_game_grid;
    private TextView tv_final_score;
    private TextView tv_score_formed;
    private TextView tv_total_score;
    private TextView tv_word_formed;
    private ArrayList<View> selectedLetterDice;
    private ArrayList<ArrayList<LetterDie>> letterDiceGrid;
    private Button btn_exit_game_activity;
    private Button btn_new_game;
    private CountDownTimer cdtTimer;
    private CountDownTimer cdtPowerUp;
    private CountDownTimer cdtTextFadeEffect;
    private LetterDiceGenerator letterDiceGenerator;
    private Map<String, Boolean> memoizeWordResults;
    private Player player;
    private SensorEventListener mSensorListener;
    private ScoreSystem scoreSystem;
    private boolean hasFinishedStartingAnimation = false;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int powerUpTimer;
    private int timer;

    //  TODO: Cleanup, progress bar still going even after finish
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        mAccel = 0.00f;

        init();
        initLayout();
        initTimer();
        generateGameBoard();

        cdtTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();

        try {
            cdtTextFadeEffect.cancel();
            cdtTextFadeEffect = null;
        } catch (Exception e) {
            Log.e("Error", "NPE");
        }

        try {
            cdtPowerUp.cancel();
            cdtPowerUp = null;
        } catch (Exception e) {
            Log.e("Error", "NPE");
        }

        try {
            cdtTimer.cancel();
            cdtTimer = null;
        } catch (Exception e) {
            Log.e("Error", "NPE");
        }
    }

    private void init() {
        letterDiceGenerator = new LetterDiceGenerator(this);
        letterDiceGrid = new ArrayList<>();
        memoizeWordResults = new HashMap<>();
        player = new Player((int) POWER_UP_THRESHOLD);
        selectedLetterDice = new ArrayList<>();
        scoreSystem = new ScoreSystem(DIMENSIONS);

        mSensorListener = new SensorEventListener() {

            public void onSensorChanged(SensorEvent se) {
                float x = se.values[0];
                float y = se.values[1];
                float z = se.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                if (mAccel > 1) {
                    if (player.getPowerUpAvailable()) {
                        hasFinishedStartingAnimation = false;
                        ll_game_top.setBackground(getDrawable(R.drawable.activity_game_top_background_power_up));

                        rl_game.setPadding(30, 30, 30, 30);
                        rl_game.setBackground(getDrawable(R.drawable.powerup_border));

                        ll_game_top.setPadding(20, 0, 20, 50);

                        ll_game_bottom.setPadding(20, 30, 20, 0);

                        tl_game_grid.removeAllViews();
                        tl_game_grid.setOnTouchListener((v, event) -> {
                            return false;
                        });

                        generateGameBoard();

                        scoreSystem.setScoreMultiplier(20);
                        cdtTimer.pause();

                        player.activatePowerUp();
                        updatePowerUpStatus();

                        ll_tumble_down.setVisibility(View.VISIBLE);
                        new CountDownTimer(TUMBLE_FIRE_MILLISECONDS, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                ll_tumble_down.setVisibility(View.GONE);

                                cdtPowerUp = new CountDownTimer(POWER_UP_MILLISECONDS, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        powerUpTimer++;
                                        pb_power_up.setProgress(powerUpTimer * 100 / (10000 / 1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        ll_game_top.setBackground(getDrawable(R.drawable.activity_game_top_background));

                                        rl_game.setPadding(0, 0, 0, 0);

                                        ll_game_top.setPadding(50, 30, 50, 50);

                                        ll_game_bottom.setPadding(50, 30, 50, 30);

                                        scoreSystem.setScoreMultiplier(5);
                                        cdtTimer.resume();

                                        player.setPowerUpActive(false);
                                        powerUpTimer = 0;

                                        pb_power_up.setProgress(100);
                                    }
                                }.start();
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    private void initLayout() {
        btn_new_game = findViewById(R.id.btn_new_game);
        btn_exit_game_activity = findViewById(R.id.btn_exit_game_activity);
        ll_game_bottom = findViewById(R.id.ll_game_bottom);
        ll_game_over = findViewById(R.id.ll_game_over);
        ll_game_top = findViewById(R.id.ll_game_top);
        ll_tumble_down = findViewById(R.id.ll_tumble_down);
        pb_left_wing = findViewById(R.id.pb_left_wing);
        pb_right_wing = findViewById(R.id.pb_right_wing);
        pb_power_up = findViewById(R.id.pb_power_up);
        rl_game = findViewById(R.id.rl_game);
        tv_final_score = findViewById(R.id.tv_final_score);
        tv_score_formed = findViewById(R.id.tv_score_formed);
        tv_total_score = findViewById(R.id.tv_total_score);
        tv_word_formed = findViewById(R.id.tv_word_formed);
        tl_game_grid = findViewById(R.id.tl_game_grid);

        btn_new_game.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), GameActivity.class);

            finish();
            startActivity(i);
        });

        btn_exit_game_activity.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), MainActivity.class);

            finish();
            startActivity(i);
        });

        ll_tumble_down.setOnClickListener(view -> {
        });

        //   Setting default value
        tv_score_formed.setText("");
        tv_total_score.setText(player.getScoreString());
        tv_word_formed.setText("");
    }

    private void initTimer() {
        timer = 0;
        powerUpTimer = 0;

        this.pb_left_wing.setProgress(timer);
        this.pb_right_wing.setProgress(timer);
        updatePowerUpStatus();

        cdtTimer = new CountDownTimer(GAME_TIME_MILLISECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Time", "Tick of Progress"+ i+ millisUntilFinished);
                timer++;
                pb_left_wing.setProgress(timer * 100 / (GAME_TIME_MILLISECONDS / 1000));
                pb_right_wing.setProgress(timer * 100 / (GAME_TIME_MILLISECONDS / 1000));
            }

            @Override
            public void onFinish() {
                timer++;
                pb_left_wing.setProgress(100);
                pb_right_wing.setProgress(100);

                ll_game_over.setVisibility(View.VISIBLE);
                ll_game_over.setOnClickListener(view -> {
                });

                tv_final_score.setText(player.getScoreString());

                saveScoreToDB(Integer.toString(player.getScore()));

                new CountDownTimer(FREEZE_SCREEN_MILLISECONDS, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        ll_game_over.setOnClickListener(view -> {
                            Intent i = new Intent(view.getContext(), MainActivity.class);

                            finish();
                            startActivity(i);
                        });
                    }
                }.start();
            }
        };
    }

    private void updatePowerUpStatus() {
        double playerProgress = player.getPowerUpProgress() / POWER_UP_THRESHOLD;

        playerProgress = playerProgress > 1 ? 1 : playerProgress < 0 ? 0 : playerProgress;

        pb_power_up.setProgress((int) (100 - playerProgress * 100));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void generateGameBoard() {
        //  Set table's weight sum to properly scale its rows evenly
        tl_game_grid.setWeightSum(DIMENSIONS);
        //  First generate the letter dice
        letterDiceGrid = letterDiceGenerator.generateTileGrid(DIMENSIONS);

        TableRowSweepAnimation tableRowSweepAnimation = new TableRowSweepAnimation(this);
        tl_game_grid.addView(tableRowSweepAnimation);

        new CountDownTimer(ANIMATE_BOARD_MILLISECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!hasFinishedStartingAnimation) {
                    tl_game_grid.removeAllViews();
                    hasFinishedStartingAnimation = true;
                }

                //  Now, render the board's contents
                for (int row = 0; row < letterDiceGrid.size(); row++) {
                    LetterDieAdapter letterDieAdapter = new LetterDieAdapter(getBaseContext(), R.layout.letter_die_item, letterDiceGrid.get(row));
                    LinearLayout tableRow = new LinearLayout(getBaseContext());

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
                                    final int MARGIN = 30;

                                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                                    layoutParams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
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
                            if (cdtTextFadeEffect != null) {
                                tv_word_formed.setText("");
                                tv_score_formed.setText("");
                                cdtTextFadeEffect.cancel();
                                cdtTextFadeEffect = null;
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
        }.start();


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
            player.addPowerUpProgress(SCORE);
            tv_total_score.setText(player.getScoreString());
            tv_score_formed.setText("+ " + SCORE_STRING);
            updatePowerUpStatus();

        } else {
            tv_score_formed.setText(getResources().getString(R.string.score_status_not_a_word));
        }

        cdtTextFadeEffect = new CountDownTimer(WORD_FADE_MILLISECONDS, 1000) {
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

                            View letterDieInstance = rowChildren.getChildAt(column);
                            letterDieInstance.setBackground(getDrawable(R.drawable.letter_die_activated));
                            selectedLetterDice.add(letterDieInstance);
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

    private void saveScoreToDB(String score) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        user.put("score", Integer.valueOf(score));

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
