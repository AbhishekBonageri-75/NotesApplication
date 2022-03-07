package com.example.notesapplication;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;

public class Notes {

    private final String title;
    private final String description;
    private final String time;

    Notes(String title , String description , String time)
    {
        this.title = title;
        this.description =description;
        this.time = time;
    }

    public String getTitle()
    {
        return title;
    }
    public String getTime()
    {
        return time;
    }
    public String getDescription()
    {
        return description;
    }

@NonNull
public String toString() {

    try {
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(sw);
        jsonWriter.setIndent("  ");
        jsonWriter.beginObject();
        jsonWriter.name("name").value(getTitle());
        jsonWriter.name("time").value(getTime());
        jsonWriter.name("description").value(getDescription());
        jsonWriter.endObject();
        jsonWriter.close();
        return sw.toString();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return "";
}

}
