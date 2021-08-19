package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class LetterDieAdapter extends ArrayAdapter<LetterDie> {
    private final ArrayList<LetterDie> letterDieRow;

    public interface IHandleTouchEvent {
        boolean handleTileClick(int row, int column, char letterTile);
    }

    private final IHandleTouchEvent handleCallBack;

    public LetterDieAdapter (Context context, ArrayList<LetterDie> letterDieRow, IHandleTouchEvent handleCallBack) {
        super(context, 0, letterDieRow);

        this.handleCallBack = handleCallBack;
        this.letterDieRow = letterDieRow;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LetterDie letterDie = letterDieRow.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.letter_die_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tv_letter = convertView.findViewById(R.id.tv_die_letter);
            viewHolder.tv_letter.setText(Character.toString(letterDie.getMyLetter()));

            //  TODO: Hover/Drag selection
            viewHolder.cl_letter_tile = convertView.findViewById(R.id.cl_letter_tile);
            viewHolder.cl_letter_tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    letterDie.toggleIsFocusedOn();

                    if (letterDie.isFocusedOn()) {
                        viewHolder.cl_letter_tile.setBackgroundResource(R.drawable.letter_die_activated);
                    } else {
                        viewHolder.cl_letter_tile.setBackgroundResource(R.drawable.letter_die);
                    }

                    handleCallBack.handleTileClick(letterDie.getRow(), letterDie.getColumn(), letterDie.getMyLetter());
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_letter;
        ConstraintLayout cl_letter_tile;
    }
}
