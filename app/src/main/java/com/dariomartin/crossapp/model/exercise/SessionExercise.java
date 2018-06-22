package com.dariomartin.crossapp.model.exercise;

import android.os.Parcel;

/**
 * Created by dariomartin on 26/9/17.
 */

public class SessionExercise extends Exercise {

    private int reps;
    private int intensityValue;
    private String intensityUnit;

    public SessionExercise() {
        super();
    }

    public SessionExercise(String id, String name) {
        super(id, name);
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getIntensityValue() {
        return intensityValue;
    }

    public void setIntensityValue(int intensityValue) {
        this.intensityValue = intensityValue;
    }

    public String getIntensityUnit() {
        return intensityUnit;
    }

    public void setIntensityUnit(String intensityUnit) {
        this.intensityUnit = intensityUnit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.reps);
        dest.writeInt(this.intensityValue);
        dest.writeString(this.intensityUnit);
    }

    protected SessionExercise(Parcel in) {
        super(in);
        this.reps = in.readInt();
        this.intensityValue = in.readInt();
        this.intensityUnit = in.readString();
    }

    public static final Creator<SessionExercise> CREATOR = new Creator<SessionExercise>() {
        @Override
        public SessionExercise createFromParcel(Parcel source) {
            return new SessionExercise(source);
        }

        @Override
        public SessionExercise[] newArray(int size) {
            return new SessionExercise[size];
        }
    };
}
