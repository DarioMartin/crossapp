package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.view.presenters.TrainingEvaluationPresenter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 14/10/17.
 */

public class TrainingEvaluationFragment extends Fragment {

    private static final String WORKOUT = "WORKOUT";
    private static final String TIME = "TIME";
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.go_to_profile_button)
    TextView goToProfileButton;

    private TrainingCompletionListener listener;
    private Workout workout;
    private TrainingEvaluationPresenter presenter;
    private long time;

    public interface TrainingCompletionListener {
        void onGoToProfile();
    }

    public TrainingEvaluationFragment() {
    }

    public static TrainingEvaluationFragment newInstance(Workout workout, long time) {
        TrainingEvaluationFragment instance = new TrainingEvaluationFragment();
        Bundle args = new Bundle();
        args.putParcelable(WORKOUT, workout);
        args.putLong(TIME, time);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workout = getArguments().getParcelable(WORKOUT);
        time = getArguments().getLong(TIME);
        presenter = new TrainingEvaluationPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_evaluation, container, false);
        ButterKnife.bind(this, view);

        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveNewTrainingSession(workout, time, ratingBar.getRating());
                listener.onGoToProfile();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrainingCompletionListener) {
            listener = (TrainingCompletionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TrainingCompletionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
