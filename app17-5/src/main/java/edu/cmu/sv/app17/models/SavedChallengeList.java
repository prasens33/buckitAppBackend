package edu.cmu.sv.app17.models;

public class SavedChallengeList {
    String id = null;
    String userId;
    String challengeId;
    String challengeName;



    public SavedChallengeList(String challengeId, String userId, String challengeName) {

        this.challengeId = challengeId;
        this.userId = userId;
        this.challengeName = challengeName;

    }


    public void setId(String id) {
        this.id = id;
    }
}
