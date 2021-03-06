package com.dariomartin.crossapp.view.listeners;

import com.dariomartin.crossapp.model.Workout;

/**
 * Created by dariomartin on 27/10/17.
 */

public interface WorkoutListener {
    void onWorkoutSelected(Workout workout, boolean itsNew);

    void addNewWorkout(boolean isPrivate);
}
