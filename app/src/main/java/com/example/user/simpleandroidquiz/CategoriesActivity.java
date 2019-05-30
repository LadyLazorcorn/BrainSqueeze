package com.example.user.simpleandroidquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.simpleandroidquiz.Utilities.NetworkUtils;
import com.example.user.simpleandroidquiz.Utilities.QuizHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hitesh.asimplegame.R;

import static android.content.Context.MODE_PRIVATE;

public class CategoriesActivity extends AppCompatActivity {

    Spinner cat1Spinner, cat2Spinner, cat3Spinner, cat4Spinner, cat5Spinner;
    QuizHelper db;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        prefs = getSharedPreferences("com.example.user.simpleandroidquiz", MODE_PRIVATE);

        cat1Spinner = (Spinner) findViewById(R.id.category1_spinner);
        cat2Spinner = (Spinner) findViewById(R.id.category2_spinner);
        cat3Spinner = (Spinner) findViewById(R.id.category3_spinner);
        cat4Spinner = (Spinner) findViewById(R.id.category4_spinner);
        cat5Spinner = (Spinner) findViewById(R.id.category5_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.allCategories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cat1Spinner.setAdapter(adapter);
        cat2Spinner.setAdapter(adapter);
        cat3Spinner.setAdapter(adapter);
        cat4Spinner.setAdapter(adapter);
        cat5Spinner.setAdapter(adapter);

        readDatabaseRevision(); // Read database revision from firebase.
        db = new QuizHelper(this); // My question bank class.
        ArrayList<Question> list = (ArrayList<Question>) db.getAllQuestions();
        for (Question q: list){
            Log.i("question text: ", q.getQUESTION());
        }
    }


    @Override
    public void onBackPressed() {
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
            }});

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Return to Main Menu", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(CategoriesActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }});

        alertDialog.show();
    }

    public void next(View niew){
        String category1 = cat1Spinner.getSelectedItem().toString();
        String category2 = cat2Spinner.getSelectedItem().toString();
        String category3 = cat3Spinner.getSelectedItem().toString();
        String category4 = cat4Spinner.getSelectedItem().toString();
        String category5 = cat5Spinner.getSelectedItem().toString();

        if (category1.equals(category2)
                || category1.equals(category3)
                || category1.equals(category4)
                || category1.equals(category5)
                || category2.equals(category3)
                || category2.equals(category4)
                || category2.equals(category5)
                || category3.equals(category4)
                || category3.equals(category5)
                || category4.equals(category5)) {

            Toast.makeText(this, "Please do not select one category twice", Toast.LENGTH_SHORT).show();

        } else {

            ArrayList<String> catArray = new ArrayList<String>();
            catArray.add(category1);
            catArray.add(category2);
            catArray.add(category3);
            catArray.add(category4);
            catArray.add(category5);

            Intent intent = new Intent(this, QuestionActivity.class);
            Bundle b = new Bundle();
            b.putStringArrayList("selectedCategories", catArray);
            intent.putExtras(b);
            startActivity(intent);
            finish();

        }
    }

    /* -----Below is the code for reading Database Revision from firebase using Http GET----- */

    private void readDatabaseRevision() {
        URL FirebaseRevisionURL = NetworkUtils.buildRevisionUrl();
        new FirebaseRevisionTask().execute(FirebaseRevisionURL);
    }

    public class FirebaseRevisionTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                String previousRevision = loadRevisionFromSharedPreferences();
                int revision = 0;
                try {
                    JSONObject revObj = new JSONObject(result);
                    revision = revObj.getInt("Revision");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String revisionStr = Integer.toString(revision);
                boolean same = revisionStr.equals(previousRevision);
                Log.i("same revisions", String.valueOf(same));
                if(same == false){
                    saveRevision("FirebaseDbaseRevision", revisionStr);
                    db.deleteAllRecords();
                    readQuestionsFromFirebase();
                }
            }
            else{
                Log.i("Error", "Couldn't read revision.");
            }
        }
    }

    /* -----Below is the code for reading Database Questions from firebase using Http GET and putting them in SQLite----- */

    private void readQuestionsFromFirebase() {
        URL FirebaseQuestionsURL = NetworkUtils.buildQuestionsUrl();
        new FirebaseQueryTask().execute(FirebaseQuestionsURL);
    }

    public class FirebaseQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String questions = null;
            try {
                questions = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return questions;
        }

        @Override
        protected void onPostExecute(String questions) {
            if (questions != null && !questions.equals("")) {
                Log.i("Success", "Successful Reading of Questions from Firebase.");
                try {
                    JSONObject questionsObj = new JSONObject(questions);
                    Iterator<String> questionsKeys = questionsObj.keys();
                    while(questionsKeys.hasNext()){
                        String questionKey = (String) questionsKeys.next();
                        if(questionsObj.get(questionKey) instanceof JSONObject){
                            JSONObject questionObj = (JSONObject) questionsObj.get(questionKey);
                            String category = questionObj.getString("Category");
                            String question = questionObj.getString("Question");
                            String correctAnswer = questionObj.getString("CorrectAnswer");
                            String firstAnswer = questionObj.getString("FirstAnswer");
                            String secondAnswer = questionObj.getString("SecondAnswer");
                            String thirdAnswer = questionObj.getString("ThirdAnswer");

                            Log.i("added question", "success");
                            Question q = new Question(question, firstAnswer, secondAnswer, thirdAnswer,
                                    correctAnswer, category);
                            db.addQuestion(q);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.close();
            }
            else{
                Log.e("Error", "Couldn't read questions.");
            }
        }
    }

    /* -----Store Firebase Database revision and read it every time the app launches----- */

    private String loadRevisionFromSharedPreferences(){
        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
        String revision = sharedPreferences.getString("FirebaseDbaseRevision", "");
        return revision;
    }

    private void saveRevision(String key, String value){
        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
