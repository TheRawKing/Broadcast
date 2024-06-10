package com.therawking.broadcast;

public class ChannelModel {
    boolean Admin;
    String channelID, channelName, channelDP;

    public ChannelModel(boolean admin, String channelID, String channelName, String channelDP) {
        Admin = admin;
        this.channelID = channelID;
        this.channelName = channelName;
        this.channelDP = channelDP;
    }

    public ChannelModel() {
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
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

    public String getChannelDP() {
        return channelDP;
    }

    public void setChannelDP(String channelDP) {
        this.channelDP = channelDP;
    }
}
