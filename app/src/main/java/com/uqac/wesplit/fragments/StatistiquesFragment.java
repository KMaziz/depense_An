package com.uqac.wesplit.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.R;
import com.uqac.wesplit.enums.CategoriesEnum;
import com.uqac.wesplit.enums.PeriodesEnum;
import com.uqac.wesplit.helpers.CalculateurStatistiques;
import com.uqac.wesplit.adapters.Depense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 Fragment affichant les statistiques des dépenses
 */

public class StatistiquesFragment extends Fragment {

    private Spinner spinnerPeriode;
    private TextView depensesPeriode;
    private PeriodesEnum periodeSelected;
    private CalculateurStatistiques calculateurStatistiques;
    private PieChart categoriesChart;
    private PieDataSet set;
    private ArrayList<Depense> items;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private static final int[] COLORS = {
            Color.rgb(245, 205, 121), Color.rgb(61, 193, 211), Color.rgb(248, 165, 194),
            Color.rgb(252, 92, 101), Color.rgb(33, 206, 153), Color.rgb(120, 111, 166)
    };

    public StatistiquesFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistiques, container, false);

        depensesPeriode = (TextView) view.findViewById(R.id.depenses_periode_text);
        spinnerPeriode = (Spinner) view.findViewById(R.id.spinner_periode);
        categoriesChart = (PieChart) view.findViewById(R.id.categories_chart);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        spinnerPeriode.setAdapter(new ArrayAdapter<PeriodesEnum>(getActivity(), android.R.layout.simple_spinner_dropdown_item, PeriodesEnum.values()));
        spinnerPeriode.setZ(100);


        items = new ArrayList<>();
        calculateurStatistiques = new CalculateurStatistiques();

        categoriesChart.setHoleRadius(33);
        categoriesChart.setTransparentCircleRadius(37);
        categoriesChart.setEntryLabelColor(Color.parseColor("#ffffff"));
        categoriesChart.setUsePercentValues(true);

        List<PieEntry> entries = new ArrayList<>();
        for (CategoriesEnum categorie : CategoriesEnum.values()) {
            entries.add(new PieEntry(16.65f, categorie.toString()));
        }
        set = new PieDataSet(null, "Répartition des dépenses");
        set.setLabel("Répartition des dépenses");
        set.setColors(COLORS);
        set.setValueFormatter(new PercentFormatter());
        set.setValueTextSize(16f);
        set.setValueTextColor(Color.WHITE);

        PieData data = new PieData(set);
        categoriesChart.setData(data);
        categoriesChart.invalidate();



        spinnerPeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                periodeSelected = (PeriodesEnum) adapterView.getItemAtPosition(position);
                refreshGraph(periodeSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        DatabaseReference refUser = database.getReference("users/" + auth.getCurrentUser().getUid());

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String groupe = (String) dataSnapshot.child("groupe").getValue();

                DatabaseReference ref = database.getReference("groupes/" + groupe + "/depenses");

                ref.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Depense depense = dataSnapshot.getValue(Depense.class);
                        depense.set_id(dataSnapshot.getKey());
                        items.add(depense);
                        refreshGraph(PeriodesEnum.SEPTJOURS);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        items.remove(dataSnapshot.getValue(Depense.class));
                        refreshGraph(PeriodesEnum.SEPTJOURS);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return view;
    }

    private void refreshGraph(PeriodesEnum periodeSelected) {

        calculateurStatistiques.repartirDepenses(items, periodeSelected);

        List<PieEntry> entries = new ArrayList<>();
        Iterator it = calculateurStatistiques.obtenirPourcentages().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if((Float) pair.getValue() > 0.0f) {
                entries.add(new PieEntry((Float) pair.getValue(), pair.getKey().toString()));
            }
        }

        set.setValues(entries);
        PieData data = new PieData(set);
        categoriesChart.setData(data);
        categoriesChart.invalidate();

        depensesPeriode.setText("Dépenses totales sur la période: " + String.format("%.2f", calculateurStatistiques.getTotalDepenses()) + " dt");
    }
}
