package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;

/**
 * Created by dariomartin on 19/9/17.
 */

public class CreateExercisePresenter {

    private IDataBaseController dataBaseController;

    public CreateExercisePresenter() {
        dataBaseController = DataBaseController.getInstance();
    }

    public void addExercise(DetailedExercise exercise) {
        dataBaseController.addExercise(exercise);
    }
}
