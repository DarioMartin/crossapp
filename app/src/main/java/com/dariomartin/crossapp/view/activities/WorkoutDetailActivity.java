package com.dariomartin.crossapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.Exercise;
import com.dariomartin.crossapp.view.fragments.ExercisesListFragment;
import com.dariomartin.crossapp.view.fragments.PartCreationFragment;
import com.dariomartin.crossapp.view.fragments.WorkoutDetailEditFragment;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;

import butterknife.ButterKnife;

import static com.dariomartin.crossapp.view.fragments.WorkoutDetailEditFragment.WorkoutDetailsListener;

public class WorkoutDetailActivity extends BaseActivity implements ExerciseListener, WorkoutDetailsListener, PartCreationFragment.PartCreationListener {

    public static final String WORKOUT = "WORKOUT";
    public static final String WORKOUT_RESULT = "WORKOUT_RESULT";
    public static final String EDIT_MODE = "EDIT_MODE";
    private static final String EXERCISE_LIST_FRAGMENT = "EXERCISE_LIST_FRAGMENT";
    private static final String NEW_PART_FRAGMENT = "NEW_PART_FRAGMENT";
    private static final int UPDATE_WORKOUT_REQUEST = 1;

    private Workout workout;
    private Part selectedPart;
    private WorkoutDetailEditFragment workoutDetailsEditFragment;
    private ExercisesListFragment exercisesListFragment;
    private PartCreationFragment partCreationFragment;
    private boolean editMode;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);

        fm = getSupportFragmentManager();

        workout = getIntent().getParcelableExtra(WORKOUT);
        editMode = getIntent().getBooleanExtra(EDIT_MODE, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_WORKOUT_REQUEST) {
            if (resultCode == RESULT_OK) {
                workout = data.getParcelableExtra(WORKOUT_RESULT);
                editMode = true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadWorkoutEditView();
    }

    @Override
    public void onExerciseSelected(DetailedExercise exercise) {
        addExerciseToSelectedPart(exercise);
    }

    @Override
    public void onAddExerciseSelected(Exercise exercise) {
        addExerciseToSelectedPart(exercise);
    }

    private void addExerciseToSelectedPart(Exercise exercise) {
        if (selectedPart != null) {
            removeExerciseListFragment();
            Intent intent = new Intent(this, AddExerciseActivity.class);
            intent.putExtra(AddExerciseActivity.EXERCISE, exercise);
            intent.putExtra(AddExerciseActivity.WORKOUT, workout);
            intent.putExtra(AddExerciseActivity.PART, selectedPart);
            startActivityForResult(intent, UPDATE_WORKOUT_REQUEST);
        }
    }

    private void removeExerciseListFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(exercisesListFragment).commit();
        fm.popBackStack();
    }


    @Override
    public void onAddNewPartClicked() {
        loadNewPartView();
    }

    @Override
    public void onAddNewExerciseToPartSelected(Part part) {
        selectedPart = part;
        loadExerciseListView();
    }

    @Override
    public void onStartTrainingSelected() {
        if (everyPartHasSomeExercises()) {
            Intent intent = new Intent(this, TrainingActivity.class);
            intent.putExtra(TrainingActivity.WORKOUT, workout);
            startActivity(intent);
            finish();
        } else {
            // TODO: 14/10/17 SHOW INFO MESSAGE
        }
    }

    private boolean everyPartHasSomeExercises() {
        boolean isComplete = true;
        if (workout.getParts() != null && !workout.getParts().isEmpty()) {
            for (Part part : workout.getParts()) {
                if (part.getExercises() == null || part.getExercises().isEmpty()) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return isComplete;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadWorkoutEditView() {
        workoutDetailsEditFragment = WorkoutDetailEditFragment.newInstance(workout, editMode);
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.content, workoutDetailsEditFragment).commit();
    }

    private void loadNewPartView() {
        partCreationFragment = PartCreationFragment.newInstance();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, partCreationFragment).addToBackStack(NEW_PART_FRAGMENT).commit();
    }

    private void loadExerciseListView() {
        exercisesListFragment = ExercisesListFragment.newInstance();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, exercisesListFragment)
                .addToBackStack(EXERCISE_LIST_FRAGMENT)
                .commit();
    }

    @Override
    public void onPartCreated(Part part) {
        workout.addPart(part);
        fm.popBackStackImmediate();
        editMode = true;
        loadWorkoutEditView();
    }
}
