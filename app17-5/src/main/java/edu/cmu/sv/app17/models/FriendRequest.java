package edu.cmu.sv.app17.models;


public class FriendRequest {
    String id = null;
    String senderId, receiverId;
    String senderFirstName, senderLastName, receiverFirstName, receiverLastName ;
    Boolean requestStatus;



    public FriendRequest( String receiverId, String receiverFirstName, String receiverLastName,String senderId, String senderFirstName, String senderLastName, Boolean requestStatus) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.requestStatus = requestStatus;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.receiverLastName = receiverLastName;
        this.receiverFirstName = receiverFirstName;


    }
    public void setId(String id) {
        this.id = id;
    }
}



