package com.lokesh.android.mychatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.R.attr.data;
import static android.R.id.button1;
import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;
    private EditText muserName;
    private EditText mroomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        muserName = (EditText) findViewById(R.id.username);
        mroomName = (EditText) findViewById(R.id.roomname);

        //Log.i("mnbv", ""+erName+" "+omName+" ");

        try {
            mSocket = IO.socket("https://chat-app-by-me.herokuapp.com/");
            mSocket.connect();
            //mSocket.emit("createMessage", "thyrd");
            //Log.i("mnb", ""+mSocket.connected());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


        Button joinButton = (Button) findViewById(R.id.join_button);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String userName = muserName.getText().toString().trim();
                String roomName = mroomName.getText().toString().trim();
                if(userName.length() >0 && roomName.length() >0 && mSocket.connected() ){
                    Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("roomName", roomName);
                    startActivity(intent);
                }
                else if(!mSocket.connected()){

                    Toast.makeText(MainActivity.this, "Server down", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter correct information", Toast.LENGTH_SHORT).show();
                }
            }
        });


//



    }

}
