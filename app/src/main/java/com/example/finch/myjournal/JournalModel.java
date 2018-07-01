package com.example.finch.myjournal;

public class JournalModel {
    private String title;
    private String content;
    private String date;
    private String time;


    public JournalModel(String title, String text, String date, String time)
    {
        this.title = title;
        this.content = text;
        this.date = date;
        this.time = time;


    }
    public JournalModel(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}