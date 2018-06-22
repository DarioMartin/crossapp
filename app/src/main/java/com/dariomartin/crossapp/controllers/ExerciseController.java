package com.dariomartin.crossapp.controllers;

import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dariomartin on 22/10/17.
 */

class ExerciseController {

    private static final String EXERCISES_NODE = "exercises";
    private static final String TYPE = "type";
    private static final String NAME = "name";

    private final DatabaseReference exercisesNode;
    private static ExerciseController instance;

    static ExerciseController getInstance() {
        if (instance == null) {
            instance = new ExerciseController();
        }
        return instance;
    }

    private ExerciseController() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        exercisesNode = database.getReference(EXERCISES_NODE);
    }

    public void addExercise(DetailedExercise exercise) {
        exercisesNode.child(exercise.getId()).setValue(exercise);
    }

    void getAllExercises(ValueEventListener valueEventListener) {
        exercisesNode.addValueEventListener(valueEventListener);
    }

    void getExercisesByType(String type, ValueEventListener valueEventListener) {
        exercisesNode.orderByChild(TYPE).equalTo(type).addValueEventListener(valueEventListener);
    }

    void getExercisesByName(String name, ValueEventListener valueEventListener) {
        exercisesNode.orderByChild(NAME).startAt(name).addValueEventListener(valueEventListener);
    }

    void getExercisesById(String exerciseId, ValueEventListener valueEventListener) {
        exercisesNode.orderByKey().equalTo(exerciseId).addValueEventListener(valueEventListener);
    }

    void getExercisesById(String exerciseId, ChildEventListener childEventListener) {
        exercisesNode.orderByKey().equalTo(exerciseId).addChildEventListener(childEventListener);
    }
}
