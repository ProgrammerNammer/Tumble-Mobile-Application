package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Button new_game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        new_game = findViewById(R.id.AppCompatButton);
        this.new_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                Intent i = new Intent(view.getContext(),GameActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
}
