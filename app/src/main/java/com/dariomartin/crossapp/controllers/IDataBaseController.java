package com.dariomartin.crossapp.controllers;

import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dariomartin on 22/10/17.
 */

public interface IDataBaseController {

    //WORKOUTS

    Workout addNewPrivateWorkout(Workout workout);

    String addNewPublicWorkout(Workout workout);

    void updateWorkout(Workout workout);

    void removeUserWorkout(Workout workout);

    void removeWorkout(Workout workout);

    void getAllWorkouts(ValueEventListener valueEventListener);

    void getWorkoutById(String id, ValueEventListener valueEventListener);

    void getPublicWorkouts(ValueEventListener valueEventListener);

    void getUserWorkouts(String userId, ValueEventListener valueEventListener);

    //EXERCISES

    void getAllExercises(ValueEventListener valueEventListener);

    void getExercisesByType(String type, ValueEventListener valueEventListener);

    void getExercisesByName(String name, ValueEventListener valueEventListener);

    void getExercisesById(String exerciseId, ValueEventListener valueEventListener);

    void getExercisesById(String exerciseId, ChildEventListener childEventListener);

    void addExercise(DetailedExercise exercise);

    //TRAINING SESSION

    void saveNewTrainingSession(TrainingSession trainingSession);

    void getWorkoutTrainingSessions(String workoutId, ValueEventListener valueEventListener);

    void getTrainingSessions(ValueEventListener valueEventListener);
}
