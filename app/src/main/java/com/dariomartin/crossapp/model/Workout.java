package com.dariomartin.crossapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dariomartin.crossapp.model.enums.ExerciseType;
import com.dariomartin.crossapp.model.exercise.Exercise;
import com.dariomartin.crossapp.model.exercise.SessionExercise;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dariomartin on 11/9/17.
 */

public class Workout implements Parcelable {

    private String id;
    private String name;
    private String ownerId;
    private Date date;
    private long time;
    private List<Part> parts = new ArrayList<>();
    private boolean isPrivate;
    private float rating;

    public Workout() {

    }

    public Workout(String name) {
        this.name = name;
        parts = new ArrayList<>();
        isPrivate = true;
        rating = 0;
    }

    public Workout(WorkoutDB workoutDB) {
        id = workoutDB.getId();
        name = workoutDB.getName();
        ownerId = workoutDB.getOwnerId();
        parts = workoutDB.getParts();
        isPrivate = workoutDB.isPrivate();
    }

    public void addPart(Part part) {
        if (parts == null) {
            parts = new ArrayList<>();
        }
        parts.add(part);
    }

    public String getName() {
        return name;
    }

    public List<Part> getParts() {
        return parts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Part) {
            return ((Workout) obj).getId().equals(id);
        } else {
            return false;
        }
    }

    public void updateWithTrainingSession(TrainingSession trainingSession) {
        if (trainingSession != null) {
            rating = trainingSession.getRating();
            time = trainingSession.getDuration();
            date = new Date(trainingSession.getDate());
        }
    }

    public int getPoints() {
        int totalPoints = 0;
        for (Part part : parts) {
            int partPoints = 0;
            for (SessionExercise exercise : part.getExercises()) {
                partPoints += exercise.getReps();
            }
            totalPoints += partPoints * part.getDurationValue();
        }
        return totalPoints;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ownerId);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeLong(this.time);
        dest.writeTypedList(this.parts);
        dest.writeByte(this.isPrivate ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.rating);
    }

    protected Workout(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.ownerId = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.time = in.readLong();
        this.parts = in.createTypedArrayList(Part.CREATOR);
        this.isPrivate = in.readByte() != 0;
        this.rating = in.readFloat();
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel source) {
            return new Workout(source);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

}
