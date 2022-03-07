package com.example.notesapplication;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title , description , time;

    MyViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.title_row);
        description = view.findViewById(R.id.description_row);
        description.setMovementMethod(new ScrollingMovementMethod());
        time = view.findViewById(R.id.time_row);

    }

}
