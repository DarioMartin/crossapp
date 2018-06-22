package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.enums.PartDurationUnit;
import com.dariomartin.crossapp.model.exercise.SessionExercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 14/10/17.
 */

public class TrainingPartFragment extends Fragment {

    private static final String PART = "PART";

    @BindView(R.id.part_name)
    TextView partNameTextView;
    @BindView(R.id.part_duration)
    TextView partDurationTextView;
    @BindView(R.id.exercise_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.next_exercise_message)
    TextView nextExerciseMessage;
    @BindView(R.id.next_exercise_button)
    View nextExerciseButton;

    private TrainingPartListener listener;
    private Part part;
    private TrainingExerciseAdapter adapter;
    private int currentExercise = 0;

    private Handler handler = new Handler();
    private int time;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            partDurationTextView.setText(formatTime(time));
            time--;
            if (time > 0) {
                handler.postDelayed(runnable, 1000);
            } else {
                timeEnded();
            }
        }

        @NonNull
        private String formatTime(int time) {
            Date date = new Date(time * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            String formatted = formatter.format(date);
            return getString(R.string.in_minutes_training, formatted);
        }

    };

    private void timeEnded() {
        listener.onPartCompleted();
    }

    public interface TrainingPartListener {
        void onPartCompleted();
    }

    public TrainingPartFragment() {
    }

    public static TrainingPartFragment newInstance(Part part) {
        TrainingPartFragment instance = new TrainingPartFragment();
        Bundle args = new Bundle();
        args.putParcelable(PART, part);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        part = getArguments().getParcelable(PART);
        time = part.getDurationValue() * 60;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_part, container, false);
        ButterKnife.bind(this, view);

        partNameTextView.setText(part.getName());

        nextExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (part.getDurationUnit() != PartDurationUnit.IN_MINUTES) nextExercise();
                else listener.onPartCompleted();
            }
        });

        if (part.getDurationUnit() == PartDurationUnit.IN_MINUTES) {
            nextExerciseMessage.setText(getString(R.string.tap_to_skip_part));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingExerciseAdapter(part.nextRound());
        recyclerView.setAdapter(adapter);

        updatePartDurationValue();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (part.getDurationUnit() == PartDurationUnit.IN_MINUTES) startClock();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (part.getDurationUnit() == PartDurationUnit.IN_MINUTES) pauseClock();
    }

    private void nextExercise() {
        if (isLastExercise()) {
            currentExercise = 0;
            adapter.changeCurrentItem(currentExercise);

            ArrayList<SessionExercise> followingExercises = part.nextRound();

            if (!followingExercises.isEmpty()) {
                adapter.setExercises(followingExercises);
            }

            if (part.isFinished()) {
                listener.onPartCompleted();
            } else {
                updatePartDurationValue();
            }
        } else {
            currentExercise++;
            adapter.changeCurrentItem(currentExercise);
        }
    }

    private boolean isLastExercise() {
        return currentExercise == part.getExercises().size() - 1;
    }

    private void updatePartDurationValue() {
        PartDurationUnit partDurationUnit = part.getDurationUnit();
        switch (partDurationUnit) {
            case ROUNDS_FOR_TIME:
                partDurationTextView.setText(getString(R.string.rounds_for_time_training, part.getCurrentRound(), part.getDurationValue()));
                break;
            case COUNT:
                partDurationTextView.setText(getString(R.string.count_training, part.getDurationValue()));
                break;
            case COUNTDOWN:
                partDurationTextView.setText(getString(R.string.countdown_training, part.getDurationValue()));
                break;
            case PROGRESSION_21_15_9:
                partDurationTextView.setText(getString(R.string.progression_21_15_9_training));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrainingPartListener) {
            listener = (TrainingPartListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TrainingPartListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void startClock() {
        handler.post(runnable);
    }

    private void pauseClock() {
        handler.removeCallbacks(runnable);
    }

    private class TrainingExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

        private static final int CURRENT = 1;
        private static final int OTHER = 0;

        private List<SessionExercise> exercises;
        private int currentExercise = 0;

        public TrainingExerciseAdapter(List<SessionExercise> exercises) {
            this.exercises = exercises;
        }

        @Override
        public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    (part.getDurationUnit() == PartDurationUnit.IN_MINUTES || viewType == CURRENT)
                            ? R.layout.exercise_training_current_item : R.layout.exercise_training_other_item,
                    parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExerciseViewHolder holder, int position) {
            holder.setExercise(exercises.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return currentExercise == position ? CURRENT : OTHER;
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        public void changeCurrentItem(int newCurrent) {
            currentExercise = newCurrent;
            notifyDataSetChanged();
        }

        public void setExercises(ArrayList<SessionExercise> exercises) {
            this.exercises = exercises;
            notifyDataSetChanged();
        }
    }

    private class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private TextView reps;
        private TextView name;
        private TextView intensityValue;
        private TextView intensityUnit;

        public ExerciseViewHolder(View view) {
            super(view);
            reps = view.findViewById(R.id.reps);
            name = view.findViewById(R.id.name);
            intensityValue = view.findViewById(R.id.intensity_value);
            intensityUnit = view.findViewById(R.id.intensity_unit);
        }

        public void setExercise(SessionExercise sessionExercise) {
            reps.setText(String.valueOf(sessionExercise.getReps()));
            name.setText(sessionExercise.getName());
            intensityValue.setText(String.valueOf(sessionExercise.getIntensityValue()));
            intensityUnit.setText(String.valueOf(sessionExercise.getIntensityUnit()));
        }
    }

}
