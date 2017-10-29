package edu.cmu.sv.app17.models;


public class Notification {
    String id = null;
    String notificationType;
    String userId;



    public Notification(String notificationType, String userId) {
        this.notificationType = notificationType;
        this.userId = userId;


    }
    public void setId(String id) {
        this.id = id;
    }
}



