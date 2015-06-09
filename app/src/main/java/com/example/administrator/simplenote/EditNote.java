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
    private static final int VIDEO_CODE = 3;
    private EditText titleText, contentText;
    private TextView timeText;
    private ImageView contentImg,contentPhoto;
    private VideoView contentVideo;
    private Button addImg, addVideo, takePhoto;
    private String noteTitle, noteContent, noteTime;
    private File imgFile, photoFile, videoFile;



    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;




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
                Intent videoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                         +getTime() +".mp4");
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                startActivityForResult(videoIntent, VIDEO_CODE);
                break;
        }
    }


    private void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.TITLE, titleText.getText().toString());
        cv.put(NotesDB.CONTENT, contentText.getText().toString());
        cv.put(NotesDB.TIME, timeText.getText().toString());
        cv.put(NotesDB.IMG, imgFile+"");
        cv.put(NotesDB.PHOTO, photoFile+"");
        cv.put(NotesDB.VIDEO, videoFile+"");
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
        if (resultCode == VIDEO_CODE){
            contentVideo.setVideoURI(Uri.fromFile(videoFile));
            contentVideo.start();
        }
    }

    @Override
    public void onBackPressed() {
        addDB();
        finish();
    }

}
