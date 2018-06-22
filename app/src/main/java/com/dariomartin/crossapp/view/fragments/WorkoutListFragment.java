package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.view.adapters.WorkoutListAdapter;
import com.dariomartin.crossapp.view.listeners.WorkoutListener;
import com.dariomartin.crossapp.view.presenters.WorkoutsListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutListFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.message_view)
    LinearLayout messageView;
    @BindView(R.id.title)
    TextView messageTitle;
    @BindView(R.id.subtitle)
    TextView messageSubtitle;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private WorkoutListener listener;
    private WorkoutListAdapter adapter;
    private WorkoutsListPresenter presenter;
    private GridLayoutManager layoutManager;
    private boolean isTablet;


    public WorkoutListFragment() {
    }

    public static WorkoutListFragment newInstance() {
        WorkoutListFragment fragment = new WorkoutListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);
        ButterKnife.bind(this, view);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        buildLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WorkoutListAdapter(getContext(), listener);
        recyclerView.setAdapter(adapter);

        presenter = new WorkoutsListPresenter(this);
        presenter.getWorkouts();

        return view;
    }

    private void buildLayoutManager() {
        layoutManager = new GridLayoutManager(getContext(), isTablet ? 2 : 1);
        if (isTablet) {
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 ? 2 : 1;
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkoutListener) {
            listener = (WorkoutListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnExerciseListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void showError() {
        // TODO: 29/9/17 SHOW ERROR ON LOADING WORKOUTS
    }

    public void addWorkouts(List<Workout> workouts) {
        progressBar.setVisibility(View.GONE);
        adapter.setWorkouts(workouts);

        if (!workouts.isEmpty()) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setVisibility(View.VISIBLE);
            messageTitle.setText(R.string.no_exercises_title);
            messageSubtitle.setText(R.string.no_exercises_subtitle);
        }
    }

}
