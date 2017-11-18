package edu.cmu.sv.app17.models;

public class DiscardedChallengeList {
    String id = null;
    String userId;
    String challengeId;
    String challengeName;



    public DiscardedChallengeList(String challengeId, String userId, String challengeName) {

        this.challengeId = challengeId;
        this.userId = userId;
        this.challengeName = challengeName;

    }


    public void setId(String id) {
        this.id = id;
    }
}
