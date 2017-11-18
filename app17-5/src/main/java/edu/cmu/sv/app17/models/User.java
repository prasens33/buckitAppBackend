package edu.cmu.sv.app17.models;

import java.awt.image.BufferedImage;

public class User {
    String id = null;
    String firstName;
    String lastName;
    String emailAddress;
    String profilePictureLink;
    int challengeIndex;


    Integer score;

    //String profilePictureLink;
    public User(String firstName, String lastName,
                  String emailAddress, Integer score, String profilePictureLink, Integer challengeIndex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.score = score;
        this.profilePictureLink = profilePictureLink;
        this.challengeIndex = challengeIndex;

    }


    public void setId(String id) {
        this.id = id;
    }
}
