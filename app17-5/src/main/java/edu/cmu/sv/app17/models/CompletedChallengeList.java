package edu.cmu.sv.app17.models;

public class CompletedChallengeList {
    String id = null;
    String userId;
    String challengeId;
    String challengeOwnerId;
    String challengeImageLink;
    String challengeName;



    public CompletedChallengeList(String challengeId, String userId, String challengeName, String challengeOwnerId, String challengeImageLink) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.challengeOwnerId = challengeOwnerId;
        this.challengeImageLink = challengeImageLink;

    }


    public void setId(String id) {
        this.id = id;
    }
}
