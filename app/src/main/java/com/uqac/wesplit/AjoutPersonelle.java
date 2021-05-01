package com.uqac.wesplit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.adapters.Depense;
import com.uqac.wesplit.adapters.Personelle;
import com.uqac.wesplit.enums.CategoriesEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AjoutPersonelle extends AppCompatActivity {

    private Context activity;
    private Button btnConfirmer;
    private ImageButton btnBack;
    private EditText nom,salaire;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_personelle);
        activity = this;

        // Récupération des éléments de la vue
        nom = (EditText) findViewById(R.id.personelle_titre);
        salaire = (EditText) findViewById(R.id.personelle_montant);

        btnConfirmer = (Button) findViewById(R.id.btn_ajout_personelle);
        btnBack = (ImageButton) findViewById(R.id.btn_personelle_retour);


        // authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        btnConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titre = nom.getText().toString().trim();
                final String montant = salaire.getText().toString().trim();
                // Contrôles

                if (TextUtils.isEmpty(titre)) {
                    Toast.makeText(getApplicationContext(), "Entrez le nom !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(montant)) {
                    Toast.makeText(getApplicationContext(), "Entrez le salaire !", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nomGroupe = (String) dataSnapshot.child("groupe").getValue();
                        String nameUser = (String) dataSnapshot.child("name").getValue();

                        if (nomGroupe != null && nameUser != null) {

                            DatabaseReference ref = database.getReference("groupes/" + nomGroupe + "/personelle");

                            Map<String, String> userMap = new HashMap<>();



                            // Construction de l'objet dépense
                            Personelle personelle = new Personelle();
                            personelle.setNom(titre);
                            personelle.setSalaire(montant);

                            ref.child(ref.push().getKey()).setValue(personelle);

                            startActivity(new Intent(AjoutPersonelle.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // @todo toast
                    }
                });
            }
        });


        // Retour à l'activité principale
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjoutPersonelle.this, MainActivity.class));
                finish();
            }
        });
    }
}