package edu.uga.cs.ridesharing.driverdata;

public class OffersModel {
    private int id;
    private String date;
    private String destination;
    private int userID;

    public OffersModel(int id, String date, String destination, int userID) {
        this.id = id;
        this.date = date;
        this.destination = destination;
        this.userID = userID;
    }

    public OffersModel(){
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
}
