package com.uqac.wesplit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.enums.CategoriesEnum;
import com.uqac.wesplit.adapters.Depense;

import java.util.HashMap;
import java.util.Map;

/**
 Activité permettant de modifier une dépense
 */

public class ModificationDepenseActivity extends AppCompatActivity {

    private Context activity;
    private EditText depenseTitre, depenseMontant,depenseqte;
    private Spinner spinnerCategories;
    private Button btnConfirmer, btnSupprimer;
    private ImageButton btnBack;
    private CategoriesEnum depenseCategorieValue;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_depense);
        activity = this;

        // Récupération des éléments de la vue
        depenseTitre = (EditText) findViewById(R.id.depense_titre);
        depenseMontant = (EditText) findViewById(R.id.depense_montant);
        spinnerCategories = (Spinner) findViewById(R.id.depense_categorie);
        btnConfirmer = (Button) findViewById(R.id.btn_modification_depense);
        btnSupprimer = (Button) findViewById(R.id.btn_suppression_depense);
        btnBack = (ImageButton) findViewById(R.id.btn_depense_retour);
        depenseqte =(EditText)findViewById(R.id.depense_date);
        // Authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Récupération de la dépense sélectionnée et affichage de celle-ci.
        final Depense depense = (Depense) getIntent().getSerializableExtra("depense");
        depenseTitre.setText(depense.getTitre());
        depenseMontant.setText(depense.getMontant());
        depenseqte.setText(depense.getQuantite());
        spinnerCategories.setAdapter(new ArrayAdapter<CategoriesEnum>(this, android.R.layout.simple_spinner_dropdown_item, CategoriesEnum.values()));
        spinnerCategories.setSelection(getIndex(spinnerCategories, depense.getCategorie()));

        // Clic sur le bouton de confirmation de modification de la dépense
        btnConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titre = depenseTitre.getText().toString().trim();
                final String montant = depenseMontant.getText().toString().trim();
                String qte = depenseqte.getText().toString().trim();
                // Contrôles des données saisies
                if (TextUtils.isEmpty(titre)) {
                    Toast.makeText(getApplicationContext(), "Entrez un titre !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(montant)) {
                    Toast.makeText(getApplicationContext(), "Entrez un montant !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(qte)) {
                    qte="1";
                }
                if (depenseCategorieValue.toString() == null || TextUtils.isEmpty(depenseCategorieValue.toString())) {
                    Toast.makeText(getApplicationContext(), "Sélectionnez une catégorie de dépense !", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

                final String finalQte = qte;
                final String finalQte1 = qte;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nomGroupe = (String) dataSnapshot.child("groupe").getValue();
                        String nameUser = (String) dataSnapshot.child("name").getValue();

                        if(nomGroupe != null && nameUser != null) {

                            DatabaseReference ref = database.getReference("groupes/" + nomGroupe + "/depenses/" + depense.get_id());

                            Map<String, String> userMap = new HashMap<>();

                            if(!titre.equals(depense.getTitre()))
                                ref.child("titre").setValue(titre);
                            if(!montant.equals(depense.getMontant()))
                                ref.child("montant").setValue(montant);
                            if(!depenseCategorieValue.toString().equals(depense.getCategorie()))
                                ref.child("categorie").setValue(depenseCategorieValue.toString());
                            if(!finalQte.toString().equals(depense.getQuantite()))
                                ref.child("quantite").setValue(finalQte1.toString());
                            ref.child("users").setValue(userMap);
                            startActivity(new Intent(ModificationDepenseActivity.this, MainActivity.class));
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

        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nomGroupe = (String) dataSnapshot.child("groupe").getValue();

                        if(nomGroupe != null) {
                            DatabaseReference refDepense = database.getReference("groupes/" + nomGroupe + "/depenses/" + depense.get_id());
                            refDepense.removeValue();
                            startActivity(new Intent(ModificationDepenseActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        // Retour à l'activité principale
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificationDepenseActivity.this, MainActivity.class));
                finish();
            }
        });

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                depenseCategorieValue = (CategoriesEnum) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

    }

    private int getIndex(Spinner spinner, String string)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
                index = i;
                break;
            }
        }
        return index;
    }
}
