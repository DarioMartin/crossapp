package com.dariomartin.crossapp.view.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.enums.PartDurationUnit;
import com.dariomartin.crossapp.model.exercise.SessionExercise;
import com.dariomartin.crossapp.view.fragments.WorkoutDetailEditFragment.WorkoutDetailsListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 7/9/17.
 */

public class PartView extends LinearLayout {
    @BindView(R.id.part_name)
    TextView name;
    @BindView(R.id.duration)
    TextView durationUnit;
    @BindView(R.id.part_exercise_list)
    LinearLayout exerciseList;
    @BindView(R.id.add_new_exercise)
    LinearLayout addNewExerciseButton;

    private boolean editMode;
    private Part part;
    private WorkoutDetailsListener listener;

    public PartView(Context context) {
        super(context);
        init();
    }

    public PartView(Context context, Part part, boolean editMode) {
        super(context);
        this.editMode = editMode;
        this.part = part;
        init();
    }

    public PartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.part_view, this, true);
        ButterKnife.bind(this);
        buildPart();
    }

    private void buildPart() {
        name.setText(part.getName());
        durationUnit.setText(getDurationString());
        LayoutInflater factory = LayoutInflater.from(getContext());
        for (SessionExercise exercise : part.getExercises()) {
            View exerciseView = factory.inflate(R.layout.part_view_exercise_item, null);
            ((TextView) exerciseView.findViewById(R.id.reps)).setText(String.valueOf(exercise.getReps()));
            ((TextView) exerciseView.findViewById(R.id.name)).setText(exercise.getName());
            ((TextView) exerciseView.findViewById(R.id.intensity_value)).setText(String.valueOf(exercise.getIntensityValue()));
            ((TextView) exerciseView.findViewById(R.id.intensity_unit)).setText(exercise.getIntensityUnit());
            exerciseList.addView(exerciseView);
        }

        addNewExerciseButton.setVisibility(editMode ? VISIBLE : GONE);
    }

    @NonNull
    private String getDurationString() {
        PartDurationUnit partDurationUnit = part.getDurationUnit();
        switch (partDurationUnit) {
            case COUNT:
                return getContext().getString(partDurationUnit.getRepresentation(getContext()), 1, part.getDurationValue());
            case COUNTDOWN:
                return getContext().getString(partDurationUnit.getRepresentation(getContext()), part.getDurationValue(), 1);
            default:
                return getContext().getString(partDurationUnit.getRepresentation(getContext()), part.getDurationValue());
        }
    }

    public void setListener(final WorkoutDetailsListener listener) {
        this.listener = listener;
        addNewExerciseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddNewExerciseToPartSelected(part);
            }
        });
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        addNewExerciseButton.setVisibility(this.editMode ? VISIBLE : GONE);
    }
}
