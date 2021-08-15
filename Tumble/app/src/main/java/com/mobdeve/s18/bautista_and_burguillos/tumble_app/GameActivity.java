package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Button new_game;
    private ProgressBar mProgressBar;
    private ProgressBar mProgressBar2;
    private CountDownTimer mCountDownTimer;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        new_game = findViewById(R.id.AppCompatButton);
        i = 0;
        this.new_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                Intent i = new Intent(view.getContext(),GameActivity.class);
                finish();
                startActivity(i);
            }
        });

        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);
        mProgressBar2=(ProgressBar)findViewById(R.id.progressbar2);
        mProgressBar.setProgress(i);
        mProgressBar2.setProgress(i);
        mCountDownTimer=new CountDownTimer(180000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressBar.setProgress((int)i*100/(180000/1000));
                mProgressBar2.setProgress((int)i*100/(180000/1000));
            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
                mProgressBar2.setProgress(100);
            }

        };

        mCountDownTimer.start();
    }
}
