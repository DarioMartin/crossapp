package com.dariomartin.crossapp.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by dariomartin on 2/11/17.
 */

public class TrainingSession implements Comparable<TrainingSession> {

    private long date;
    private String userId;
    private String workoutId;
    private long duration;
    private float rating;
    private int points;

    public TrainingSession() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(@NonNull TrainingSession other) {
        return Long.compare(this.date, other.getDate());
    }
}
