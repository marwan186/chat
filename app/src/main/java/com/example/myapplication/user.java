package com.example.myapplication;

public class user {
    private String username;
    private String email;
    private String profilepicture;

    public user(){

    }

    public user(String username, String email, String profilepicture) {
        this.username = username;
        this.email = email;
        this.profilepicture = profilepicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }
}
