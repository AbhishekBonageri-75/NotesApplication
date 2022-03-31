package com.example.notesapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    EditText title , description;
    private final ArrayList<Notes> notesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.textViewTitle);
        description = findViewById(R.id.textViewDescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        String tit = intent.getStringExtra(MainActivity.EXTRA_TITLE);
        String desc = intent.getStringExtra(MainActivity.EXTRA_DESC);
        title.setText(tit);
        description.setText(desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.save_opt_menu,menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if (item.getItemId() == R.id.save){
//            if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
//                Notes n = new Notes(title.getText().toString(),
////                        String.valueOf(System.currentTimeMillis()) ,
//                        description.getText().toString() );
//                notesList.add(n);
////                Toast.makeText(this, "-- "+n.getTime(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,title.getText().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,description.getText().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,n.toString(),Toast.LENGTH_SHORT).show();
//
//            }
//            savejson();
//            Intent intent = new Intent(EditActivity.this , MainActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        else {
//            Toast.makeText(this, "No Such Option", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.save){

            if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                try {

                    String jsonDataString = readJSONDataFromFile();
                    JSONArray jsonArray = new JSONArray(jsonDataString);

                    for (int i=0; i<jsonArray.length(); ++i) {

                        JSONObject itemObj = jsonArray.getJSONObject(i);

                        String name = itemObj.getString("name");
                        String description = itemObj.getString("description");
                        String time = itemObj.getString("time");

                        Notes notes = new Notes(name, description , time);
                        notesList.add(notes);
                    }

                } catch (JSONException | IOException e) {
                    Log.d(TAG, "addItemsFromJSON: ", e);
                }
                Notes n = new Notes(title.getText().toString(),
                        description.getText().toString(),
                        String.valueOf(System.currentTimeMillis())
                         );
                notesList.add(n);
//              Toast.makeText(this, "-- "+n.getTime(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,title.getText().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,description.getText().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,n.toString(),Toast.LENGTH_SHORT).show();
                savejson();
                Intent intent = new Intent(EditActivity.this , MainActivity.class);
                startActivity(intent);
            }

            return true;
        }
        else {
            Toast.makeText(this, "No Such Option", Toast.LENGTH_SHORT).show();
            return true;
        }

    }
    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
//            inputStream = getResources().openRawResource(R.raw.holidays);
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

    private void savejson() {
        Log.d(TAG, "saveJson: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notesList);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveNotes: JSON:\n" + notesList.toString());

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(this);
        builder.setMessage("Save Notes ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);


        builder
                .setPositiveButton(
                        "Save",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                                    try {

                                        String jsonDataString = readJSONDataFromFile();
                                        JSONArray jsonArray = new JSONArray(jsonDataString);

                                        for (int i=0; i<jsonArray.length(); ++i) {

                                            JSONObject itemObj = jsonArray.getJSONObject(i);

                                            String name = itemObj.getString("name");
                                            String description = itemObj.getString("description");
                                            String time = itemObj.getString("time");
                                            Notes notes = new Notes(name, description , time);
                                            notesList.add(notes);
                                        }

                                    } catch (JSONException | IOException e) {
                                        Log.d(TAG, "addItemsFromJSON: ", e);
                                    }
                                    Notes n = new Notes(title.getText().toString(),
                                            description.getText().toString() ,
                                            String.valueOf(System.currentTimeMillis())
                                            );
                                    notesList.add(n);
                                    savejson();
                                    Intent intent = new Intent(EditActivity.this , MainActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });

        builder
                .setNegativeButton(
                        "Discard",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                dialog.cancel();
                                Intent i = new Intent(EditActivity.this , MainActivity.class);
                                startActivity(i);
                            }
                        });


        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}