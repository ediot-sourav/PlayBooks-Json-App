package com.magicmusic.playbooks;

public class PlayBooks {

        private String mName;
    private String mBio;
    private String mLanguage;
    private String mDate;
    private String mUrl;
    private String mImage;


    public PlayBooks(String name, String bio, String date, String language, String url) {
        this.mName = name;
        this.mBio = bio;
        this.mLanguage = language;
        this.mDate = date;
        this.mUrl = url;
//        this.mImage = image;
    }

    public String getName() {
        return mName;
    }

    public String getBio() {
        return mBio;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImage(){return mImage;}
}
