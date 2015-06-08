package com.example.administrator.simplenote;

/**
 * Created by Administrator on 2015/6/1.
 */
public class NoteList {
    public String content;
    public String title;
    public String time;
    public NoteList(String title, String time, String content){
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getContent(){
        return content;
    }
}