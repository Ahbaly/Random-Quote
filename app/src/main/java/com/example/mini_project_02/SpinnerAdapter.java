package com.example.mini_project_02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mini_project_02.models.Color;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<Color> {
    public SpinnerAdapter(Context context, ArrayList<Color> algorithmList) {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout, parent, false);
        }

        TextView tvColorName = convertView.findViewById(R.id.tvColorName);
        TextView tvColorCode = convertView.findViewById(R.id.tvColorCode);

        Color currentItem = getItem(position);

        convertView.setBackgroundColor(android.graphics.Color.parseColor(currentItem.getCode()));


        if (currentItem != null) {
            tvColorName.setText(currentItem.getName());
            tvColorCode.setText(currentItem.getCode());
        }
        return convertView;
    }
}
