package edu.cmu.sv.app17.models;

public class ProfileImage {
    String id = null;
    String challengeImageLink;


    String userId;

    public ProfileImage(String challengeImageLink, String userId) {

        this.challengeImageLink = challengeImageLink;
        this.userId = userId;


    }
    public void setId(String id) {
        this.id = id;
    }
}



