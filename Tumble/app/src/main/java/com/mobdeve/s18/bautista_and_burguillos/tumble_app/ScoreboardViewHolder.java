package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreboardViewHolder extends RecyclerView.ViewHolder {
    private TextView tv_score;

    public ScoreboardViewHolder(View view) {
        super(view);

        this.tv_score = view.findViewById(R.id.tv_score);
    }

    public void setTv_score(int tv_score) {
        this.tv_score.setText(Integer.toString(tv_score));
    }
}
