package com.legalimpurity.ChattingSampleApplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.legalimpurity.ChattingSampleApplication.Utils.MyPreferences;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity{

    @BindView(R.id.username)
    AutoCompleteTextView username;

    @BindView(R.id.email_sign_in_button)
    Button email_sign_in_button;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppCompatActivity act = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(act);
        checkFirebaseAndPreferences(act);
        email_sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToChatApp(act);
            }
        });
    }

    private void loginToChatApp(final AppCompatActivity act)
    {
        if(mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateUI(act);
                            } else {
                                Toast.makeText(act, getResources().getString(R.string.sign_in_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            updateUI(act);
        }
    }

    private void checkFirebaseAndPreferences(final AppCompatActivity act)
    {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                checkPreferences(act);
                            } else {
                                Toast.makeText(act, getResources().getString(R.string.sign_in_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            checkPreferences(act);
        }
    }

    private void checkPreferences(AppCompatActivity act)
    {
        String tempUsername = MyPreferences.getString(act,MyPreferences.PROPERTY_USERNAME);
        if(!TextUtils.isEmpty(tempUsername))
        {
            Toast.makeText(this,
                    getResources().getString(R.string.welcome_prefix) + " " + tempUsername,
                    Toast.LENGTH_LONG)
                    .show();

            takeToChatActivity(act);
        }
    }

    private void updateUI(AppCompatActivity act)
    {
            Toast.makeText(act, getResources().getString(R.string.sign_in_msg),
                    Toast.LENGTH_SHORT).show();
            MyPreferences.setString(act,MyPreferences.PROPERTY_USERNAME,username.getText().toString());
            takeToChatActivity(act);
    }

    private void takeToChatActivity(final AppCompatActivity act)
    {
        Intent chatActivityClickIntent = new Intent(act,MainActivity.class);
        act.startActivity(chatActivityClickIntent);
        this.finish();
    }
}

