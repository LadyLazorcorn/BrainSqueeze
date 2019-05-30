package com.example.user.simpleandroidquiz.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.simpleandroidquiz.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16/3/2017.
 */

public class QuizHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "quizDatabase"; // Database name.
    private static final String TABLE_QUEST = "quest"; // Tasks table columns names.

    private static final String KEY_ID = "qid";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer"; // Correct option.
    private static final String KEY_ANSW1 = "answ1"; // Option a.
    private static final String KEY_ANSW2 = "answ2"; // Option b.
    private static final String KEY_ANSW3 = "answ3";// Option c.
    private static final String KEY_CATEGORY = "category"; // Category of question.

    private static SQLiteDatabase dbase;

    public QuizHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        dbase = db;
        String SQL = "CREATE TABLE IF NOT EXISTS "
                + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_QUES + " TEXT, "
                + KEY_ANSWER + " TEXT, "
                + KEY_ANSW1 + " TEXT, "
                + KEY_ANSW2 + " TEXT, "
                + KEY_ANSW3 + " TEXT, "
                + KEY_CATEGORY + " TEXT)";
        db.execSQL(SQL);
        //db.close();
    }

    /*public void addQuestions(){
        Question q1 = new Question("Who was the first president of the USA?", "George Washington", "John Adams",
        "Abraham Lincoln", "George Washington", "History");
        this.addQuestion(q1);
        Question q2 = new Question("What is the national animal of China?", "Koala", "Giant panda", "Kangaroo",
        "Giant panda", "Animals");
        this.addQuestion(q2);
        Question q3 = new Question("What is the largest state of the United States?", "California", "Kansas",
        "Alaska", "Alaska", "Geography");
        this.addQuestion(q3);
        Question q4 = new Question("On which continent can you visit Sierra Leone?", "America", "Africa", "Asia",
        "Africa", "Geography");
        this.addQuestion(q4);
        Question q5 = new Question("Which planet is nearest the sun?", "Mercury", "Earth", "Aphrodite", "Mercury",
        "Space");
        this.addQuestion(q5);
        Question q6 = new Question("Who discovered one of the first antibiotics: penicillin ?", "Alexander Fleming",
        "James Watson", "Louis Pasteur", "Alexander Fleming", "Science");
        this.addQuestion(q6);
        Question q7 = new Question("Who was the first American in space?", "Neil Armstrong", "Michael Collins",
        "Alan Shepard", "Alan Shepard", "Space");
        this.addQuestion(q7);
        Question q8 = new Question("In computing what is Ram short for?", "Random Access Memory",
        "Random Acceleration Memory", "Reading Access Memory", "Random Access Memory", "Technology");
        this.addQuestion(q8);
        Question q9 = new Question("Which unit is an indication for the sound quality of MP3?", "Decibel (dB)",
        "Kilobits per second (Kbps)", "Herz (Hz)", "Kilobits per second (Kbps)", "Technology");
        this.addQuestion(q9);
        Question q10 = new Question("Which country is the largest producer of olive oil?", "Greece", "Italy",
        "Spain", "Spain", "Geography");
        this.addQuestion(q10);
        Question q11 = new Question("Which country is the origin of the car brands Daewoo and Kia?", "Japan",
        "South Korea", "China", "South Korea", "Business");
        this.addQuestion(q11);
        Question q12 = new Question("In which country happened the Orange Revolution between 2004-2005?",
        "France", "USA", "Ukraine", "Ukraine", "History");
        this.addQuestion(q12);
        Question q13 = new Question("In what year was the Berlin wall built?", "1958", "1963", "1961",
        "1961", "History");
        this.addQuestion(q13);
        Question q14 = new Question("Who is the director of the X-files?", "David Lynch", "Rob Bowman",
        "David Cronenberg", "Rob Bowman", "Movies");
        this.addQuestion(q14);
        Question q15 = new Question("Which Italian film director is considered as the father of the spaghetti western?",
        "Federico Fellini", "Michelangelo Antonioni", "Sergio Leone", "Sergio Leone", "Movies");
        this.addQuestion(q15);
        Question q16 = new Question("In which city did Romeo and Julia live?", "Rome", "Venice", "Verona",
         "Verona", "Literature");
        this.addQuestion(q16);
        Question q17 = new Question("Which country is the origin of the Stella beer?", "Belgium",
         "Greece", "Germany", "Belgium", "Business");
        this.addQuestion(q17);
        Question q18 = new Question("In which city is the composer Frιdιric Chopin buried?",
        "Berlin", "Paris", "Vienna", "Paris", "History");
        this.addQuestion(q18);
        Question q19 = new Question("In which city is the oldest zoo in the world?", "London",
        "Vienna", "Paris", "Vienna", "Animals");
        this.addQuestion(q19);
        Question q20 = new Question("What is the noisy invention of Louis Glass in 1890 called?",
        "Electric guitar", "Jukebox", "Radio", "Jukebox", "Science");
        this.addQuestion(q20);
        Question q21 = new Question("What was the Olympic city of 1992 ?", "Atlanta", "Barcelona",
        "Seoul", "Barcelona", "History");
        this.addQuestion(q21);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST); // Drop older table if existed.
        onCreate(db); // Create tables again.
    }

    public void deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUEST, null, null);
        //db.execSQL("delete from " + TABLE_QUEST);
        db.close();
    }

    // Adding new question.
    public void addQuestion(Question quest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getCORRECT());
        values.put(KEY_ANSW1, quest.getANSWER1());
        values.put(KEY_ANSW2, quest.getANSWER2());
        values.put(KEY_ANSW3, quest.getANSWER3());
        values.put(KEY_CATEGORY, quest.getCATEGORY());

        db.insert(TABLE_QUEST, null, values); // Inserting Row.
        db.close();
    }

    public List<Question> getQuestionsFrom(String categoryName){
        List<Question> questionList = new ArrayList<Question>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST +
                " WHERE " + KEY_CATEGORY + " = " + " '" + categoryName + "'"; // Select from specific category.
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // Looping through all rows and adding to list.
        if (cursor.moveToFirst()){
            do {
                Question question = new Question();
                question.setID(cursor.getInt(0));
                question.setQUESTION(cursor.getString(1));
                question.setCORRECT(cursor.getString(2));
                question.setANSWER1(cursor.getString(3));
                question.setANSWER2(cursor.getString(4));
                question.setANSWER3(cursor.getString(5));
                question.setCATEGORY(cursor.getString(6));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList; // Return question list.
    }

    public List<Question> getAllQuestions(){
        List<Question> questionList = new ArrayList<Question>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // Looping through all rows and adding to list.
        if (cursor.moveToFirst()){
            do {
                Question question = new Question();
                question.setID(cursor.getInt(0));
                question.setQUESTION(cursor.getString(1));
                question.setCORRECT(cursor.getString(2));
                question.setANSWER1(cursor.getString(3));
                question.setANSWER2(cursor.getString(4));
                question.setANSWER3(cursor.getString(5));
                question.setCATEGORY(cursor.getString(6));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList; // Return question list.
    }

}
