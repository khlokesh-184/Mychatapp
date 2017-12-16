package com.lokesh.android.mychatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.lokesh.android.mychatapp.R.id.createdAt;
import static com.lokesh.android.mychatapp.R.id.from;

/**
 * Created by LOKESH on 10-12-2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, ArrayList<Message> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message m = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item, parent, false);
        }
        // Lookup view for data population
        TextView from = (TextView) convertView.findViewById(R.id.from);
        TextView text = (TextView) convertView.findViewById(R.id.text);
        TextView createdAt = (TextView) convertView.findViewById(R.id.createdAt);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        // Populate the data into the template view using the data object

        int i=m.getMessageFormat();

        from.setText(m.getFrom());
        if(i==1){
            text.setText(m.getText());
        }
        else {
            try {
                final String geoURI = m.getText();
                text.setText("My current location");
                text.setAllCaps(true);
                text.setTextColor(Color.MAGENTA);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoURI));
                        getContext().startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        createdAt.setText(formattedDate);
        // Return the completed view to render on screen
        return convertView;
    }

}
