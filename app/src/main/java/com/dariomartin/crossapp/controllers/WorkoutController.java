package com.dariomartin.crossapp.controllers;

import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dariomartin on 22/10/17.
 */

class WorkoutController {

    private static final String WORKOUTS_NODE = "workouts";
    private static final String ADMIN = "admin";
    private static final String PRIVATE = "private";
    private static final String OWNER_ID = "ownerId";

    private final DatabaseReference workoutsNode;
    private static WorkoutController instance;

    static WorkoutController getInstance() {
        if (instance == null) {
            instance = new WorkoutController();
        }
        return instance;
    }

    private WorkoutController() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        workoutsNode = database.getReference(WORKOUTS_NODE);
    }

    Workout addNewPrivateWorkout(WorkoutDB workout) {
        String workoutId = workout.getId();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            workout.setOwnerId(user.getUid());
            workout.setPrivate(true);

            if (workoutId == null) {
                workoutId = addNewWorkout(workout);
                workout.setId(workoutId);
                updateWorkout(workout);
            } else {
                updateWorkout(workout);
            }
            return workout.getWorkout();
        } else {
            return null;
        }
    }

    String addNewPublicWorkout(WorkoutDB workout) {
        String workoutId = workout.getId();
        workout.setOwnerId(ADMIN);
        workout.setPrivate(false);

        if (workoutId == null) {
            workoutId = addNewWorkout(workout);
        } else {
            updateWorkout(workout);
        }

        return workoutId;
    }

    private String addNewWorkout(WorkoutDB workout) {
        DatabaseReference dbRef = workoutsNode.push();
        dbRef.setValue(workout);
        return dbRef.getKey();
    }

    public void updateWorkout(WorkoutDB workout) {
        if (workout.getId() != null) {
            workoutsNode.child(workout.getId()).setValue(workout);
        } else {
            addNewPrivateWorkout(workout);
        }
    }

    public void removeUserWorkout(WorkoutDB workout) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (workout.isPrivate() && user.getUid().equals(workout.getOwnerId())) {
            removeWorkout(workout);
        }
    }

    public void removeWorkout(WorkoutDB workout) {
        workoutsNode.child(workout.getId()).removeValue();
    }

    public void getAllWorkouts(ValueEventListener valueEventListener) {
        workoutsNode.addValueEventListener(valueEventListener);
    }

    public void getWorkoutById(String id, ValueEventListener valueEventListener) {
        workoutsNode.orderByKey().equalTo(id).addValueEventListener(valueEventListener);
    }

    public void getPublicWorkouts(ValueEventListener valueEventListener) {
        workoutsNode.orderByChild(PRIVATE).equalTo(false).addValueEventListener(valueEventListener);
    }

    public void getUserWorkouts(String userId, ValueEventListener valueEventListener) {
        workoutsNode.orderByChild(OWNER_ID).equalTo(userId).addValueEventListener(valueEventListener);
    }
}
