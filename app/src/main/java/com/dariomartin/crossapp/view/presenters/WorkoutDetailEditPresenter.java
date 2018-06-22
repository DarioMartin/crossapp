package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.SessionExercise;
import com.dariomartin.crossapp.view.fragments.WorkoutDetailEditFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by dariomartin on 27/9/17.
 */

public class WorkoutDetailEditPresenter {

    private IDataBaseController dataBaseController;
    private WorkoutDetailEditFragment view;
    private Workout workout;

    public WorkoutDetailEditPresenter(WorkoutDetailEditFragment view, Workout workout) {
        this.view = view;
        this.workout = workout;
        dataBaseController = DataBaseController.getInstance();
    }

    public void saveWorkout() {
        workout = dataBaseController.addNewPrivateWorkout(workout);
    }

    public void deleteWorkout() {
        dataBaseController.removeUserWorkout(workout);
    }

    public void getParts() {
        for (Part part : workout.getParts()) {
            updatePartExercises(part);
        }
        view.updateParts(workout.getParts());
    }

    public void updatePartExercises(final Part part) {
        for (final SessionExercise partExercise : part.getExercises()) {
            dataBaseController.getExercisesById(partExercise.getId(), new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    DetailedExercise exercise = dataSnapshot.getValue(DetailedExercise.class);
                    partExercise.setName(exercise.getName());
                    saveWorkout();
                    view.updateParts(workout.getParts());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    DetailedExercise exercise = dataSnapshot.getValue(DetailedExercise.class);
                    part.removeExercise(exercise);
                    saveWorkout();
                    view.updateParts(workout.getParts());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }

    }

    public boolean isAnEmptyWorkout() {
        for (Part part : workout.getParts()) {
            if (part.getExercises() != null && !part.getExercises().isEmpty()) return false;
        }
        return true;
    }

    public boolean allPartsAreComplete() {
        for (Part part : workout.getParts()) {
            if (part.getExercises() == null || part.getExercises().isEmpty()) return false;
        }
        return true;
    }
}
