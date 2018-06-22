package com.dariomartin.crossapp.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.view.fragments.TrainingEvaluationFragment;
import com.dariomartin.crossapp.view.fragments.TrainingPartFragment;
import com.dariomartin.crossapp.widget.WidgetWorkoutService;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dariomartin.crossapp.view.fragments.TrainingEvaluationFragment.TrainingCompletionListener;
import static com.dariomartin.crossapp.view.fragments.TrainingPartFragment.TrainingPartListener;

public class TrainingActivity extends BaseActivity implements TrainingPartListener, TrainingCompletionListener {

    public static final String WORKOUT = "WORKOUT";

    @BindView(R.id.clock)
    TextView clockTextView;
    @BindView(R.id.clock_play)
    ImageView clockPlayImageView;
    @BindView(R.id.clock_pause)
    ImageView clockPauseImageView;

    private Workout workout;
    private FragmentManager fm;
    private TrainingEvaluationFragment trainingEvaluationFragment;
    private TrainingPartFragment trainingPartFragment;

    private int currentPart = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (activityIsAlreadyRunning()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_training);
        ButterKnife.bind(this);

        workout = getIntent().getParcelableExtra(WORKOUT);

        clockPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClock();
            }
        });

        clockPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseClock();
            }
        });

        fm = getSupportFragmentManager();

        goToTrainingPartView();

        startClock();
    }

    private boolean activityIsAlreadyRunning() {
        return (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0;
    }

    private void startClock() {
        clockPlayImageView.setVisibility(View.GONE);
        clockPauseImageView.setVisibility(View.VISIBLE);
        handler.post(runnable);
    }

    private void pauseClock() {
        clockPlayImageView.setVisibility(View.VISIBLE);
        clockPauseImageView.setVisibility(View.GONE);
        handler.removeCallbacks(runnable);
    }


    private Handler handler = new Handler();
    private long time;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            clockTextView.setText(formatTime(time));
            time += 50;
            handler.postDelayed(runnable, 50);
        }

        @NonNull
        private String formatTime(long time) {
            Date date = new Date(time);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SS");
            String formatted = formatter.format(date);
            return formatted;
        }
    };


    @Override
    public void onPartCompleted() {
        showTimeEndedDialog();
    }

    @Override
    public void onGoToProfile() {
        Intent intent = new Intent(this, FlavorMainActivity.class);
        intent.putExtra(MainActivity.TAB, R.id.navigation_statistics);
        startActivity(intent);
    }

    private boolean isLastPart() {
        return currentPart == workout.getParts().size() - 1;
    }

    private void goToTrainingPartView() {
        trainingPartFragment = TrainingPartFragment.newInstance(workout.getParts().get(currentPart));
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, trainingPartFragment).commit();
        runWidgetWorkoutService();
    }

    private void goToEvaluationView() {
        pauseClock();
        clockPlayImageView.setVisibility(View.GONE);
        trainingEvaluationFragment = TrainingEvaluationFragment.newInstance(workout, time);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, trainingEvaluationFragment).commit();
        runCleanWidget();
    }

    private void runWidgetWorkoutService() {
        Intent intent = new Intent(this, WidgetWorkoutService.class);
        intent.putExtra(WidgetWorkoutService.PART, workout.getParts().get(currentPart));
        intent.setAction(WidgetWorkoutService.ACTION_UPDATE_TRAINING_SESSION);
        getApplicationContext().startService(intent);
    }

    private void runCleanWidget() {
        Intent intent = new Intent(this, WidgetWorkoutService.class);
        getApplicationContext().startService(intent);
    }

    private void showTimeEndedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle(!isLastPart() ? R.string.part_time_ended_title : R.string.workout_ended_title)
                .setMessage(!isLastPart() ? R.string.part_time_ended_message : R.string.workout_ended_message)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isLastPart()) {
                            goToEvaluationView();
                        } else {
                            currentPart++;
                            goToTrainingPartView();
                        }
                    }
                }).setCancelable(false).show();
    }
}
