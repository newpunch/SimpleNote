package com.example.administrator.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
    private NoteAdapter adapter;
    private Button newNote;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private ListView noteList;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEven();

    }

    private void initView() {
        newNote = (Button) findViewById(R.id.new_note);
        noteList = (ListView) findViewById(R.id.note_list_view);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
    }


    private void initEven() {
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditNote.class);
                startActivity(intent);
            }
        });
    }

    private void selectDB() {
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new NoteAdapter(this, cursor);
        noteList.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

}