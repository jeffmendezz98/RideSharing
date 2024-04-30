package edu.uga.cs.ridesharing.DB;

public class UserModel {
    private int id;
    private String name;
    private String email;
    private String password;
    private int points;


    public UserModel(int id, String name, String email, String password, int points) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public UserModel(){
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", points=" + points +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
