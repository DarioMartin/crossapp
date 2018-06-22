package com.dariomartin.crossapp.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.exercise.Exercise;
import com.dariomartin.crossapp.view.fragments.ExerciseDetailFragment;
import com.dariomartin.crossapp.view.fragments.ExercisesListFragment;
import com.dariomartin.crossapp.view.fragments.StatsFragment;
import com.dariomartin.crossapp.view.fragments.WorkoutListFragment;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;
import com.dariomartin.crossapp.view.listeners.LogoutListener;
import com.dariomartin.crossapp.view.listeners.WorkoutListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class MainActivity extends BaseActivity implements ExerciseListener, WorkoutListener, LogoutListener {

    private static final String SELECTED_ITEM = "SELECTED_ITEM";
    private static final String EXERCISE_LIST_FRAGMENT = "EXERCISE_LIST_FRAGMENT";
    private static final String WORKOUT_LIST_FRAGMENT = "WORKOUT_LIST_FRAGMENT";
    private static final String STATS_FRAGMENT = "STATS_FRAGMENT";
    public static final String TAB = "TAB";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            updateLayout(item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_exercises:
                    loadExerciseList();
                    return true;
                case R.id.navigation_wods:
                    loadWorkoutList();
                    return true;
                case R.id.navigation_statistics:
                    loadUserView();
                    return true;
                default:
                    return loadFlavoredViews(item.getItemId());
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        int selectedItem = R.id.navigation_exercises;

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SELECTED_ITEM);
        }

        if (getIntent().hasExtra(TAB)) {
            selectedItem = getIntent().getIntExtra(TAB, R.id.navigation_exercises);
        }

        navigation.setSelectedItemId(selectedItem);
    }

    private void updateLayout(int itemId) {
        if (isTablet) {
            switch (itemId) {
                case R.id.navigation_exercises:
                    findViewById(R.id.content_detail).setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_wods:
                case R.id.navigation_statistics:
                    findViewById(R.id.content_detail).setVisibility(View.GONE);
                    break;
                default:
                    updateFlavoredLayouts(itemId);
            }
        }
    }

    protected abstract void updateFlavoredLayouts(int itemId);

    @Override
    public void onExerciseSelected(DetailedExercise exercise) {
        if (isTablet) {
            updateDetailsView(ExerciseDetailFragment.newInstance(exercise));
        } else {
            goToExerciseDetailsActivity(exercise);
        }
    }


    @Override
    public void onWorkoutSelected(Workout workout, boolean itsNew) {
        goToWorkoutDetailsActivity(workout, itsNew);
    }

    @Override
    public void onAddExerciseSelected(Exercise exercise) {
        goToAddExerciseActivity(exercise);
    }

    @Override
    public void addNewWorkout(boolean isPrivate) {
        showWorkoutNameDialog(isPrivate);
    }

    private void goToExerciseDetailsActivity(DetailedExercise exercise) {
        Intent intent = new Intent(this, ExerciseDetailActivity.class);
        intent.putExtra(ExerciseDetailActivity.EXERCISE, exercise);
        startActivity(intent);
    }

    private void goToWorkoutDetailsActivity(Workout workout, boolean itsNew) {
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.WORKOUT, workout);
        intent.putExtra(WorkoutDetailActivity.EDIT_MODE, itsNew);
        startActivity(intent);
    }

    private void goToAddExerciseActivity(Exercise exercise) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(AddExerciseActivity.EXERCISE, exercise);
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void loadExerciseList() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(EXERCISE_LIST_FRAGMENT);
        if (fragment == null) {
            fragment = ExercisesListFragment.newInstance();
        }
        loadFragment(fragment, EXERCISE_LIST_FRAGMENT);
    }

    private void loadWorkoutList() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(WORKOUT_LIST_FRAGMENT);
        if (fragment == null) {
            fragment = WorkoutListFragment.newInstance();
        }
        loadFragment(fragment, WORKOUT_LIST_FRAGMENT);
    }

    private void loadUserView() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(STATS_FRAGMENT);
        if (fragment == null) {
            fragment = StatsFragment.newInstance();
        }
        loadFragment(fragment, STATS_FRAGMENT);
    }

    protected abstract boolean loadFlavoredViews(int itemId);

    public void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.content, fragment, tag).commit();
    }

    private void updateDetailsView(Fragment fragment) {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.content_detail, fragment).commit();
    }

    private void showWorkoutNameDialog(final boolean isPrivate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme);
        builder.setTitle(getString(R.string.new_workout_dialog_title));

        final EditText workoutNameET = new EditText(this);
        workoutNameET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(workoutNameET);

        builder.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkWorkoutName()) {
                    String workoutName = workoutNameET.getText().toString();
                    Workout workout = new Workout(workoutName);
                    workout.setPrivate(isPrivate);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    onWorkoutSelected(workout, true);
                } else {
                    Toast.makeText(MainActivity.this, R.string.invalid_workout_name, Toast.LENGTH_SHORT).show();
                }
            }

            private boolean checkWorkoutName() {
                return !workoutNameET.getText().toString().isEmpty();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        goToLoginActivity();
    }
}
