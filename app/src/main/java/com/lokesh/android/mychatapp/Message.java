package com.lokesh.android.mychatapp;

/**
 * Created by LOKESH on 10-12-2017.
 */

public class Message {
    private String from;
    private String text;
    private int i;

    Message( String from, String text, String createdAt, int i){
        this.i = i;
        this.text = text;
        this.from = from;
    }

    public String getFrom(){
        return from;
    }

    public String getText(){
        return text;
    }

    public int getMessageFormat(){
        return i;
    }
}
