package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.enums.ExerciseType;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.view.fragments.ExercisesListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dariomartin on 19/9/17.
 */

public class ExercisesListPresenter {

    private IDataBaseController dataBaseController;
    private ExercisesListFragment view;
    private List<DetailedExercise> exercises;

    public ExercisesListPresenter(ExercisesListFragment view) {
        this.view = view;
        dataBaseController = DataBaseController.getInstance();
        exercises = new ArrayList<>();
    }

    public void getAllExercises() {
        dataBaseController.getAllExercises(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercises.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    DetailedExercise exercise = noteSnapshot.getValue(DetailedExercise.class);
                    exercises.add(exercise);
                }
                view.paintExercises(exercises);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });

    }

    public void getExercisesByType(ExerciseType typeSelected) {
        dataBaseController.getExercisesByType(typeSelected.toString(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercises.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    DetailedExercise exercise = noteSnapshot.getValue(DetailedExercise.class);
                    exercises.add(exercise);
                }
                view.paintExercises(exercises);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });
    }

    public void searchExercises(String query) {
        dataBaseController.getExercisesByName(query, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercises.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    DetailedExercise exercise = noteSnapshot.getValue(DetailedExercise.class);
                    exercises.add(exercise);
                }
                view.paintExercises(exercises);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showError();
            }
        });
    }
}
