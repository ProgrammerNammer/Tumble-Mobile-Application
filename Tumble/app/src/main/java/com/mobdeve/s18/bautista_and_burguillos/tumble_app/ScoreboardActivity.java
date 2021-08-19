package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button main_menu;
    private ArrayList<ScoreboardItem> scoreboardItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        initBoard();
        main_menu = findViewById(R.id.appCompatButton3);
        recyclerView = findViewById(R.id.recycler_area);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initBoard();
        viewAdapter = new ViewAdapter(scoreboardItems);
        recyclerView.setAdapter(viewAdapter);
        this.main_menu.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view){
                finish();
            }
        });
    }
/*
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
*/
    private void initBoard(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference Ref = rootRef.collection("highscores");
        Query query = Ref.orderBy("score", Query.Direction.DESCENDING).limit(10);

               query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                   @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            scoreboardItems.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                ScoreboardItem item = new ScoreboardItem(document.get("username").toString(),document.get("score").toString());
                                scoreboardItems.add(item);
                                Log.d("TAG", item.getScore() + "kek");

                            }
                            viewAdapter.notifyDataSetChanged();


                    }
                });



    }
}
