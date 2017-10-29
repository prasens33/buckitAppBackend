package edu.cmu.sv.app17.models;

public class ChallengeRequest {
    String id = null;
    String challengerId, challengeId;
    String challengeReceiverId;


    public ChallengeRequest(String challengerId, String challengeReceiverId, String challengeId) {
        this.challengerId = challengerId;
        this.challengeReceiverId = challengeReceiverId;
        this.challengeId = challengeId;

    }
    public void setId(String id) {
        this.id = id;
    }
}



