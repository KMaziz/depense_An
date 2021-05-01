package com.uqac.wesplit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uqac.wesplit.R;

import java.util.ArrayList;

/**
 Adapter permettant de générer chaque un élément de la ListView des dépenses
 */

public class DepenseAdapter extends ArrayAdapter<Depense> {

    private ArrayList<Depense> objects;

    public DepenseAdapter(Context context, int textViewResourceId, ArrayList<Depense> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public Depense getItem(int position) {
        return super.getItem(getCount() - position - 1);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_list_depenses, null);
        }

        Depense i = getItem(position);

        if (i != null) {

            TextView titre = (TextView) v.findViewById(R.id.view_titre);
            TextView payepar = (TextView) v.findViewById(R.id.view_paye_par);
            TextView categorie = (TextView) v.findViewById(R.id.view_categorie);
            TextView montant = (TextView) v.findViewById(R.id.view_montant);

            if (titre != null){
                titre.setText(i.getTitre());
            }
            if (payepar != null){
                payepar.setText("quantité " + i.getQuantite());
            }
            if (categorie != null){
                categorie.setText(i.getCategorie());
            }
            if (montant != null){
                montant.setText(i.getMontant() + " dt");
            }
        }

        return v;

    }


}