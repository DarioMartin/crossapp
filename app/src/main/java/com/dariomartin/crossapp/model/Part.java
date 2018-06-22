package com.dariomartin.crossapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.dariomartin.crossapp.model.enums.PartDurationUnit;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.SessionExercise;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by dariomartin on 11/9/17.
 */

public class Part implements Parcelable {

    private String id;
    private String name;
    private int durationValue;
    private PartDurationUnit durationUnit;
    private ArrayList<SessionExercise> exercises;

    @Exclude
    private int currentRound;

    public Part() {
        durationValue = 1;
        durationUnit = PartDurationUnit.ROUNDS_FOR_TIME;
        currentRound = 0;
        exercises = new ArrayList<>();
    }

    public Part(String name) {
        this();
        this.name = name;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public void setDurationUnit(PartDurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public void addExercise(SessionExercise exercise) {
        exercises.add(exercise);
    }

    public ArrayList<SessionExercise> nextRound() {
        currentRound++;
        if (isFinished()) {
            return new ArrayList<>();
        } else {
            return prepareExercises();
        }
    }

    private ArrayList<SessionExercise> prepareExercises() {
        ArrayList<SessionExercise> roundExercises = new ArrayList<>();

        if (exercises != null) {
            for (SessionExercise exercise : exercises) {
                int repsForRound = getRepsForRound();
                if (repsForRound != -1) exercise.setReps(repsForRound);
                roundExercises.add(exercise);
            }
        }

        return roundExercises;
    }

    private int getTotalRounds() {
        switch (durationUnit) {
            case PROGRESSION_21_15_9:
                return 3;
            case IN_MINUTES:
                return 1;
            default:
                return durationValue;
        }
    }

    private int getRepsForRound() {
        switch (durationUnit) {
            case COUNTDOWN:
                return durationValue - (currentRound - 1);
            case COUNT:
                return currentRound;
            case PROGRESSION_21_15_9:
                return get21159Reps();
            default:
                return -1;
        }
    }

    private int get21159Reps() {
        switch (currentRound) {
            case 1:
                return 21;
            case 2:
                return 15;
            default:
                return 9;
        }
    }

    public String getName() {
        return name;
    }

    public int getDurationValue() {
        return durationValue;
    }

    public PartDurationUnit getDurationUnit() {
        return durationUnit;
    }

    public ArrayList<SessionExercise> getExercises() {
        return exercises != null ? exercises : new ArrayList<SessionExercise>();
    }

    @Exclude
    public int getCurrentRound() {
        return currentRound;
    }

    public String getId() {
        return id;
    }

    public void removeExercise(DetailedExercise exercise) {
        exercises.remove(exercise);
    }


    @Exclude
    public boolean isFinished() {
        return currentRound > getTotalRounds();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.durationValue);
        dest.writeInt(this.durationUnit == null ? -1 : this.durationUnit.ordinal());
        dest.writeInt(this.currentRound);
        dest.writeTypedList(this.exercises);
    }

    protected Part(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.durationValue = in.readInt();
        int tmpDurationUnit = in.readInt();
        this.durationUnit = tmpDurationUnit == -1 ? null : PartDurationUnit.values()[tmpDurationUnit];
        this.currentRound = in.readInt();
        this.exercises = in.createTypedArrayList(SessionExercise.CREATOR);
    }

    public static final Parcelable.Creator<Part> CREATOR = new Parcelable.Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel source) {
            return new Part(source);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
        }
    };


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Part) {
            return ((Part) obj).getId().equals(id);
        } else {
            return false;
        }
    }
}
