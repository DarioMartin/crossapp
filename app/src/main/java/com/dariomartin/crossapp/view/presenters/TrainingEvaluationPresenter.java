package com.dariomartin.crossapp.view.presenters;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.view.fragments.TrainingEvaluationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dariomartin on 19/9/17.
 */

public class TrainingEvaluationPresenter {

    private IDataBaseController dataBaseController;
    private TrainingEvaluationFragment view;

    public TrainingEvaluationPresenter(TrainingEvaluationFragment view) {
        this.view = view;
        dataBaseController = DataBaseController.getInstance();
    }

    public void saveNewTrainingSession(Workout workout, long duration, float rating) {
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setDate(new Date().getTime());
        trainingSession.setDuration(duration);
        trainingSession.setPoints(workout.getPoints());
        trainingSession.setRating(rating);
        trainingSession.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        trainingSession.setWorkoutId(workout.getId());
        dataBaseController.saveNewTrainingSession(trainingSession);
    }

}
