package edu.cmu.sv.app17.models;

public class SavedChallengeList {
    String id = null;
    String userId;
    String challengeId;



    public SavedChallengeList(String challengeId, String userId) {

        this.challengeId = challengeId;
        this.userId = userId;

    }


    public void setId(String id) {
        this.id = id;
    }
}
