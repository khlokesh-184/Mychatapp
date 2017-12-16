package com.lokesh.android.mychatapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by LOKESH on 12-12-2017.
 */

public class ChatActivity extends Activity {
    private EditText message;
    private ImageButton sendButton;
    private String messageString;
    private Socket mSocket;
    private ListView list;
    private ListView peopleListView;
    boolean joinSuccessful;
    private MessageAdapter itemsAdapter;
    private ArrayAdapter<String> peopleAdapter;
    private double longitude;
    private double latitude;

    private boolean sentSuccessful;
    private ArrayList<String> peopleList = new ArrayList<String>();
    private ArrayList<Message> message_list = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        list = (ListView) findViewById(R.id.message_list);
        peopleListView = (ListView) findViewById(R.id.people_list);

        message = (EditText) findViewById(R.id.message);

        try {
            mSocket = IO.socket("https://chat-app-by-me.herokuapp.com/");
            mSocket.connect();
            //mSocket.emit("createMessage", "thyrd");
            //Log.i("mnb", ""+mSocket.connected());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Bundle extras = getIntent().getExtras();
        String userName=extras.getString("userName");
        String roomName=extras.getString("roomName");

        JSONObject joinData = new JSONObject();
        try {
            joinData.put("name", userName);
            joinData.put("room", roomName);
            //Log.i("mnb", "3");
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        mSocket.emit("join", joinData, new Ack() {
            @Override
            public void call(Object... args) {

            }
        } );



        sendButton = (ImageButton) findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Log.i("mnb", "2");
                attemptMessageSending();
                message.setText("");
            }
        });

        mSocket.on("newMessage", onNewMessage);
        mSocket.on("updateUsersList", onPeopleList);
        mSocket.on("newLocationMessage", onLocationMessage);

        itemsAdapter = new MessageAdapter(this, message_list);
        itemsAdapter.notifyDataSetChanged();

        peopleAdapter= new ArrayAdapter<String>(this, R.layout.people_list_item, peopleList);
        peopleAdapter.notifyDataSetChanged();

        list.setAdapter(itemsAdapter);
        peopleListView.setAdapter(peopleAdapter);

    }

    private void attemptMessageSending() {
        // Reset errors.
        //message.setError(null);

        // Store values at the time of the login attempt.
        messageString = message.getText().toString();

        // Check for a valid username.
        if (TextUtils.isEmpty(messageString)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            message.setError("this field is required");
            message.requestFocus();
            return;
        }

        // mUsername = username;

        // perform the user login attempt.
        JSONObject data = new JSONObject();
        try {
            data.put("from", "User");
            data.put("text", messageString);
            Log.i("mnb", "3");
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mSocket.emit("createMessage", data, new Ack() {
            @Override
            public void call(Object... args) {


            }
        });

    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String from;
                    String text;

                    Message m;
                    try {
                        from = data.getString("from");
                        text = data.getString("text");
                        m=new Message(from,text,data.getString("createdAt"),1);
                        message_list.add(m);
                    } catch (JSONException e) {
                        return;
                    }
                    itemsAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private Emitter.Listener onPeopleList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray peopleArray = (JSONArray) args[0];
                    peopleList.clear();
                    for(int i=0; i<peopleArray.length(); i++){
                        try{
                        peopleList.add(peopleArray.getString(i));
                        }catch (Exception e){
                            Log.i("mn", ""+e);
                        }
                    }
                    peopleAdapter.notifyDataSetChanged();

                    //Log.i("mn", "123"+args[0]);
                }
            });
        }
    };

    private Emitter.Listener onLocationMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.i("klj", "123"+args[0]);
                    JSONObject data = (JSONObject) args[0];
                    String from;
                    String murl;

                    Message m;
                    try {
                        from = data.getString("from");
                        murl = data.getString("url");
                        m=new Message(from,murl,data.getString("createdAt"),0);
                        message_list.add(m);
                    } catch (JSONException e) {
                        return;
                    }
                    itemsAdapter.notifyDataSetChanged();
                }
            });
        }
    };

}
