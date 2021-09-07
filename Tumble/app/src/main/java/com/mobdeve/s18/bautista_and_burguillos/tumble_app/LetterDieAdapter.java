package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class LetterDieAdapter extends ArrayAdapter<LetterDie> {
    private Context myContext;
    private int resourceLayout;

    public LetterDieAdapter(Context context, int resourceLayout, List<LetterDie> letterDieRow) {
        super(context, resourceLayout, letterDieRow);
        this.resourceLayout = resourceLayout;
        this.myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(myContext);
            view = layoutInflater.inflate(resourceLayout, parent, false);
        }

        LetterDie letterDie = getItem(position);

        if (letterDie != null) {
            TextView tv_die_letter = view.findViewById(R.id.tv_die_letter);
            ConstraintLayout cl_letter_tile = view.findViewById(R.id.cl_letter_tile);

            tv_die_letter.setText(letterDie.getMyLetter());
            cl_letter_tile.setBackgroundResource(letterDie.isFocusedOn() ? R.drawable.letter_die_activated : R.drawable.letter_die);
        }

        return view;
    }
}
