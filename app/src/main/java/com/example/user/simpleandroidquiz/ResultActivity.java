package com.example.user.simpleandroidquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.simpleandroidquiz.Utilities.QuizHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hitesh.asimplegame.R;

/**
 * Created by user on 16/3/2017.
 */

public class ResultActivity extends AppCompatActivity{

    private int score;
    DatabaseReference mDatabase;
    String user, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textResult = (TextView) findViewById(R.id.textResult);

        Bundle b = getIntent().getExtras();

        score = b.getInt("score");

        textResult.setText("Your score is " + " " + score + ".");

        if(isUserLogin()){

            mDatabase = FirebaseDatabase.getInstance().getReference();
            user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mDatabase.child("Scores").child(userID).child("Score").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long previousScore = (Long) dataSnapshot.getValue();
                    if(previousScore == null){
                        mDatabase.child("Scores").child(userID).child("User").setValue(user);
                        mDatabase.child("Scores").child(userID).child("Score").setValue(score);
                    }
                    else {
                        if(previousScore < score){
                            mDatabase.child("Scores").child(userID).child("User").setValue(user);
                            mDatabase.child("Scores").child(userID).child("Score").setValue(score);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.toException();
                }
            });

        }
    }

    public void playagain(View o) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

    public void mainMenu(View o) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isUserLogin(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            return true;
        }
        return false;
    }

    public void highScoresFromResultActivity(View view){
        if (isUserLogin()){
            Intent highScoresIntent = new Intent (this, HighScoresActivity.class);
            startActivity(highScoresIntent);
        } else {
            Toast.makeText(this, R.string.mustLoginFirst, Toast.LENGTH_LONG).show();
        }
    }

}
