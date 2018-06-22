package com.dariomartin.crossapp.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.SessionExercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dariomartin on 30/10/17.
 */

public class Utils {
    public static List<String> getExercisesDescription(Part part) {
        List<String> exercises = new ArrayList<>();
        for (SessionExercise exercise : part.getExercises()) {
            exercises.add(exercise.getReps() + " " +
                    exercise.getName() + " " +
                    exercise.getIntensityValue() +
                    exercise.getIntensityUnit());
        }
        return exercises;
    }

    public static Drawable getWorkoutImage(Workout workout, Context context) {
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.workout_backgrounds);
        int position = Math.abs(workout.getId().hashCode()) % imgs.length();
        Drawable background = context.getResources().getDrawable(imgs.getResourceId(position, 0));
        imgs.recycle();
        return background;
    }
}
