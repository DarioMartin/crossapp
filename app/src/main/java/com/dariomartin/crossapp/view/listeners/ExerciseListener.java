package com.dariomartin.crossapp.view.listeners;

import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.Exercise;

/**
 * Created by dariomartin on 27/10/17.
 */

public interface ExerciseListener {
    void onExerciseSelected(DetailedExercise exercise);

    void onAddExerciseSelected(Exercise exercise);
}
