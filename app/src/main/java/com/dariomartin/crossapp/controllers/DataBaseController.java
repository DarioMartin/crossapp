package com.dariomartin.crossapp.controllers;

import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dariomartin on 19/9/17.
 */

public class DataBaseController implements IDataBaseController {

    private static DataBaseController instance;

    private DataBaseController() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    }

    public static DataBaseController getInstance() {
        if (instance == null) {
            instance = new DataBaseController();
        }
        return instance;
    }

    //EXERCISES

    @Override
    public void addExercise(DetailedExercise exercise) {
        ExerciseController.getInstance().addExercise(exercise);
    }

    @Override
    public void getAllExercises(ValueEventListener valueEventListener) {
        ExerciseController.getInstance().getAllExercises(valueEventListener);
    }

    @Override
    public void getExercisesByType(String type, ValueEventListener valueEventListener) {
        ExerciseController.getInstance().getExercisesByType(type, valueEventListener);
    }

    @Override
    public void getExercisesByName(String name, ValueEventListener valueEventListener) {
        ExerciseController.getInstance().getExercisesByName(name, valueEventListener);
    }

    @Override
    public void getExercisesById(String exerciseId, ValueEventListener valueEventListener) {
        ExerciseController.getInstance().getExercisesById(exerciseId, valueEventListener);
    }

    @Override
    public void getExercisesById(String exerciseId, ChildEventListener childEventListener) {
        ExerciseController.getInstance().getExercisesById(exerciseId, childEventListener);
    }

    //WORKOUTS

    @Override
    public Workout addNewPrivateWorkout(Workout workout) {
        return WorkoutController.getInstance().addNewPrivateWorkout(new WorkoutDB(workout));
    }

    @Override
    public String addNewPublicWorkout(Workout workout) {
        return WorkoutController.getInstance().addNewPublicWorkout(new WorkoutDB(workout));
    }

    @Override
    public void updateWorkout(Workout workout) {
        WorkoutController.getInstance().updateWorkout(new WorkoutDB(workout));
    }

    @Override
    public void removeUserWorkout(Workout workout) {
        WorkoutController.getInstance().removeUserWorkout(new WorkoutDB(workout));
    }

    @Override
    public void removeWorkout(Workout workout) {
        WorkoutController.getInstance().removeWorkout(new WorkoutDB(workout));
    }

    @Override
    public void getAllWorkouts(ValueEventListener valueEventListener) {
        WorkoutController.getInstance().getAllWorkouts(valueEventListener);
    }

    @Override
    public void getWorkoutById(String id, ValueEventListener valueEventListener) {
        WorkoutController.getInstance().getWorkoutById(id, valueEventListener);
    }

    @Override
    public void getPublicWorkouts(ValueEventListener valueEventListener) {
        WorkoutController.getInstance().getPublicWorkouts(valueEventListener);
    }

    @Override
    public void getUserWorkouts(String userId, ValueEventListener valueEventListener) {
        WorkoutController.getInstance().getUserWorkouts(userId, valueEventListener);
    }

    //TRAINING SESSIONS

    @Override
    public void saveNewTrainingSession(TrainingSession trainingSession) {
        TrainingSessionController.getInstance().addNewTrainingSessions(trainingSession);
    }

    @Override
    public void getWorkoutTrainingSessions(String workoutId, ValueEventListener valueEventListener) {
        TrainingSessionController.getInstance().getWorkoutTrainingSessions(workoutId, valueEventListener);
    }

    @Override
    public void getTrainingSessions(ValueEventListener valueEventListener) {
        TrainingSessionController.getInstance().getUserTrainingSessions(valueEventListener);
    }

    public interface DBResultListener<T> {
        void onSuccess(T result);

        void onError(int code, String message);
    }

}