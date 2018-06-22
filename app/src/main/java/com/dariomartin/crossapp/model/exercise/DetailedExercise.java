package com.dariomartin.crossapp.model.exercise;

import android.os.Parcel;

import com.dariomartin.crossapp.model.enums.Equipment;
import com.dariomartin.crossapp.model.enums.ExerciseType;
import com.dariomartin.crossapp.model.enums.Muscle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dariomartin on 26/9/17.
 */

public class DetailedExercise extends Exercise {

    private String description;
    private List<Muscle> muscles;
    private List<Equipment> equipment;
    private String videoUrl;
    private ArrayList<String> images;
    private ExerciseType type;

    public DetailedExercise() {
        super();
    }

    public DetailedExercise(String name) {
        super(name);
        muscles = new ArrayList<>();
        equipment = new ArrayList<>();
        images = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Muscle> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<Muscle> muscles) {
        this.muscles = muscles;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.description);
        dest.writeList(this.muscles);
        dest.writeList(this.equipment);
        dest.writeString(this.videoUrl);
        dest.writeStringList(this.images);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected DetailedExercise(Parcel in) {
        super(in);
        this.description = in.readString();
        this.muscles = new ArrayList<>();
        in.readList(this.muscles, Muscle.class.getClassLoader());
        this.equipment = new ArrayList<>();
        in.readList(this.equipment, Equipment.class.getClassLoader());
        this.videoUrl = in.readString();
        this.images = in.createStringArrayList();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : ExerciseType.values()[tmpType];
    }

    public static final Creator<DetailedExercise> CREATOR = new Creator<DetailedExercise>() {
        @Override
        public DetailedExercise createFromParcel(Parcel source) {
            return new DetailedExercise(source);
        }

        @Override
        public DetailedExercise[] newArray(int size) {
            return new DetailedExercise[size];
        }
    };
}
