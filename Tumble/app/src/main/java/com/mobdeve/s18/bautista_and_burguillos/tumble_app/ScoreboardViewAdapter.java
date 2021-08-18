package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreboardViewAdapter extends RecyclerView.Adapter<ScoreboardViewHolder> {
    private ArrayList<ScoreboardItem> scoreList;

    public ScoreboardViewAdapter(ArrayList<ScoreboardItem> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public ScoreboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.best_score_item, parent, false);

        ScoreboardViewHolder scoreboardViewHolder = new ScoreboardViewHolder(view);
        return scoreboardViewHolder;
    }

    @Override
    public void onBindViewHolder(ScoreboardViewHolder scoreboardViewHolder, final int position) {
        scoreboardViewHolder.setTv_score(scoreList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}
