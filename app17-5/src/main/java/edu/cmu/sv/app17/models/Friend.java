package edu.cmu.sv.app17.models;

public class Friend {
    String id = null;
    String userId;
    String userFirstName;
    String userLastName;
    String friendFirstName;
    String friendLastName;
    String friendId;


    public Friend(String userId, String friendId, String userFirstName, String userLastName, String friendFirstName, String friendLastName) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendId = friendId;

    }


    public void setId(String id) {
        this.id = id;
    }
}
