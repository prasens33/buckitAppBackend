package edu.cmu.sv.app17.models;

public class ReceivedChallengeList {
    String id = null;
    String userId;
    String challengeId;



    public ReceivedChallengeList( String challengeId, String userId) {
        this.userId = userId;
        this.challengeId = challengeId;

    }


    public void setId(String id) {
        this.id = id;
    }
}
