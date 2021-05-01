package com.uqac.wesplit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uqac.wesplit.R;

import java.util.ArrayList;

public class PersonelleAdapter extends ArrayAdapter<Personelle> {

    private ArrayList<Personelle> objects;

    public PersonelleAdapter(Context context, int textViewResourceId, ArrayList<Personelle> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public Personelle getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_equilibre, null);
        }

        Personelle i = getItem(position);

        if (i != null) {

            TextView titre = (TextView) v.findViewById(R.id.view_titre);
            TextView payepar = (TextView) v.findViewById(R.id.view_paye_par);

            if (titre != null){
                titre.setText(i.getNom());
            }
            if (payepar != null){
                payepar.setText(i.getSalaire());
            }
        }

        return v;

    }


}