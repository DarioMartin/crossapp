package com.dariomartin.crossapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.Exercise;
import com.dariomartin.crossapp.view.fragments.ExerciseDetailFragment;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;

import butterknife.ButterKnife;

public class ExerciseDetailActivity extends BaseActivity implements ExerciseListener {

    public static final String EXERCISE = "EXERCISE";
    private DetailedExercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXERCISE)) {
            exercise = getIntent().getParcelableExtra(EXERCISE);
        } else {
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(exercise.getName());

        loadContent();
    }

    private void loadContent() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        ExerciseDetailFragment fragment = ExerciseDetailFragment.newInstance(exercise);
        fm.replace(R.id.content, fragment).commit();
    }

    @Override
    public void onExerciseSelected(DetailedExercise exercise) {

    }

    @Override
    public void onAddExerciseSelected(Exercise exercise) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(AddExerciseActivity.EXERCISE, exercise);
        startActivity(intent);
    }
}
