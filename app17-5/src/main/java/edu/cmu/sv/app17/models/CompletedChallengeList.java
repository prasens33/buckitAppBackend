package edu.cmu.sv.app17.models;

public class CompletedChallengeList {
    String id = null;
    String userId;
    String challengeId;



    public CompletedChallengeList(String challengeId, String userId) {
        this.userId = userId;
        this.challengeId = challengeId;

    }


    public void setId(String id) {
        this.id = id;
    }
}
