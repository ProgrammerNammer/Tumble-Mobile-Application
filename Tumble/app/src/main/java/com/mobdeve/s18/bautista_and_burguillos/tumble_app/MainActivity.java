package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        exitButton = findViewById(R.id.exitButton);

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

                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(view.getContext(), LoginActivity.class);
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                //startActivity(i);
                startActivity( i );
                finishAndRemoveTask();
            }
        });

    }
}
