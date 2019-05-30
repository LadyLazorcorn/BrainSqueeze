package com.example.user.simpleandroidquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hitesh.asimplegame.R;

/**
 * Created by user on 20/4/2017.
 */

public class StartActivity extends AppCompatActivity {

    private static final String TAG = StartActivity.class.getSimpleName();
    private static final String PATH_TOS = "";
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    Button loginButton, playButton, highScoresButton, exitButton, logoutButton, deleteUserButton;
    TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loginButton = (Button) findViewById(R.id.loginBtn);
        playButton = (Button) findViewById(R.id.playBtn);
        highScoresButton = (Button) findViewById(R.id.highScoresBtn);
        exitButton = (Button) findViewById(R.id.exitBtn);
        welcomeTextView = (TextView) findViewById(R.id.welcome_txtView);

        if(isUserLogin()){
            setUIForAuthenticatedUser();
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(PATH_TOS)
                        .build(), RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                final DatabaseReference userRef = mDatabase.getReference("Users/" + auth.getCurrentUser().getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            setUIForAuthenticatedUser();
                        }
                        else {
                            userRef.child("Full Name").setValue(auth.getCurrentUser().getDisplayName());
                            setUIForAuthenticatedUser();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
            if(resultCode == RESULT_CANCELED){
                displayMessage(getString(R.string.signin_failed));
            }
            return;
        }
        displayMessage(getString(R.string.unknown_response));
    }

    private void setUIForAuthenticatedUser(){
        logoutButton = findViewById(R.id.sign_out);
        logoutButton.setVisibility(View.VISIBLE);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(StartActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            signOut();
                            logoutButton.setVisibility(View.INVISIBLE);
                            deleteUserButton.setVisibility(View.INVISIBLE);
                            loginButton.setText(R.string.loginBtnTxt);
                        }else {
                            displayMessage(getString(R.string.sign_out_error));
                        }
                    }
                });
            }
        });
        deleteUserButton = (Button)findViewById(R.id.delete_user);
        deleteUserButton.setVisibility(View.VISIBLE);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        signOut();
                        logoutButton.setVisibility(View.INVISIBLE);
                        deleteUserButton.setVisibility(View.INVISIBLE);
                        loginButton.setText(R.string.loginBtnTxt);
                    }else{
                        displayMessage(getString(R.string.user_deletion_error));
                    }
                }
            });
            }
        });
        loginButton.setText(R.string.switchUserBtnTxt);
        welcomeTextView.setText("Welcome " + auth.getCurrentUser().getDisplayName());
    }

    private boolean isUserLogin(){
        if(auth.getCurrentUser() != null){
            return true;
        }
        return false;
    }

    private void signOut(){
        Intent signOutIntent = new Intent(this, StartActivity.class);
        startActivity(signOutIntent);
        finish();
    }

    private void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void play(View view){
        Intent playIntent = new Intent(this, CategoriesActivity.class);
        startActivity(playIntent);
        finish();
    }

    public void suggestQuestion(View view){
        Intent suggestIntent = new Intent(this, SuggestQuestionActivity.class);
        startActivity(suggestIntent);
    }

    public void highScores(View view){
        if (isUserLogin()){
            Intent highScoresIntent = new Intent (this, HighScoresActivity.class);
            startActivity(highScoresIntent);
        } else {
            Toast.makeText(this, R.string.mustLoginFirst, Toast.LENGTH_LONG).show();
        }
    }

}
