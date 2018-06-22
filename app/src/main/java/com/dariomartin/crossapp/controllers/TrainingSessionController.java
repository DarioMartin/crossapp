package com.dariomartin.crossapp.controllers;

import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dariomartin on 22/10/17.
 */

class TrainingSessionController {

    private static final String TRAINING_SESSIONS_NODE = "training_sessions";
    private static final String WORKOUT_ID = "workoutId";
    private static final String DATE = "date";

    private static TrainingSessionController instance;

    static TrainingSessionController getInstance() {
        if (instance == null) {
            instance = new TrainingSessionController();
        }
        return instance;
    }

    public String addNewTrainingSessions(TrainingSession trainingSession) {
        DatabaseReference dbRef = getTrainingSessionNode().push();
        dbRef.setValue(trainingSession);
        return dbRef.getKey();
    }

    public void getWorkoutTrainingSessions(String workoutId, ValueEventListener valueEventListener) {
        getTrainingSessionNode().orderByChild(WORKOUT_ID).equalTo(workoutId)
                .addValueEventListener(valueEventListener);
    }

    public void getUserTrainingSessions(ValueEventListener valueEventListener) {
        getTrainingSessionNode().orderByChild(DATE).addValueEventListener(valueEventListener);
    }

    private DatabaseReference getTrainingSessionNode(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return database.getReference(TRAINING_SESSIONS_NODE + "/" + user.getUid());
    }
}
