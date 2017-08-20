package com.legalimpurity.ChattingSampleApplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.legalimpurity.ChattingSampleApplication.Utils.MyPreferences;
import com.legalimpurity.ChattingSampleApplication.adapters.ChatAdapter;
import com.legalimpurity.ChattingSampleApplication.objects.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private ChatAdapter chatAdapter;

    @BindView(R.id.message_box)
    EditText message_box;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.list_of_messages)
    RecyclerView list_of_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setFabAction(this);
        setAdapter(this);
        displayChatMessages();
    }



    private void setFabAction(final AppCompatActivity act)
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToDatabase(act, message_box.getText().toString());
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
            FirebaseAuth.getInstance().signOut();
            MyPreferences.setString(this,MyPreferences.PROPERTY_USERNAME,"");
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.sign_out_msg),
                    Toast.LENGTH_LONG)
                    .show();
            Intent chatActivityClickIntent = new Intent(this,LoginActivity.class);
            startActivity(chatActivityClickIntent);
            this.finish();
        }
        return true;
    }

    private void displayChatMessages() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("messages");


        Query myTopPostsQuery = myRef.child("messages").orderByChild("messageTime");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMsg = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.addChatMessage(chatMsg);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMsg = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.changeChatMessage(chatMsg);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatMessage chatMsg = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.removeChatMessage(chatMsg);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMsg = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.removeChatMessage(chatMsg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setAdapter(AppCompatActivity act)
    {
        chatAdapter = new ChatAdapter(act);
        list_of_messages.setAdapter(chatAdapter);
        list_of_messages.setLayoutManager(new LinearLayoutManager(act));
    }

    private void writeToDatabase(AppCompatActivity act, String message)
    {
        String key = myRef.child("messages").push().getKey();
        ChatMessage messageObj = new ChatMessage(message, MyPreferences.getString(act,MyPreferences.PROPERTY_USERNAME));
        Map<String, Object> postValues = messageObj.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }

}
