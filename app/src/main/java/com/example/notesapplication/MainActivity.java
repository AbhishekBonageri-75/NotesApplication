package com.example.notesapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final List<Notes> NotesList = new ArrayList<>();  // Main content is here
    public static  final String EXTRA_TITLE = "com.example.notesapplication.EXTRA_TITLE";
    public static  final String EXTRA_DESC = "com.example.notesapplication.EXTRA_DESC";
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView; // Layout's recyclerview
    NotesAdapter mAdapter = new NotesAdapter(NotesList, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        //NotesAdapter mAdapter = new NotesAdapter(NotesList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addItemsFromJSON();
        titleChage();
    }
    public void titleChage(){
        this.setTitle("Android Notes"+"("+NotesList.size()+")");
    }
    public void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i=0; i<jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String name = itemObj.getString("name");
                String description = itemObj.getString("description");
                String time = itemObj.getString("time");

                Notes notes = new Notes(name, description,time);
                NotesList.add(notes);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }
    }

    public String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }



    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes m = NotesList.get(pos);
        Intent intent = new Intent(MainActivity.this,EditActivity.class);
        intent.putExtra(EXTRA_TITLE,m.getTitle());
        intent.putExtra(EXTRA_DESC,m.getDescription());
        startActivity(intent);
//        Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes m = NotesList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);



        // lambda can be used here (as is below)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAdapter.notifyItemRemoved(pos);
                NotesList.remove(pos);
                titleChage();
                JSONArray jsonArray = new JSONArray(NotesList);
                try {
                    FileOutputStream fos = getApplicationContext().
                            openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

                    PrintWriter printWriter = new PrintWriter(fos);
                    printWriter.print(NotesList);
                    printWriter.close();
                    fos.close();

                    Log.d(TAG, "saveNotes: JSON:\n" + NotesList.toString());

                    //Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getStackTrace();
                }

            }
        });
        // lambda can be used here (as is below)
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "Notes Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Sure you want to Delete?");
        builder.setTitle("Delete Note ?");

        AlertDialog dialog = builder.create();
        dialog.show();
//        Toast.makeText(v.getContext(), "LONG " + m.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.new_note){
            Intent intent = new Intent(MainActivity.this , EditActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.about){
            Intent intent = new Intent( MainActivity.this , AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            Toast.makeText(this, "No Such Option", Toast.LENGTH_SHORT).show();
            return true;
        }

    }
}