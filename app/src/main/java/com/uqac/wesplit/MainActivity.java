package com.uqac.wesplit;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.fragments.DepensesFragment;
import com.uqac.wesplit.fragments.EquilibresFragment;
import com.uqac.wesplit.fragments.StatistiquesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 Activité principale. Instancie les fragments des différentes vues ainsi que le menu.
 */

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView headerName, headerEmail;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private ImageButton buttonChat;
    private FloatingActionButton buttonAddDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération des éléments de la vue
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.mytabs);
        buttonAddDepense = (FloatingActionButton) findViewById(R.id.add_depense_button);
        buttonChat = (ImageButton) findViewById(R.id.btn_chat);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Configuration de la toolbar
        toolbar.setTitle(getString(R.string.app_name));

        // Mise en place du système de navigation
        // Contrôle de l'authentification. Redirection vers l'activité de Login si l'utilisateur n'est pas authentifié
        // Sinon, création de la vue
        if (auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("kefi.aziz@yahoo.fr", "123456")
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // En cas d'échec d'authentification, on informe l'utilisateur d'un problème

                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            } else {

                                // En cas de succès d'authentification, on contrôle si l'utilisateur à déjà choisi un groupe ou non
                                // Si il es déjà associé à un groupe, il est rédirigé vers l'activité principale
                                // Si il ne l'est pas encore, il est redirigé vers la page de choix du groupe

                                DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        progressBar.setVisibility(View.GONE);

                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                });
                            }
                        }
                    });

        } else {

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            // Récupération des données de l'utilisateur
            DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String groupe = (String) dataSnapshot.child("groupe").getValue();

                    // Si l'utilisateur n'a pas encore créé ou rejoint un groupe, on le redirige vers l'activité de choix du groupe
                    // Sinon, on finalise l'affichage de l'interface


                        setViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
        // Clic sur le bouton permettant d'aller sur l'activité d'inscription
        buttonAddDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AjoutDepenseActivity.class));
            }
        });

        // Clic sur le boutton permettant d'aller sur le chat
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });

    }

    @Override protected void onResume() {
        super.onResume();
    }



    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DepensesFragment(), "Dépenses");
        adapter.addFragment(new EquilibresFragment(),"personelle");
        adapter.addFragment(new StatistiquesFragment(), "Statistiques");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void disconnect() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Vous avez été déconnecté", Toast.LENGTH_SHORT).show();
        finish();
    }

}
