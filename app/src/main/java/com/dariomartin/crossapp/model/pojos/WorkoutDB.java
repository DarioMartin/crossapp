package com.dariomartin.crossapp.model.pojos;

import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dariomartin on 11/9/17.
 */

public class WorkoutDB {

    private String id;
    private String name;
    private String ownerId;
    private List<Part> parts;
    private boolean isPrivate;

    public WorkoutDB() {
        parts = new ArrayList<>();
    }

    public WorkoutDB(Workout workout) {
        id = workout.getId();
        name = workout.getName();
        ownerId = workout.getOwnerId();
        isPrivate = workout.isPrivate();
        parts = workout.getParts();
    }

    public String getId() {
        return id;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Part> parts) {
        this.parts = parts;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void addPart(Part partId) {
        parts.add(partId);
    }

    public void removePart(String partId) {
        parts.remove(partId);
    }

    public Workout getWorkout() {
        Workout workout = new Workout();
        workout.setId(id);
        workout.setName(name);
        workout.setOwnerId(ownerId);
        workout.setPrivate(isPrivate);
        workout.setParts(parts);
        return workout;
    }
}
