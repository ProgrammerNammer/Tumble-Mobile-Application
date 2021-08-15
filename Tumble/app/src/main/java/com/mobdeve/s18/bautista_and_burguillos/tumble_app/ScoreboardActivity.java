package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ScoreboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button main_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        main_menu = findViewById(R.id.appCompatButton3);
        recyclerView = findViewById(R.id.recycler_area);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ScoreboardItem> scoreboardItems = getScoreboard();

        viewAdapter = new ViewAdapter(scoreboardItems);
        recyclerView.setAdapter(viewAdapter);
        this.main_menu.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                finish();
            }
        });
    }

    private ArrayList<ScoreboardItem> getScoreboard() {
        //  TODO: Get from Database. Below is just a sample. MAXIMUM of 10 ITEMS
        Integer[] sampleScores = {199, 2314, 21351, 1231, 128491, 123, 1515, 99999, 10, 111, 313124,1141321,2412,41251,5,15};

        ArrayList<ScoreboardItem> scoreboardItems = new ArrayList<>();

        //  Sort the arrayList first to maintain arrangement integrity
        Arrays.sort(sampleScores, Collections.reverseOrder());

        for (int score : sampleScores) {
            scoreboardItems.add(new ScoreboardItem(score));
        }

        return scoreboardItems;
    }
}
