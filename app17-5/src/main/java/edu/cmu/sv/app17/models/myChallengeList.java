package edu.cmu.sv.app17.models;

public class myChallengeList {
    String id = null;
    String userId;
    int challengeIndex;


    public myChallengeList(String userId, int challengeIndex) {

        this.userId = userId;
        this.challengeIndex = challengeIndex;

    }


    public void setId(String id) {
        this.id = id;
    }
}
