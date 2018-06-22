package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.Workout;
import com.dariomartin.crossapp.util.Utils;
import com.dariomartin.crossapp.view.components.PartView;
import com.dariomartin.crossapp.view.presenters.WorkoutDetailEditPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 1/10/17.
 */

public class WorkoutDetailEditFragment extends Fragment {

    private static final String WORKOUT = "WORKOUT";
    private static String EDIT_MODE = "EDIT_MODE";

    private WorkoutDetailEditPresenter presenter;
    private WorkoutDetailsListener listener;

    @BindView(R.id.start_training_button)
    TextView startTrainingButton;
    @BindView(R.id.save_training_button)
    TextView saveButton;
    @BindView(R.id.part_list)
    LinearLayout partListLayout;
    @BindView(R.id.message_view)
    LinearLayout messageView;
    @BindView(R.id.workout_backdrop)
    ImageView background;

    LinearLayout addNewPartButton;

    private boolean editMode;

    public interface WorkoutDetailsListener {
        void onAddNewPartClicked();

        void onAddNewExerciseToPartSelected(Part part);

        void onStartTrainingSelected();
    }

    public WorkoutDetailEditFragment() {
    }

    public static WorkoutDetailEditFragment newInstance(Workout workout, boolean editMode) {
        WorkoutDetailEditFragment instance = new WorkoutDetailEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(WORKOUT, workout);
        args.putBoolean(EDIT_MODE, editMode);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        editMode = getArguments().getBoolean(EDIT_MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_edit_detail, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Workout workout = getArguments().getParcelable(WORKOUT);
        getActivity().setTitle(workout.getName());

        LayoutInflater factory = LayoutInflater.from(getActivity());
        addNewPartButton = (LinearLayout) factory.inflate(R.layout.add_part_button, null);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = false;
                saveWorkout();
            }
        });

        addNewPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddNewPartClicked();
            }
        });

        startTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStartTrainingSelected();
            }
        });

        presenter = new WorkoutDetailEditPresenter(this, workout);

        if(workout.getId()!=null){
            background.setImageDrawable(Utils.getWorkoutImage(workout, getContext()));
        }

        updateEditModeView(editMode);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.workout_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.workout_edit);
        menuItem.setEnabled(!editMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.workout_edit:
                updateEditModeView(true);
                break;
            case R.id.workout_delete:
                showConfirmDeleteDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkoutDetailsListener) {
            listener = (WorkoutDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement WorkoutDetailsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        paintWorkout();
    }

    public void paintWorkout() {
        presenter.getParts();
    }

    public void updateParts(List<Part> parts) {

        partListLayout.removeAllViews();


        if (parts != null) {
            for (Part part : parts) {
                PartView partView = new PartView(getActivity(), part, editMode);
                partView.setListener(listener);
                partListLayout.addView(partView);
            }
        }

        updateEmptyWorkoutMessage();

        startTrainingSetEnabled(!presenter.isAnEmptyWorkout());

        addNewPartButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        partListLayout.addView(addNewPartButton);

    }

    private void updateEditModeView(boolean editMode) {
        this.editMode = editMode;

        saveButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        addNewPartButton.setVisibility(editMode ? View.VISIBLE : View.GONE);
        startTrainingButton.setVisibility(editMode ? View.GONE : View.VISIBLE);
        updateEmptyWorkoutMessage();

        for (int i = 0; i < partListLayout.getChildCount(); i++) {
            if (partListLayout.getChildAt(i) instanceof PartView) {
                ((PartView) partListLayout.getChildAt(i)).setEditMode(editMode);
            }
        }
    }

    private void saveWorkout() {
        if (presenter.allPartsAreComplete()) {
            presenter.saveWorkout();
            updateEditModeView(editMode);
        } else {
            showAlertDialog(R.string.incomplete_parts_error);
        }
    }

    private void deleteWorkout() {
        presenter.deleteWorkout();
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showAlertDialog(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setMessage(message).setTitle(R.string.attention);
        builder.setNeutralButton(R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmDeleteDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteWorkout();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle(R.string.workout_delete_confirmation_title)
                .setMessage(R.string.workout_delete_confirmation_message)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private void startTrainingSetEnabled(boolean enabled) {
        startTrainingButton.setEnabled(enabled);
    }

    private void updateEmptyWorkoutMessage() {
        messageView.setVisibility(presenter.isAnEmptyWorkout() && !editMode ? View.VISIBLE : View.INVISIBLE);
    }
}
