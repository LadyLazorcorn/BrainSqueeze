package com.example.user.simpleandroidquiz;

/**
 * Created by user on 19/12/2017.
 */

public class Player {

    private String username;
    private String Uid;
    private Long score;

    public Player(String username, String Uid, Long Score){
        this.username = username;
        this.Uid = Uid;
        this.score = Score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
