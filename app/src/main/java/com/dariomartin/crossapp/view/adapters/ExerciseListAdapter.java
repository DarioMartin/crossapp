package com.dariomartin.crossapp.view.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.model.enums.Muscle;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

    private final boolean isTablet;
    private List<DetailedExercise> exercises;
    private final ExerciseListener listener;
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();
    private int currentItem = -1;

    public ExerciseListAdapter(ExerciseListener listener, boolean isTablet) {
        this.exercises = new ArrayList<>();
        this.listener = listener;
        this.isTablet = isTablet;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, final int position) {
        holder.bindExercise(exercises.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(holder.exercise, position);
            }
        });

        holder.addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onAddExerciseSelected(holder.exercise);
                }
            }
        });
    }

    private void onItemClicked(DetailedExercise detailedExercise, int position) {
        if (null != listener) {
            listener.onExerciseSelected(detailedExercise);
            updateSelectedPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<DetailedExercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void selectItem(int position) {
        if (position >= 0 && position < exercises.size()) {
            onItemClicked(exercises.get(position), position);
        }
    }

    private void updateSelectedPosition(int position) {
        selectedPositions.put(currentItem, false);
        notifyItemChanged(currentItem);

        currentItem = position;
        selectedPositions.put(currentItem, true);
        notifyItemChanged(currentItem);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        final TextView muscles;
        private final ImageView icon;
        private DetailedExercise exercise;
        private ImageView addExerciseButton;

        ExerciseViewHolder(View view) {
            super(view);
            this.view = view;
            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            muscles = view.findViewById(R.id.muscles);
            addExerciseButton = view.findViewById(R.id.add_exercise_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + muscles.getText() + "'";
        }

        void bindExercise(DetailedExercise exercise) {
            this.exercise = exercise;
            name.setText(exercise.getName());
            muscles.setText(getMuscleNames(muscles.getContext(), exercise.getMuscles()));

            if (!exercise.getEquipment().isEmpty()) {
                icon.setImageDrawable(view.getResources().getDrawable(exercise.getType().getIcon()));
            }

            if (isTablet) {
                boolean isSelected = selectedPositions.get(getAdapterPosition());
                Resources res = view.getResources();
                int backgroundColor = res.getColor(isSelected ? R.color.colorAccent10 : android.R.color.transparent);
                view.setBackgroundColor(backgroundColor);
            }
        }
    }

    private String getMuscleNames(Context context, List<Muscle> muscles) {
        ArrayList<String> muscleNames = new ArrayList<>();
        for (Muscle muscle : muscles) {
            muscleNames.add(context.getResources().getString(muscle.getName()));
        }
        return TextUtils.join(", ", muscleNames);
    }

    public int getCurrentItem() {
        return currentItem;
    }
}
