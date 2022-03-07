package com.example.notesapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "NotesAdapter";

    private final List<Notes> notesList;
    private final MainActivity mainAct;

    NotesAdapter(List<Notes> noteList , MainActivity ma){
        this.notesList = noteList;
        this.mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Notes " + position);

        Notes employee = notesList.get(position);

        holder.title.setText(employee.getTitle());
        Date currentDate = new Date(Long.parseLong(employee.getTime()));
        holder.time.setText(currentDate.toString());
        holder.description.setText(employee.getDescription());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
