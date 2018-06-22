package com.dariomartin.crossapp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.Equipment;
import com.dariomartin.crossapp.model.enums.ExerciseType;
import com.dariomartin.crossapp.model.enums.Muscle;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.view.presenters.CreateExercisePresenter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateExerciseFragment extends Fragment {

    @BindView(R.id.exercise_name) EditText name;
    @BindView(R.id.exercise_description) EditText description;
    @BindView(R.id.material_grid) LinearLayout materialGrid;
    @BindView(R.id.muscles_grid) LinearLayout musclesGrid;
    @BindView(R.id.type_spinner) MaterialSpinner typeSpinner;
    @BindView(R.id.add_exercise) TextView addButton;

    private CreateExercisePresenter presenter;

    public CreateExerciseFragment() {
    }

    public static CreateExerciseFragment newInstance() {
        CreateExerciseFragment fragment = new CreateExerciseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_exercise_fragment, container, false);
        ButterKnife.bind(this, view);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercise();
            }
        });

        presenter = new CreateExercisePresenter();

        addNewMuscleSpinner();

        addNewMaterialSpinner();

        buildTypeSpinner();

        return view;
    }

    private void buildTypeSpinner() {
        ArrayList<String> types = new ArrayList<>();
        for (ExerciseType type : ExerciseType.values()) {
            types.add(getString(type.getName()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);
        typeSpinner.setSelectedIndex(0);
    }

    private void buildMuscleSpinner(MaterialSpinner spinner) {
        final ArrayList<String> muscles = new ArrayList<>();
        muscles.add(getString(R.string.select_one));
        for (Muscle muscle : Muscle.values()) {
            muscles.add(getString(muscle.getName()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, muscles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            boolean hasAlreadySelectedAnOption = false;

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (!hasAlreadySelectedAnOption && position > 0) {
                    hasAlreadySelectedAnOption = true;
                    addNewMuscleSpinner();
                } else if (hasAlreadySelectedAnOption && position == 0 && musclesGrid.getChildCount() > 1) {
                    musclesGrid.removeView(view);
                }
            }

        });
    }

    private void buildEquipmentSpinner(MaterialSpinner spinner) {
        ArrayList<String> equipmentList = new ArrayList<>();
        equipmentList.add(getString(R.string.select_one));
        for (Equipment equipment : Equipment.values()) {
            equipmentList.add(getString(equipment.getName()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, equipmentList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            boolean hasAlreadySelectedAnOption = false;

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (!hasAlreadySelectedAnOption && position > 0) {
                    hasAlreadySelectedAnOption = true;
                    addNewMaterialSpinner();
                } else if (hasAlreadySelectedAnOption && position == 0 && materialGrid.getChildCount() > 1) {
                    materialGrid.removeView(view);
                }
            }
        });
    }

    private void addExercise() {
        if (isCorrect()) {
            DetailedExercise exercise = new DetailedExercise(getName());
            exercise.setDescription(getDescription());
            exercise.setEquipment(getEquipment());
            exercise.setMuscles(getMuscles());
            exercise.setType(getType());
            presenter.addExercise(exercise);

            clearFields();
        }
    }

    private void clearFields() {
        name.getText().clear();

        description.getText().clear();

        materialGrid.removeAllViews();
        addNewMaterialSpinner();

        musclesGrid.removeAllViews();
        addNewMuscleSpinner();

        typeSpinner.setSelectedIndex(0);
    }

    private void addNewMaterialSpinner() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        MaterialSpinner spinner = factory.inflate(R.layout.material_spinner, null).findViewById(R.id.spinner);
        if(spinner.getParent()!=null){
            ((ViewGroup)spinner.getParent()).removeView(spinner);
        }
        materialGrid.addView(spinner);
        buildEquipmentSpinner(spinner);
    }

    private void addNewMuscleSpinner() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        MaterialSpinner spinner = factory.inflate(R.layout.material_spinner, null).findViewById(R.id.spinner);
        if(spinner.getParent()!=null){
            ((ViewGroup)spinner.getParent()).removeView(spinner);
        }
        musclesGrid.addView(spinner);
        buildMuscleSpinner(spinner);
    }

    private boolean isCorrect() {
        return !name.getText().toString().isEmpty();
    }

    private String getName() {
        return name.getText().toString();
    }

    private String getDescription() {
        return description.getText().toString();
    }

    private List<Equipment> getEquipment() {
        List<Equipment> equipment = new ArrayList<>();

        for (int i = 0; i < materialGrid.getChildCount(); i++) {
            View view = materialGrid.getChildAt(i);
            if (view instanceof MaterialSpinner) {
                MaterialSpinner spinner = (MaterialSpinner) view;
                int position = spinner.getSelectedIndex() - 1;
                if (position >= 0) equipment.add(Equipment.values()[position]);
            }
        }

        return equipment;
    }

    private List<Muscle> getMuscles() {
        List<Muscle> muscles = new ArrayList<>();

        for (int i = 0; i < musclesGrid.getChildCount(); i++) {
            View view = musclesGrid.getChildAt(i);
            if (view instanceof MaterialSpinner) {
                MaterialSpinner spinner = (MaterialSpinner) view;
                int position = spinner.getSelectedIndex() - 1;
                if (position >= 0) muscles.add(Muscle.values()[position]);
            }
        }

        return muscles;
    }

    private ExerciseType getType() {
        int position = typeSpinner.getSelectedIndex();
        return ExerciseType.values()[position];
    }
    
}
