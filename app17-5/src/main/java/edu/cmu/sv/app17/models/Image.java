package edu.cmu.sv.app17.models;

public class Image {
    String id = null;
    String challengeId,challengeImageLink;


    String userId;

    public Image(String challengeId, String challengeImageLink, String userId) {
        this.challengeId = challengeId;
        this.challengeImageLink = challengeImageLink;
        this.userId = userId;


    }
    public void setId(String id) {
        this.id = id;
    }
}



