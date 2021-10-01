package com.prahladinala.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        // GET PERMANENT STORAGE
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.prahladinala.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(set == null){
            notes.add("Click to Edit Notes");
        } else {
            notes = new ArrayList(set);
        }

        // ADD NOTES TO LISTVIEW
        // notes.add("Click to Edit Notes");
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,notes);

        listView.setAdapter(arrayAdapter);

        // ON CLICK ON NOTES MOVE TO OTHER ACTIVITY TO EDIT
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int i, long l) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", i);
                startActivity(intent);

            }
        });

        // ON LONG PRESS
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,int i, long l) {

                // ALERT DIALOG BUILDER
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // REMOVE FROM NOTES
                                notes.remove(i);
                                arrayAdapter.notifyDataSetChanged();

                                // PERMANENT STORAGE
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.prahladinala.notes", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}