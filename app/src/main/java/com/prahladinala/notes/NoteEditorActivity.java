package com.prahladinala.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        // DISPLAY TEXT OF NOTE > USER HAS SELECTED
        EditText editText = (EditText) findViewById(R.id.editTextTextMultiLine);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        if(noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            // CREATING EMPTY NOTE
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // s is the new text
                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                // PERMANENT STORAGE
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.prahladinala.notes", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes",set).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}