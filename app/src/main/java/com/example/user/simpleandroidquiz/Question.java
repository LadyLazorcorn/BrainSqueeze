package com.example.user.simpleandroidquiz;

import android.app.Activity;

/**
 * Created by user on 15/3/2017.
 */

public class Question {

    private int ID;
    private String QUESTION;
    private String ANSWER1;
    private String ANSWER2;
    private String ANSWER3;
    private String CORRECT;
    private String CATEGORY;

    public Question() {
        ID = 0;
        QUESTION = "";
        ANSWER1 = "";
        ANSWER2 = "";
        ANSWER3 = "";
        CORRECT = "";
    }

    public Question(String QUESTION, String ANSWER1, String ANSWER2, String ANSWER3,
                    String CORRECT, String CATEGORY){
        this.QUESTION = QUESTION;
        this.ANSWER1 = ANSWER1;
        this.ANSWER2 = ANSWER2;
        this.ANSWER3 = ANSWER3;
        this.CORRECT = CORRECT;
        this.CATEGORY = CATEGORY;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getANSWER1() {
        return ANSWER1;
    }

    public void setANSWER1(String ANSWER1) { this.ANSWER1 = ANSWER1; }

    public String getANSWER2() {
        return ANSWER2;
    }

    public void setANSWER2(String ANSWER2) {
        this.ANSWER2 = ANSWER2;
    }

    public String getANSWER3() {
        return ANSWER3;
    }

    public void setANSWER3(String ANSWER3) {
        this.ANSWER3 = ANSWER3;
    }

    public String getCORRECT() {
        return CORRECT;
    }

    public void setCORRECT(String CORRECT) {
        this.CORRECT = CORRECT;
    }

    public String getCATEGORY() { return CATEGORY; }

    public void setCATEGORY(String CATEGORY) { this.CATEGORY = CATEGORY; }
}
