package com.dariomartin.crossapp.model.exercise;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by dariomartin on 6/9/17.
 */

public class Exercise implements Parcelable {

    protected String id;
    protected String name;

    public Exercise() {

    }

    public Exercise(String name) {
        this.name = name;
        this.id = buildId();
    }

    public Exercise(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id != null ? id : buildId();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    private String buildId() {
        return name != null ? name.replace(" ", "_").toLowerCase() : "unknown";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Exercise) {
            return ((Exercise) obj).getId().equals(id);
        } else {
            return false;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    protected Exercise(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
