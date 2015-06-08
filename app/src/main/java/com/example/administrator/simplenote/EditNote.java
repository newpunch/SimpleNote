package com.example.administrator.simplenote;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/1.
 */
public class EditNote extends Activity implements View.OnClickListener {
    private static final int PICK_CODE = 1;
    private static final int CAMERA_CODE = 2;
    private EditText titleText;
    private EditText contentText;
    private TextView timeText;
    private ImageView contentImg;
    private ImageView contentPhoto;
    private VideoView contentVideo;
    private Button addImg;
    private Button addVideo;
    private Button takePhoto;
    private String noteTitle;
    private  String noteContent;
    private  String noteTime;
    private File imgFile;
    private File photoFile;


    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    protected  String noteTitlePrevious;
    protected  String noteContentPrevious;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
        initView();
        initEven();
        timeText.setText(getTime());


    }

    private void initView() {
        titleText = (EditText) findViewById(R.id.title);
        contentText = (EditText) findViewById(R.id.content);
        timeText = (TextView) findViewById(R.id.note_time);
        contentImg = (ImageView) findViewById(R.id.c_img);
        contentPhoto = (ImageView) findViewById(R.id.c_photo);
        contentVideo = (VideoView) findViewById(R.id.c_video);
        addImg = (Button) findViewById(R.id.add_img);
        takePhoto = (Button) findViewById(R.id.take_photo);
        addVideo = (Button) findViewById(R.id.add_video);

    }

    private void initEven() {
        addImg.setOnClickListener(this);
        addVideo.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_img:
                contentImg.setVisibility(View.VISIBLE);
                imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                        + getTime() +".jpg");
                Intent getImgIntent = new Intent(Intent.ACTION_PICK);
                getImgIntent.setType("image/*");
                getImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                startActivityForResult(getImgIntent, PICK_CODE);
                break;
            case R.id.take_photo:
                contentImg.setVisibility(View.VISIBLE);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                        + getTime() + ".jpg");
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePhotoIntent,CAMERA_CODE);
                break;
            case R.id.add_video:
                contentVideo.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, titleText.getText().toString());
        cv.put(NotesDB.CONTENT, contentText.getText().toString());
        cv.put(NotesDB.CONTENT, timeText.getText().toString());
        cv.put(NotesDB.IMG, imgFile+"");
        cv.put(NotesDB.PHOTO, photoFile+"");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public String getTime(){
        SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate   =   new Date(System.currentTimeMillis());
        String str = dateFormat24.format(curDate);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PICK_CODE){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            contentImg.setImageBitmap(bitmap);
        }
        if (resultCode == CAMERA_CODE){
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            contentPhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        noteTitle = titleText.getText().toString();
        noteContent = contentText.getText().toString();
        if(noteTitle.equals(noteTitlePrevious) && noteContent.equals(noteContentPrevious)){
            setResult(RESULT_CANCELED);
            finish();
        }
        if(noteTitle.equals("")){
            if(noteContent.equals("")){
                setResult(RESULT_CANCELED);
                finish();
            }
            noteTitle = noteContent;
        }

        Intent noteIntent = new Intent();
        noteIntent.putExtra("time", noteTime);
        noteIntent.putExtra("title", noteTitle);
        setResult(RESULT_OK, noteIntent);
        editor = getSharedPreferences(noteTitle + noteTime,MODE_PRIVATE).edit();
        editor.putString("noteContent", noteContent);
        editor.putString("noteTime", noteTime);
        editor.putString("noteTitle", noteTitle);
        editor.commit();

        addDB();
        finish();
    }

}
