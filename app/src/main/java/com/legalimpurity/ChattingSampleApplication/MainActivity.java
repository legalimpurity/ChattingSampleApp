package com.legalimpurity.ChattingSampleApplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legalimpurity.ChattingSampleApplication.objects.ChatMessage;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public final static int SIGN_IN_REQUEST_CODE = 100;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @BindView(R.id.message_box)
    EditText message_box;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkFirebase(this);
        setFabAction();
    }

    private void checkFirebase(final AppCompatActivity act)
    {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(act,user);
                            } else {
                                updateUI(act,null);
                            }
                        }
                    });

        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.welcome_prefix) + mAuth
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
            displayChatMessages();
        }
    }

    private void updateUI(AppCompatActivity act, FirebaseUser user)
    {
        if(user == null)
        {
            Toast.makeText(act, getResources().getString(R.string.sign_in_error),
                    Toast.LENGTH_SHORT).show();
            displayChatMessages();
        }
        else
        {
            Toast.makeText(act, getResources().getString(R.string.sign_in_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setFabAction()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToDatabase(message_box.getText().toString());
                message_box.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            mAuth.signOut();
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.sign_out_msg),
                    Toast.LENGTH_LONG)
                    .show();
        }
        return true;
    }

    private void displayChatMessages() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

    }

    private void writeToDatabase(String message)
    {
        String key = myRef.child("messages").push().getKey();
        ChatMessage messageObj = new ChatMessage(message, mAuth.getCurrentUser().getDisplayName());
        Map<String, Object> postValues = messageObj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }

}
