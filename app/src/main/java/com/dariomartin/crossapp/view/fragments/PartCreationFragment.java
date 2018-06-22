package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.model.enums.PartDurationUnit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dariomartin on 14/10/17.
 */

public class PartCreationFragment extends Fragment {

    @BindView(R.id.part_name)
    EditText partNameEditText;
    @BindView(R.id.duration_value_edit_text)
    EditText durationValueEditText;
    @BindView(R.id.duration_unit_spinner)
    Spinner durationUnitSpinner;
    @BindView(R.id.accept)
    TextView acceptButton;
    @BindView(R.id.cancel)
    TextView cancelButton;

    private PartCreationListener listener;

    public interface PartCreationListener {
        void onPartCreated(Part part);
    }

    public PartCreationFragment() {
    }

    public static PartCreationFragment newInstance() {
        PartCreationFragment instance = new PartCreationFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part_creation, container, false);
        ButterKnife.bind(this, view);

        buildNameEditText();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPart();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        durationUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PartDurationUnit unit = PartDurationUnit.values()[i];
                if (unit == PartDurationUnit.PROGRESSION_21_15_9) {
                    durationValueEditText.setText("1");
                    durationValueEditText.setEnabled(false);
                } else {
                    durationValueEditText.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void buildNameEditText() {
        partNameEditText.addTextChangedListener(new TextWatcher() {
            public CharSequence name;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                acceptButton.setEnabled(name.length() > 0);
            }
        });
    }

    private void createPart() {
        if (isValidPart()) {
            Part part = new Part(partNameEditText.getText().toString());
            part.setDurationValue(getDurationValue());
            part.setDurationUnit(getDurationUnit());
            closeKeyboard();
            listener.onPartCreated(part);
        }
    }

    private Integer getDurationValue() {
        return Integer.valueOf(durationValueEditText.getText().toString());
    }

    private PartDurationUnit getDurationUnit() {
        int position = durationUnitSpinner.getSelectedItemPosition();
        PartDurationUnit unit = PartDurationUnit.values()[position];
        return unit;
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    private boolean isValidPart() {
        return !partNameEditText.getText().toString().isEmpty();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PartCreationListener) {
            listener = (PartCreationListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PartCreationListener");
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
        configureIntensityUnits();
    }

    private void configureIntensityUnits() {
        ArrayList<String> durationUnits = new ArrayList<>();
        for (PartDurationUnit durationUnit : PartDurationUnit.values()) {
            durationUnits.add(durationUnit.getName(getContext()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, durationUnits);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        durationUnitSpinner.setAdapter(dataAdapter);
        durationUnitSpinner.setSelection(0);
    }
}
