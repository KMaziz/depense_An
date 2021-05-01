package com.uqac.wesplit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uqac.wesplit.adapters.Message;
import com.uqac.wesplit.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 Activité permettant d'afficher le chat
 */


public class ChatActivity extends AppCompatActivity {

    private Context activity;
    private EditText inputMessage;
    private Button btnEnvoyer;
    private ImageButton btnBack;
    private ListView listMessages;
    private ArrayAdapter<Message> adapter;
    private Map<String, String> userInfos;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity = this;

        // Récupération des éléments de la vue
        inputMessage = (EditText) findViewById(R.id.message);
        btnEnvoyer = (Button) findViewById(R.id.btn_envoyer);
        btnBack = (ImageButton) findViewById(R.id.btn_chat_retour);
        listMessages = (ListView) findViewById(R.id.listview_messages);

        // authentification
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userInfos = new HashMap<>();
        final ArrayList<Message> messages = new ArrayList<>();

        adapter = new MessageAdapter(ChatActivity.this, R.layout.row_list_messages, messages);
        listMessages.setAdapter(adapter);

        DatabaseReference ref = database.getReference("users/" + auth.getCurrentUser().getUid());

        // Récupération de la liste des messages

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInfos = (Map<String, String>) dataSnapshot.getValue();

                DatabaseReference ref = database.getReference("groupes/" + userInfos.get("groupe") + "/messages");

                ref.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // Enregistrement et ajout du message reçu
                        Message message = dataSnapshot.getValue(Message.class);
                        message.set_id(dataSnapshot.getKey());
                        messages.add(message);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
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
            }
        });


        // Envoi du message
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                DatabaseReference ref = database.getReference("groupes/" + userInfos.get("groupe") + "/messages");

                // Contrôles de la validité du message
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(getApplicationContext(), "Entrez un message !", Toast.LENGTH_SHORT).show();
                    return;
                }
                inputMessage.setText("");

                // Envoi du message
                Map<String,String> datas = new HashMap<>();
                datas.put("message", message);
                datas.put("date", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "");
                datas.put("user", userInfos.get("name"));
                datas.put("userid", auth.getCurrentUser().getUid());

                String key = ref.push().getKey();
                ref.child(key).setValue(datas);
            }
        });

        // Retour à l'activité principale
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
