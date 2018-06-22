package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dariomartin.crossapp.BuildConfig;
import com.dariomartin.crossapp.CrossApplication;
import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.Equipment;
import com.dariomartin.crossapp.model.enums.Muscle;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.view.adapters.ImageAdapter;
import com.dariomartin.crossapp.view.components.MaterialView;
import com.dariomartin.crossapp.view.components.MuscleView;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 27/10/17.
 */

public class ExerciseDetailFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    private static final String EXERCISE = "EXERCISE";

    @BindView(R.id.expand_text_view)
    ExpandableTextView description;
    @BindView(R.id.muscles_grid)
    GridLayout musclesGrid;
    @BindView(R.id.material_grid)
    GridLayout materialGrid;
    @BindView(R.id.exercise_type_icon)
    ImageView exerciseTypeIcon;
    @BindView(R.id.exercise_type_name)
    TextView exerciseTypeName;
    @BindView(R.id.video_content)
    LinearLayout videoContent;

    @Nullable
    @BindView(R.id.add_exercise_button)
    FloatingActionButton addExerciseFab;
    @Nullable
    @BindView(R.id.media_viewpager)
    ViewPager mediaViewPager;

    private DetailedExercise exercise;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private boolean isTablet;
    private ExerciseListener listener;

    public ExerciseDetailFragment() {
    }

    public static ExerciseDetailFragment newInstance(DetailedExercise exercise) {
        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXERCISE, exercise);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercise = getArguments().getParcelable(EXERCISE);
        isTablet = getContext().getResources().getBoolean(R.bool.isTablet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = isTablet ? R.layout.fragment_exercise_detail_tablet : R.layout.fragment_exercise_detail_body;
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        description.setText(exercise.getDescription());
        exerciseTypeName.setText(exercise.getType().getName());
        exerciseTypeIcon.setImageDrawable(getResources().getDrawable(exercise.getType().getIcon()));

        if (isTablet) {
            ((TextView) getView().findViewById(R.id.exercise_name)).setText(exercise.getName());
        }

        setUpMediaViewPager();

        setUpAddExerciseButton();

        fillMaterialGrid(exercise.getEquipment());

        fillMuscleGrid(exercise.getMuscles());
    }

    private void setUpAddExerciseButton() {
        if (addExerciseFab == null) {
            addExerciseFab = getActivity().findViewById(R.id.add_exercise_button);
        }

        addExerciseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddExerciseSelected(exercise);
            }
        });
    }

    private void setUpMediaViewPager() {
        if (mediaViewPager == null) {
            mediaViewPager = getActivity().findViewById(R.id.media_viewpager);
        }

        ImageAdapter imageAdapter = new ImageAdapter(getContext(), exercise.getImages());
        mediaViewPager.setAdapter(imageAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadVideo(exercise.getVideoUrl());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExerciseListener) {
            listener = (ExerciseListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ExerciseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void loadVideo(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            videoContent.setVisibility(View.VISIBLE);
            youTubePlayerFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
            youTubePlayerFragment.initialize(BuildConfig.YoutubeApiKey, this);
        }
    }

    private void fillMaterialGrid(List<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            View view = new MaterialView(getContext(), equipment);
            materialGrid.addView(view);
        }
    }

    private void fillMuscleGrid(List<Muscle> muscles) {
        for (Muscle muscle : muscles) {
            View view = new MuscleView(getContext(), muscle);
            musclesGrid.addView(view);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.setFullscreenControlFlags(0);
            player.setShowFullscreenButton(false);
            player.cueVideo(exercise.getVideoUrl());
        }
    }

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = getString(R.string.error_player);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(BuildConfig.YoutubeApiKey, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerFragment;
    }
}
