package com.dariomartin.crossapp.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.IntensityUnit;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.SessionExercise;
import com.dariomartin.crossapp.view.presenters.AddExercisePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseActivity extends BaseActivity {

    public static final String EXERCISE = "ADDED_EXERCISE";
    public static final String WORKOUT = "TO_WORKOUT";
    public static final String PART = "TO_PART";


    @BindView(R.id.exercise_name)
    TextView exerciseName;
    @BindView(R.id.add_and_finish_button)
    TextView addAndFinishButton;
    @BindView(R.id.intensity_value)
    EditText intensityValueField;
    @BindView(R.id.intensity_unit_spinner)
    Spinner intensityUnit;
    @BindView(R.id.exercise_reps)
    EditText repsField;
    @BindView(R.id.workouts_spinner)
    Spinner workoutsSpinner;
    @BindView(R.id.parts_list)
    LinearLayout partsList;


    private DetailedExercise exercise;
    private Workout selectedWorkout;
    private Part selectedPart;
    private AddExercisePresenter presenter;
    private boolean fixedWorkout;
    private List<CheckBox> checkBoxes;
    private boolean fixedPart;
    private ArrayList<String> units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);


        if (getIntent().hasExtra(EXERCISE)) {
            exercise = getIntent().getParcelableExtra(EXERCISE);
        } else {
            finish();
        }

        if (getIntent().hasExtra(WORKOUT)) {
            selectedWorkout = getIntent().getParcelableExtra(WORKOUT);
            fixedWorkout = true;
        }

        if (getIntent().hasExtra(PART)) {
            selectedPart = getIntent().getParcelableExtra(PART);
            fixedPart = true;
        }

        checkBoxes = new ArrayList<>();

        exerciseName.setText(exercise.getName());
        addAndFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndFinish();
            }
        });

        presenter = new AddExercisePresenter(this);

        configureIntensityUnits();
        if (fixedWorkout && null != selectedWorkout) {
            setUserWorkouts(selectedWorkout);
        } else {
            presenter.getUserWorkouts();
        }

    }

    private void loadWorkout() {
        partsList.removeAllViews();
        checkBoxes.clear();
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Part part : selectedWorkout.getParts()) {
            CheckBox partCheckBox = (CheckBox) inflater.inflate(R.layout.check_box_item, null);
            partCheckBox.setText(part.getName());
            partsList.addView(partCheckBox);
            if (fixedPart && part.getName().equals(selectedPart.getName())) {
                partCheckBox.setChecked(true);
            }
            checkBoxes.add(partCheckBox);
        }
    }

    private void configureIntensityUnits() {
        units = new ArrayList<>();
        for (IntensityUnit intensityUnit : IntensityUnit.values()) {
            units.add(intensityUnit.getName(this));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        intensityUnit.setAdapter(dataAdapter);
        intensityUnit.setSelection(0);
    }

    public void setUserWorkouts(Workout workout) {
        setUserWorkouts(new ArrayList<>(Arrays.asList(workout)));
        workoutsSpinner.setEnabled(false);
    }

    public void setUserWorkouts(final List<Workout> workouts) {
        List<String> workoutNames = new ArrayList<>();
        for (Workout workout : workouts) {
            workoutNames.add(workout.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        workoutsSpinner.setAdapter(dataAdapter);
        workoutsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedWorkout = workouts.get(i);
                loadWorkout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        workoutsSpinner.setSelection(0);
    }

    private void addAndFinish() {
        for (Part part : selectedWorkout.getParts()) {
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.getText().equals(part.getName()) && checkBox.isChecked()) {
                    SessionExercise sessionExercise = new SessionExercise(exercise.getId(), exercise.getName());
                    sessionExercise.setIntensityUnit(getIntensityUnit().getName(this));
                    sessionExercise.setIntensityValue(getIntensityValue());
                    sessionExercise.setReps(getRepsValue());
                    part.addExercise(sessionExercise);
                }
            }
        }

        if (fixedWorkout) {
            finisAndReturnWorkout();
        } else {
            finish();
        }
    }

    private void finisAndReturnWorkout() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(WorkoutDetailActivity.WORKOUT_RESULT, selectedWorkout);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private IntensityUnit getIntensityUnit() {
        int position = intensityUnit.getSelectedItemPosition();
        IntensityUnit unit = IntensityUnit.values()[position];
        return unit;
    }

    private int getIntensityValue() {
        return Integer.valueOf(intensityValueField.getText().toString());
    }

    private int getRepsValue() {
        return Integer.valueOf(repsField.getText().toString());
    }

    public void addWorkouts(List<Workout> workouts) {

    }

    public void showError() {

    }
}
