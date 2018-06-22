package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.pojos.WorkoutDB;
import com.dariomartin.crossapp.view.fragments.WorkoutListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dariomartin on 19/9/17.
 */

public class WorkoutsListPresenter {

    private IDataBaseController dataBaseController;
    private WorkoutListFragment view;
    private List<Workout> workouts;

    public WorkoutsListPresenter(WorkoutListFragment view) {
        this.view = view;
        dataBaseController = DataBaseController.getInstance();
        workouts = new ArrayList<>();
    }

    public void getWorkouts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dataBaseController.getUserWorkouts(user.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workouts.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    WorkoutDB workoutDB = noteSnapshot.getValue(WorkoutDB.class);
                    Workout workout = new Workout(workoutDB);
                    workouts.add(workout);
                }

                updateWorkoutWithTrainingSessions();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });

    }

    private void updateWorkoutWithTrainingSessions() {
        for (int i = 0; i < workouts.size(); i++) {
            getTrainingSessions(i);
        }
    }

    private void getTrainingSessions(final int i) {
        dataBaseController.getWorkoutTrainingSessions(workouts.get(i).getId(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TrainingSession> trainingSessions = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    TrainingSession trainingSession = noteSnapshot.getValue(TrainingSession.class);
                    trainingSessions.add(trainingSession);
                }
                TrainingSession lastTrainingSession = getLastTrainingSesssion(trainingSessions);
                workouts.get(i).updateWithTrainingSession(lastTrainingSession);
                view.addWorkouts(workouts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });

    }

    private TrainingSession getLastTrainingSesssion(List<TrainingSession> trainingSessions) {
        if (trainingSessions != null && !trainingSessions.isEmpty()) {
            Collections.sort(trainingSessions);
            return trainingSessions.get(trainingSessions.size() - 1);
        }
        return null;
    }

    public boolean hasWorkouts() {
        return workouts != null && !workouts.isEmpty();
    }
}
