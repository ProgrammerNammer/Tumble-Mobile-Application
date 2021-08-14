package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<ScoreboardItem> scoreList;

    public ViewAdapter(ArrayList<ScoreboardItem> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.best_score_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setTv_score(scoreList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}
