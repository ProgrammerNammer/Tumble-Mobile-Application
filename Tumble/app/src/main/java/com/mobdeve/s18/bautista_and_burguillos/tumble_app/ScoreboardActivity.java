package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ScoreboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScoreboardViewAdapter scoreboardViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button main_menu;
    private ArrayList<ScoreboardItem> scoreboardItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        main_menu = findViewById(R.id.appCompatButton3);
        recyclerView = findViewById(R.id.recycler_area);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        initBoard();
        scoreboardViewAdapter = new ScoreboardViewAdapter(scoreboardItems);
        recyclerView.setAdapter(scoreboardViewAdapter);

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                finish();
            }
        });
    }

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
                scoreboardViewAdapter.notifyDataSetChanged();


            }
        });



    }
}
