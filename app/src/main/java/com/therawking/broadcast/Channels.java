package com.therawking.broadcast;

public class Channels {

    String channelAdmins, channelID, channelName, channelDescription, channelDP, creationTime, channelMembers;

    public Channels() {

    }

    public Channels(String channelAdmins, String channelID, String channelName, String channelDescription, String channelDP, String creationTime, String channelMembers) {
        this.channelAdmins = channelAdmins;
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.channelDP = channelDP;
        this.creationTime = creationTime;
        this.channelMembers = channelMembers;
    }

    public String getChannelMembers() { return channelMembers; }
    public void setChannelMembers(String channelMembers) { this.channelMembers = channelMembers; }
    public String getChannelAdmins() {
        return channelAdmins;
    }
    public void setChannelAdmins(String channelAdmins) {
        this.channelAdmins = channelAdmins;
    }
    public String getChannelID() {
        return channelID;
    }
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getChannelDescription() {
        return channelDescription;
    }
    public void setChannelDescription(String channelDescription) { this.channelDescription = channelDescription; }
    public String getChannelDP() {
        return channelDP;
    }
    public void setChannelDP(String channelDP) {
        this.channelDP = channelDP;
    }
    public String getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

}