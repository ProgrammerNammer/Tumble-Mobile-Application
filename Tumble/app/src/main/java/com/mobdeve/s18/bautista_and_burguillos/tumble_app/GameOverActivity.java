package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private TextView tv_final_score;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        tv_final_score = findViewById(R.id.tv_final_score);

        int finalScore = getIntent().getIntExtra(getResources().getString(R.string.key_final_score), -1);
        tv_final_score.setText(Integer.toString(finalScore));
    }
}
