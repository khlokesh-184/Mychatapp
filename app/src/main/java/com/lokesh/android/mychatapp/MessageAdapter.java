package com.lokesh.android.mychatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        from.setText(m.getFrom());
        text.setText(m.getText());
        createdAt.setText(formattedDate);
        // Return the completed view to render on screen
        return convertView;
    }

}
