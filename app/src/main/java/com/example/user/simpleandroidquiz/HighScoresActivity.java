package com.example.user.simpleandroidquiz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hitesh.asimplegame.R;

public class HighScoresActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    //private ArrayList<Player> players;
    Context context = this;
    TableLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        final ArrayList<Player> players = new ArrayList<Player>();
        layout = (TableLayout) findViewById(R.id.highScoresTable);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query scoresQuery = mDatabase.child("Scores").orderByChild("Score").limitToLast(10);
        scoresQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                players.clear();
                layout.removeAllViews();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String uid = (String) ds.getKey();
                    Long score = (Long) ds.child("Score").getValue();
                    String username  = (String) ds.child("User").getValue();
                    players.add(new Player(username, uid, score));
                }

                int i = 0;

                TableRow firstRow = new TableRow(getApplicationContext());
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                firstRow.setLayoutParams(lp1);
                firstRow.setGravity(Gravity.CENTER);

                TextView numberTextView = new TextView(getApplicationContext());
                customizeFirstRowTextView("Position", numberTextView);
                numberTextView.setPaintFlags(numberTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                TextView uidTextView = new TextView(getApplicationContext());
                customizeFirstRowTextView("Uid", uidTextView);
                uidTextView.setPaintFlags(uidTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                TextView playerNameTextView = new TextView(getApplicationContext());
                customizeFirstRowTextView("Player Name", playerNameTextView);
                playerNameTextView.setPaintFlags(playerNameTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                TextView playerScoreTextView = new TextView(getApplicationContext());
                customizeFirstRowTextView("Score", playerScoreTextView);
                playerScoreTextView.setPaintFlags(playerScoreTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                firstRow.addView(numberTextView);
                firstRow.addView(uidTextView);
                firstRow.addView(playerNameTextView);
                firstRow.addView(playerScoreTextView);
                layout.addView(firstRow,i);

                i++;

                Collections.sort(players, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        Player p1 = (Player) o1;
                        Player p2 = (Player) o2;
                        if (p1.getScore() >= p2.getScore()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                for (Player player: players){
                    TableRow row = new TableRow(getApplicationContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    row.setGravity(Gravity.CENTER);

                    TextView numberText = new TextView(getApplicationContext());
                    customizeRowTextView(Integer.toString(i), numberText);
                    numberText.setGravity(Gravity.CENTER);

                    TextView uidText = new TextView(getApplicationContext());
                    customizeRowTextView(player.getUid().substring(0,2)+"..."+player.getUid()
                            .substring(player.getUid().length()-2), uidText);

                    TextView playerNameText = new TextView(getApplicationContext());
                    customizeRowTextView(player.getUsername(), playerNameText);

                    TextView playerScoreText = new TextView(getApplicationContext());
                    customizeRowTextView(Long.toString(player.getScore()), playerScoreText);
                    playerScoreText.setGravity(Gravity.CENTER);

                    if(player.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        numberText.setTextColor(getResources().getColor(R.color.colorGreen));
                        uidText.setTextColor(getResources().getColor(R.color.colorGreen));
                        playerNameText.setTextColor(getResources().getColor(R.color.colorGreen));
                        playerNameText.setText("You");
                        playerScoreText.setTextColor(getResources().getColor(R.color.colorGreen));
                    }

                    row.addView(numberText);
                    row.addView(uidText);
                    row.addView(playerNameText);
                    row.addView(playerScoreText);
                    layout.addView(row,i);
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException();
            }
        });
    }

    public void customizeFirstRowTextView(String text, TextView txtView){
        txtView.setText(text);
        txtView.setPaddingRelative(5,0,15,0);
        txtView.setTextColor(getResources().getColor(R.color.colorAccent));
        txtView.setShadowLayer(15, 5, 5, getResources().getColor(R.color.colorBlack));
        txtView.setTextSize(20);
    }

    public void customizeRowTextView(String text, TextView txtView){
        txtView.setText(text);
        txtView.setPaddingRelative(5,0,15,0);
        txtView.setTextColor(Color.parseColor("#ffffff"));
        txtView.setShadowLayer(15, 5, 5, getResources().getColor(R.color.colorBlack));
    }

}
