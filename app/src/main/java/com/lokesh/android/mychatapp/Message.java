package com.lokesh.android.mychatapp;

/**
 * Created by LOKESH on 10-12-2017.
 */

public class Message {
    private String from;
    private String text;
    private String createdAt;

    Message( String from, String text, String createdAt){
        this.createdAt = createdAt;
        this.from = from;
        this.text = text;
    }

    public String getFrom(){
        return from;
    }

    public String getText(){
        return text;
    }

    public String getCreatedAt(){
        return createdAt;
    }
}
