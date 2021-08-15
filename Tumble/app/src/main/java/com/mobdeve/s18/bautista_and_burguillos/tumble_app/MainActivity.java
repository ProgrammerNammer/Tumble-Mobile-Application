package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button new_game;
    private Button my_leaderboard;
    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new_game = findViewById(R.id.appCompatButton3);
        my_leaderboard = findViewById(R.id.appCompatButton4);
        exit = findViewById(R.id.exitButton);
        this.new_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                Intent i = new Intent(view.getContext(),GameActivity.class);
                startActivity(i);
            }
        });
        this.my_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                Intent i = new Intent(view.getContext(),ScoreboardActivity.class);
                startActivity(i);
            }
        });
        this.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                finishAndRemoveTask();
            }
        });

    }
}
