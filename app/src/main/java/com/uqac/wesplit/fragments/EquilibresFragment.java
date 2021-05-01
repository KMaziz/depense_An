package com.uqac.wesplit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.AjoutPersonelle;
import com.uqac.wesplit.R;
import com.uqac.wesplit.adapters.Personelle;
import com.uqac.wesplit.adapters.PersonelleAdapter;

import java.util.ArrayList;
import java.util.Map;

/**
 Fragment affichant les équilibres entre les personnes
 */

public class EquilibresFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ProgressBar progressBar;
    private LinearLayout ll;
    private ListView listview;
    Button ajout;
    Map<String, String> usersGroupe;
    final ArrayList<Personelle> items = new ArrayList<>();
    PersonelleAdapter adapter;
    public EquilibresFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equilibres, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ajout = (Button) view.findViewById(R.id.button2);
        listview = (ListView) view.findViewById(R.id.listview_personelle);

        adapter = new PersonelleAdapter(this.getActivity(), R.layout.row_equilibre, items);
        listview.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        // Authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AjoutPersonelle.class));

            }
        });

        DatabaseReference refUser = database.getReference("users/" + auth.getCurrentUser().getUid());

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String groupe = (String) dataSnapshot.child("groupe").getValue();

                DatabaseReference ref = database.getReference("groupes/" + groupe + "/personelle");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() == 0) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                ref.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Personelle personelle = dataSnapshot.getValue(Personelle.class);
                        personelle.set_id(dataSnapshot.getKey());
                        items.add(personelle);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        items.remove(dataSnapshot.getValue(Personelle.class));
                        adapter.notifyDataSetChanged();
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

}
