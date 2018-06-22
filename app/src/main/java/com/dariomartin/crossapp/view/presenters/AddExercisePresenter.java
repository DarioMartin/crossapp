package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.view.activities.AddExerciseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by dariomartin on 19/9/17.
 */

public class AddExercisePresenter {

    private IDataBaseController dataBaseController;
    private AddExerciseActivity view;
    private List<Workout> workouts;

    public AddExercisePresenter(AddExerciseActivity view) {
        this.view = view;
        dataBaseController = DataBaseController.getInstance();
        workouts = new ArrayList<>();
    }


    public void getUserWorkouts(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        dataBaseController.getUserWorkouts(user.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workouts.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Workout workout = noteSnapshot.getValue(Workout.class);
                    workouts.add(workout);
                    //view.addWorkout(workout);
                }
                view.setUserWorkouts(workouts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });

    }

}
