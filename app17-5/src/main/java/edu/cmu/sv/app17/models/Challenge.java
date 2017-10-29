package edu.cmu.sv.app17.models;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import java.awt.image.BufferedImage;

public class Challenge {
    String id = null;
    String challenegName, challengeDescription, challengeCreatedDate, challengeType ;


    String userId;

    public Challenge(String challenegName, String challengeDescription, String challengeCreatedDate, String challengeType,String userId) {
        this.challenegName = challenegName;
        this.challengeDescription = challengeDescription;
        this.challengeCreatedDate = challengeCreatedDate;
        this. challengeType = challengeType;

        this.userId = userId;



    }
    public void setId(String id) {
        this.id = id;
    }
}



