package com.example.user.simpleandroidquiz;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.simpleandroidquiz.Utilities.NetworkUtils;
import com.example.user.simpleandroidquiz.Utilities.QuizHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hitesh.asimplegame.R;

public class QuestionActivity extends AppCompatActivity {

    List<Question> questionsFromSpecificCategory;
    ArrayList<Question> questionList;
    int score=0;
    int qid=0;
    int livesCount=3, correctAnswersCounter=0;

    Question currentQ;
    TextView txtQuestion, times, scored, lives;
    Button button1, button2, button3;

    static boolean active = false;
    CounterClass timer;
    long millis;

    /* -----Below are the methods that handle the Activity LifeCycle----- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        ArrayList<String> selectedCategories = b.getStringArrayList("selectedCategories");

        QuizHelper db = new QuizHelper(this);
        questionList = new ArrayList<Question>();

        for (String category: selectedCategories){
            questionsFromSpecificCategory = db.getQuestionsFrom(category);
            Collections.shuffle(questionsFromSpecificCategory);
            for (Question q: questionsFromSpecificCategory.subList(0, 5)){
                questionList.add(q);
            }
            questionsFromSpecificCategory.clear();
        }

        //List<Question> questionList1 = db.getAllQuestions();  // This will fetch "all" questions.

        currentQ = questionList.get(qid); // The current question.

        txtQuestion = (TextView) findViewById(R.id.txtQuestion); // The textview in which the question will be displayed.

        // The three buttons. The idea is to set the text of three buttons with the options from question bank.
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        scored = (TextView) findViewById(R.id.score); // The textview in which score will be displayed.
        times = (TextView) findViewById(R.id.timers); // The timer.
        lives = (TextView) findViewById(R.id.lives);

        // Method which will set the things up for our game.
        setQuestionView();

        // A timer of 10 seconds to play for, with an interval of 1 second (1000 milliseconds).
        timer = new CounterClass(21000, 1000);
        timer.start();

        // Button click listeners.
        // Passing the button text to other method to check whether the answer is correct or not.
        // Same for all three buttons.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button1.getText().toString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button2.getText().toString());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer(button3.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Pause Game");
        alertDialog.setMessage("Would you like to exit the game?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            } });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
                timer = new CounterClass(millis, 1000);
                timer.start();
            }});

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Return to Main Menu", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(QuestionActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }});

        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /* -----Below is the code for handling the answer that user choose and the score----- */

    public void getAnswer(String AnswerString){
        timer.cancel();
        if (currentQ.getCORRECT().equals(AnswerString)){
            // If conditions matches increase the int (score) by 1 and set the text of the score view.
            score++;
            scored.setText("Score: " + score);
            correctAnswersCounter += 1;
            if(correctAnswersCounter == 5){
                livesCount += 1;
                lives.setText("Lives: " + livesCount);
            }
        } else {
            livesCount -= 1;
            // If unlucky start activity and finish the game.
            if (livesCount == 0){
                Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                // Passing the int value.
                Bundle b = new Bundle();
                b.putInt("score", score); // Your score.
                intent.putExtras(b); // Put your score to your next.
                startActivity(intent);
                finish();
                active = false;
                timer.cancel();
            } else {
                lives.setText("Lives: " + livesCount);
            }
        }
        timer = new CounterClass(21000, 1000);
        timer.start();

        if(qid<25){
            // If questions are not over then do this.
            currentQ = questionList.get(qid);
            setQuestionView();
        }
        else {
            // If over do this.
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("score", score); // Your score.
            intent.putExtras(b);
            startActivity(intent);
            finish();
            active=false;
        }
    }

    private void setQuestionView(){
        // The method which will put all things together.
        txtQuestion.setText("[" + currentQ.getCATEGORY() + "]" + " " + currentQ.getQUESTION());
        button1.setText(currentQ.getANSWER1());
        button2.setText(currentQ.getANSWER2());
        button3.setText(currentQ.getANSWER3());

        qid++;
    }

    /* -----The timer of the game----- */

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (active=true){
                Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                Bundle b = new Bundle();
                b.putInt("score", score);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                active=false;
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                                    .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                    .toMinutes(millis)));
            System.out.println(hms);
            times.setText(hms);
        }
    }

}
