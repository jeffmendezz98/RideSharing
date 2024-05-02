package edu.uga.cs.ridesharing.DB;

public class RequestsModel {
    private int id;
    private String date;
    private String destination;
    private int userID;

    private Boolean hasNotBeenAccepted;

    public RequestsModel(int id, String date, String destination, int userID, boolean hasNotBeenAccepted) {
        this.id = id;
        this.date = date;
        this.destination = destination;
        this.userID = userID;
        this.hasNotBeenAccepted = hasNotBeenAccepted;
    }

    public RequestsModel(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Boolean getHasNotBeenAccepted() {
        return hasNotBeenAccepted;
    }

    public void setHasNotBeenAccepted(Boolean hasNotBeenAccepted) {
        this.hasNotBeenAccepted = hasNotBeenAccepted;
    }
}
