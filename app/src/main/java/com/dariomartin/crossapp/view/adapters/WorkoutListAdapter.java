package com.dariomartin.crossapp.view.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.util.Utils;
import com.dariomartin.crossapp.view.listeners.WorkoutListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_BUTTON = 0;
    private static final int WORKOUT = 1;
    private final Context context;

    private List<Workout> workouts;
    private final WorkoutListener listener;

    public WorkoutListAdapter(Context context, WorkoutListener listener) {
        this.context = context;
        this.workouts = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WORKOUT:
                View workoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
                return new WorkoutViewHolder(workoutView);
            default:
                View addWorkoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_workout_list_item, parent, false);
                addWorkoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.addNewWorkout(true);
                        }
                    }
                });
                return new AddItemViewHolder(addWorkoutView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == WORKOUT) {
            final WorkoutViewHolder workoutViewHolder = (WorkoutViewHolder) holder;
            workoutViewHolder.setWorkout(workouts.get(position - 1));
            workoutViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onWorkoutSelected(workoutViewHolder.workout, false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ADD_BUTTON : WORKOUT;
    }

    @Override
    public int getItemCount() {
        return workouts.size() + 1;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
        notifyDataSetChanged();
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.last_date)
        TextView lastDate;
        @BindView(R.id.last_duration)
        TextView lastDuration;
        @BindView(R.id.background)
        ImageView background;
        Workout workout;

        WorkoutViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setWorkout(Workout workout) {
            this.workout = workout;
            name.setText(workout.getName());
            ratingBar.setRating(workout.getRating());
            lastDate.setText(getDateText(workout.getDate()));
            lastDuration.setText(getDurationText(workout.getTime()));
            background.setImageDrawable(Utils.getWorkoutImage(workout, context));
        }

        private String getDurationText(long time) {
            if (time <= 0) {
                return "--:--:--";
            } else {
                Date date = new Date(time);
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SS");
                return formatter.format(date);
            }
        }

        private String getDateText(Date date) {
            if (date == null) {
                return context.getString(R.string.no_done_yet);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
                return formatter.format(date);
            }
        }
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {
        public AddItemViewHolder(View view) {
            super(view);
        }
    }

}
