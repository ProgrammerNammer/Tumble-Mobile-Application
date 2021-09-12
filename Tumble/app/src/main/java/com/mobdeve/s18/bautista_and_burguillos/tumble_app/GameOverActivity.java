package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private TextView tv_final_score;
    private Button btn_new_game;
    private Button btn_game_over_exit_game_activity;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        tv_final_score = findViewById(R.id.tv_final_score);
        btn_new_game = findViewById(R.id.btn_game_over_new_game);
        btn_game_over_exit_game_activity = findViewById(R.id.btn_game_over_exit_game_activity);

        int finalScore = getIntent().getIntExtra(getResources().getString(R.string.key_final_score), -1);
        tv_final_score.setText(Integer.toString(finalScore));

        this.btn_new_game.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), GameActivity.class);

            finish();
            startActivity(i);
        });

        this.btn_game_over_exit_game_activity.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), MainActivity.class);

            finish();
            startActivity(i);
        });
    }
}
