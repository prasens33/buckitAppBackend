package edu.cmu.sv.app17.models;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import java.awt.image.BufferedImage;

public class Challenge {
    String id = null;
    String challengeName, challengeDescription, challengeCreatedDate, challengeType, ownerChallengeImageLink ;


    String userId;

    public Challenge(String challengeName, String challengeDescription, String challengeCreatedDate, String challengeType, String ownerChallengeImageLink, String userId) {
        this.challengeName = challengeName;
        this.challengeDescription = challengeDescription;
        this.challengeCreatedDate = challengeCreatedDate;
        this.challengeType = challengeType;
        this. ownerChallengeImageLink = ownerChallengeImageLink;

        this.userId = userId;



    }
    public void setId(String id) {
        this.id = id;
    }
}



