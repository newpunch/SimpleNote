package com.example.administrator.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private List<NoteList> noteList = new ArrayList<NoteList>();
    private NoteAdapter adapter;
    private ListView noteListView;
    private String titleData;
    private String timeData;
    private Intent intent1;
    private Button newNote;
    public static final int RequestCode = 1;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEven();

    }

    private void initView() {
        newNote = (Button) findViewById(R.id.new_note);
        adapter = new NoteAdapter(MainActivity.this,
                R.layout.list_item, noteList);
        noteListView = (ListView) findViewById(R.id.note_list_view);
        noteListView.setAdapter(adapter);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
    }

    private void initEven() {
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(MainActivity.this, EditNote.class);
                intent1.putExtra("savedName"," ");
                startActivityForResult(intent1, RequestCode);

            }
        });
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteList selectNote = noteList.get(position);
                intent1 = new Intent(MainActivity.this, EditNote.class);
                intent1.putExtra("savedName", selectNote.getTitle() + selectNote.getTime());
                startActivityForResult(intent1, RequestCode);
            }
        });
    }

    private void selectDB() {
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new NoteAdapter(this, cursor);

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == RequestCode && resultCode == RESULT_OK){
            String title = data.getStringExtra("title");
            String time = data.getStringExtra("time");
            Log.d("text",title + time);
            NoteList newNoteList = new NoteList(title,time);
            noteList.add(newNoteList);
            adapter.notifyDataSetChanged();
            noteListView.setSelection(1);
        }
    }
}