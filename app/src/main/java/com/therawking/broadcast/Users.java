package com.therawking.broadcast;

public class Users {
    String UserID,EMail,Password, Name,ImageURI;

    public Users(String UserID, String EMail, String Password, String Username, String ImageURI){
        this.UserID = UserID;
        this.EMail = EMail;
        this.Password = Password;
        this.Name = Username;
        this.ImageURI = ImageURI;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURI() { return ImageURI; }

    public void setImageURI(String imageURI) { ImageURI = imageURI; }

}
