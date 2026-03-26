package com.example.myapplication;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String genreDuration;
    private int posterResId;
    private String trailerVideoId;
    private boolean isComingSoon;

    public Movie(String name, String genreDuration, int posterResId, String trailerVideoId, boolean isComingSoon) {
        this.name = name;
        this.genreDuration = genreDuration;
        this.posterResId = posterResId;
        this.trailerVideoId = trailerVideoId;
        this.isComingSoon = isComingSoon;
    }

    public String getName() {
        return name;
    }

    public String getGenreDuration() {
        return genreDuration;
    }

    public int getPosterResId() {
        return posterResId;
    }

    public String getTrailerVideoId() {
        return trailerVideoId;
    }

    public boolean isComingSoon() {
        return isComingSoon;
    }
}
