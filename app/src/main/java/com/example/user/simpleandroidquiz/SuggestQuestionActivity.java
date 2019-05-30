package com.example.user.simpleandroidquiz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hitesh.asimplegame.R;

public class SuggestQuestionActivity extends AppCompatActivity {

    EditText questionFieldText, answer1FieldText, answer2FieldText, answer3FieldText, correctAnswerFieldText;
    Spinner selectCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_question);

        questionFieldText = (EditText) findViewById(R.id.QuestionField);
        answer1FieldText = (EditText) findViewById(R.id.Answer1Field);
        answer2FieldText = (EditText) findViewById(R.id.Answer2Field);
        answer3FieldText = (EditText) findViewById(R.id.Answer3Field);
        correctAnswerFieldText = (EditText) findViewById(R.id.CorrectAnswerField);
        selectCategorySpinner = (Spinner) findViewById(R.id.select_category_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.allCategories, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectCategorySpinner.setAdapter(adapter);
    }

    public void sendPotentialQuestion(View view){
        String question = questionFieldText.getText().toString();
        String answer1 = answer1FieldText.getText().toString();
        String answer2 = answer2FieldText.getText().toString();
        String answer3 = answer3FieldText.getText().toString();
        String correctAnswer = correctAnswerFieldText.getText().toString();
        String category = selectCategorySpinner.getSelectedItem().toString();

        if( question.equals("")|| answer1.equals("") || answer2.equals("") || answer3.equals("") || correctAnswer.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(SuggestQuestionActivity.this).create();

            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Fill in all the fields");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("PotentialQuestions").child(question).child("FirstAnswer").setValue(answer1);
            mDatabase.child("PotentialQuestions").child(question).child("SecondAnswer").setValue(answer2);
            mDatabase.child("PotentialQuestions").child(question).child("ThirdAnswer").setValue(answer3);
            mDatabase.child("PotentialQuestions").child(question).child("CorrectAnswer").setValue(correctAnswer);
            mDatabase.child("PotentialQuestions").child(question).child("Category").setValue(category);

            /* ----Another way to write on Firebase----
            DatabaseReference answer1Ref = database.getReference("PotentialQuestions/" + question + "/answer1");
            DatabaseReference answer2Ref = database.getReference("PotentialQuestions/" + question + "/answer2");
            DatabaseReference answer3Ref = database.getReference("PotentialQuestions/" + question + "/answer3");
            DatabaseReference correctAnswerRef = database.getReference("PotentialQuestions/" + question + "/correctAnswer");
            DatabaseReference categoryRef = database.getReference("PotentialQuestions/" + question + "/category");

            answer1Ref.setValue(answer1);
            answer2Ref.setValue(answer2);
            answer3Ref.setValue(answer3);
            correctAnswerRef.setValue(correctAnswer);
            categoryRef.setValue(category);
            */

            Toast.makeText(this, "Your question was sent successfully.", Toast.LENGTH_SHORT).show();
            questionFieldText.setText(null);
            answer1FieldText.setText(null);
            answer2FieldText.setText(null);
            answer3FieldText.setText(null);
            correctAnswerFieldText.setText(null);
        }
    }

}
