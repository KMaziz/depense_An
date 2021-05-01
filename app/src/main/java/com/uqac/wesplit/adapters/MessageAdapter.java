package com.uqac.wesplit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.uqac.wesplit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 Adapter permettant de générer chaque un élément de la ListView des messages
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> objects;

    public MessageAdapter(Context context, int textViewResourceId, ArrayList<Message> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_list_messages, null);
        }


        FirebaseAuth auth = FirebaseAuth.getInstance();

        Message i = getItem(position);


        RelativeLayout relativeLayoutRight = (RelativeLayout) v.findViewById(R.id.zone_text_right);
        RelativeLayout relativeLayoutLeft = (RelativeLayout) v.findViewById(R.id.zone_text_left);

        relativeLayoutRight.setVisibility(View.INVISIBLE);
        relativeLayoutLeft.setVisibility(View.INVISIBLE);

        if (i != null && i.getUserid() != null && i.getDate() != null && i.getMessage() != null && i.getUser() != null) {


            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(Long.parseLong(i.getDate()) * 1000);
            String id = auth.getCurrentUser().getUid();
            String userid = i.getUserid();
            if(i.getUserid().equals(auth.getCurrentUser().getUid())) {

                relativeLayoutRight.setVisibility(View.VISIBLE);
                TextView message = (TextView) v.findViewById(R.id.text_right);
                TextView nameDate = (TextView) v.findViewById(R.id.text_right_bottom);

                if (message != null) {
                    message.setText(i.getMessage());
                }
                if (nameDate != null) {
                    nameDate.setText("Moi, le " + sf.format(date));
                }

            } else {
                relativeLayoutLeft.setVisibility(View.VISIBLE);
                TextView message = (TextView) v.findViewById(R.id.text_left);
                TextView nameDate = (TextView) v.findViewById(R.id.text_left_bottom);

                if (message != null) {
                    message.setText(i.getMessage());
                }
                if (nameDate != null) {
                    nameDate.setText(i.getUser() + ", le " + sf.format(date));
                }
            }
        }

        return v;

    }
}


