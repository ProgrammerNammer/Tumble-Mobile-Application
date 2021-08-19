package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

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
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;


public class GameActivity extends AppCompatActivity {
    private final int DIMENSIONS = 4;
    private Button btn_new_game;
    private ProgressBar pb_progressbar_1;
    private ProgressBar pb_progressbar_2;
    private CountDownTimer cdt_timer;
    private int i;

    private ArrayList<ArrayList<LetterDie>> letterDiceGrid;
    private TableLayout tl_game_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
        initGameBoard();
        initTimer();
        cdt_timer.start();
    }

    private void init() {
        btn_new_game = findViewById(R.id.AppCompatButton);

        this.btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                Intent i = new Intent(view.getContext(),GameActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    private void initTimer() {
        i = 0;

        pb_progressbar_1 =(ProgressBar)findViewById(R.id.progressbar);
        pb_progressbar_2 =(ProgressBar)findViewById(R.id.progressbar2);
        pb_progressbar_1.setProgress(i);
        pb_progressbar_2.setProgress(i);

        //  Load the custom progress bar
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.custom_timer);
        pb_progressbar_1.setProgressDrawable(drawable);

        cdt_timer =new CountDownTimer(180000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Time", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                pb_progressbar_1.setProgress((int)i*100/(180000/1000));
                pb_progressbar_2.setProgress((int)i*100/(180000/1000));
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                pb_progressbar_1.setProgress(100);
                pb_progressbar_2.setProgress(100);
            }

        };
    }

    private void initGameBoard() {
        //  Generate the board using RNG
        Random random = new Random();
        letterDiceGrid = new ArrayList<>();
        tl_game_grid = findViewById(R.id.tl_game_grid);

        //  Set table's weight sum to properly scale its rows evenly
        tl_game_grid.setWeightSum(DIMENSIONS);

        for (int row = 0; row < DIMENSIONS; row++) {
            ArrayList<LetterDie> letterDieRow = new ArrayList<>();

            for (int column = 0; column < DIMENSIONS; column++) {
                //  TODO: Loaded RNG, difficult letters should spawn less!
                char myDieLetter = (char)(random.nextInt(26) + 'A');
                letterDieRow.add(new LetterDie(myDieLetter));
            }

            letterDiceGrid.add(letterDieRow);
        }

        //  Now, render the board
        for (int row = 0; row < letterDiceGrid.size(); row++) {
            TableRow tableRow = new TableRow(this);

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
                LetterDie letterDie = letterDiceGrid.get(row).get(column);
                View view = getLayoutInflater().inflate(R.layout.letter_die_item, tableRow, false);
                TextView letter = view.findViewById(R.id.tv_die_letter);

                letter.setText(Character.toString(letterDie.getMyLetter()));

                //  Set the universal attributes of each letter die in the baord
                view.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                                layoutParams.setMargins(30, 30, 30, 30);
                                view.setLayoutParams(layoutParams);
                            }
                        }
                );

                tableRow.addView(view);
            }

            tl_game_grid.addView(tableRow, row);
        }
    }
    /*
    to check if a word is a word call  "new CallBackTask().execute(inflections(insertwordhere)) ";
    callback returns a string "404" if it didn't find the word in the dictionary but if it does find a word it prints to log a String
    in JSON format that contains the word, the word's definition, and info about the word;
    */

    private String inflections(String s){
        final String url = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        final String word = s;
        return url+word;
    }

    private class CallBackTask extends AsyncTask<String,Integer,String> {
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
    private void gameOver(String score){

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
