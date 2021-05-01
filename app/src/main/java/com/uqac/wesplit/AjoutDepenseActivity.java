package com.uqac.wesplit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.adapters.Depense;
import com.uqac.wesplit.enums.CategoriesEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 Activité permettant d'ajouter une dépense
 */

public class AjoutDepenseActivity extends AppCompatActivity {

    private Context activity;
    private EditText depenseTitre, depenseMontant, depneseQuantite, date;
    private Spinner spinnerCategories;
    private Button btnConfirmer;
    private ImageButton btnBack;
    private ListView listUsersCheckbox;
    private CategoriesEnum depenseCategorieValue;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_depense);
        activity = this;

        // Récupération des éléments de la vue
        depenseTitre = (EditText) findViewById(R.id.depense_titre);
        depenseMontant = (EditText) findViewById(R.id.depense_montant);
        depneseQuantite = (EditText) findViewById(R.id.depense_date);
        spinnerCategories = (Spinner) findViewById(R.id.depense_categorie);
        btnConfirmer = (Button) findViewById(R.id.btn_ajout_depense);
        btnBack = (ImageButton) findViewById(R.id.btn_depense_retour);

        // authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final Calendar myCalendar = Calendar.getInstance();

        final EditText edittext= (EditText) findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar. MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edittext.setText(sdf.format(myCalendar.getTime()));            }


        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AjoutDepenseActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // Création de la liste des catégories de dépenses possibles
        spinnerCategories.setAdapter(new ArrayAdapter<CategoriesEnum>(this, android.R.layout.simple_spinner_dropdown_item, CategoriesEnum.values()));

        // Ajout d'une dépense
        btnConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titre = depenseTitre.getText().toString().trim();
                final String montant = depenseMontant.getText().toString().trim();
                String quantite = depneseQuantite.getText().toString().trim();
                // Contrôles

                if (TextUtils.isEmpty(titre)) {
                    Toast.makeText(getApplicationContext(), "Entrez un titre !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(quantite)) {
                    quantite = "1";
                }
                if (TextUtils.isEmpty(montant)) {
                    Toast.makeText(getApplicationContext(), "Entrez un montant !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (depenseCategorieValue.toString() == null || TextUtils.isEmpty(depenseCategorieValue.toString())) {
                    Toast.makeText(getApplicationContext(), "Sélectionnez une catégorie de dépense !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edittext.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Entrez une date", Toast.LENGTH_SHORT).show();
                    return;
                }


                DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

                final String finalQuantite = quantite;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nomGroupe = (String) dataSnapshot.child("groupe").getValue();
                        String nameUser = (String) dataSnapshot.child("name").getValue();

                        if (nomGroupe != null && nameUser != null) {

                            DatabaseReference ref = database.getReference("groupes/" + nomGroupe + "/depenses");

                            Map<String, String> userMap = new HashMap<>();



                            // Construction de l'objet dépense
                            Depense depense = new Depense();
                            depense.setTitre(titre);
                            depense.setMontant(montant);
                            depense.setQuantite(finalQuantite);
                            depense.setCategorie(depenseCategorieValue.toString());
                               // depense.setTimestamp(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "");
depense.setTimestamp(edittext.getText().toString());
                            ref.child(ref.push().getKey()).setValue(depense);

                            startActivity(new Intent(AjoutDepenseActivity.this, MainActivity.class));
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
                startActivity(new Intent(AjoutDepenseActivity.this, MainActivity.class));
                finish();
            }
        });

        spinnerCategories.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                depenseCategorieValue = (CategoriesEnum) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
    }
}
