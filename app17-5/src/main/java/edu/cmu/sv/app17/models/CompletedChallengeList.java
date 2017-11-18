package edu.cmu.sv.app17.models;

public class CompletedChallengeList {
    String id = null;
    String userId;
    String challengeId;
    String challengeName;



    public CompletedChallengeList(String challengeId, String userId, String challengeName) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.challengeName = challengeName;

    }


    public void setId(String id) {
        this.id = id;
    }
}
