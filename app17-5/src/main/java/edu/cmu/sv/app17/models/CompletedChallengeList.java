package edu.cmu.sv.app17.models;

public class CompletedChallengeList {
    String id = null;
    String userId;
    String challengeId;
    String challengeOwnerId;
    String challengeImageLink;
    String challengeName;
    String challengeDescription;



    public CompletedChallengeList(String challengeId, String userId, String challengeName, String challengeOwnerId, String challengeImageLink, String challengeDescription) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.challengeOwnerId = challengeOwnerId;
        this.challengeImageLink = challengeImageLink;
        this.challengeDescription = challengeDescription;
    }


    public void setId(String id) {
        this.id = id;
    }
}
