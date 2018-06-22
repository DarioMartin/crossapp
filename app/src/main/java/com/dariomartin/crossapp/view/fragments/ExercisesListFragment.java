package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.ExerciseType;
import com.dariomartin.crossapp.model.exercise.DetailedExercise;
import com.dariomartin.crossapp.view.adapters.ExerciseListAdapter;
import com.dariomartin.crossapp.view.listeners.ExerciseListener;
import com.dariomartin.crossapp.view.presenters.ExercisesListPresenter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExercisesListFragment extends Fragment {

    private static final String CURRENT_ITEM = "CURRENT_ITEM";
    private static final String CURRENT_TYPE = "CURRENT_TYPE";

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.spinner)
    MaterialSpinner typeSpinner;
    @BindView(R.id.message_view)
    LinearLayout messageView;
    @BindView(R.id.title)
    TextView messageTitle;
    @BindView(R.id.subtitle)
    TextView messageSubtitle;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ExerciseListener listener;
    private ExerciseListAdapter adapter;
    private ExercisesListPresenter presenter;
    private boolean isTablet;
    private int currentItem = 0;

    public ExercisesListFragment() {
    }

    public static ExercisesListFragment newInstance() {
        ExercisesListFragment fragment = new ExercisesListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ButterKnife.bind(this, view);

        isTablet = getContext().getResources().getBoolean(R.bool.isTablet);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExerciseListAdapter(listener, isTablet);
        recyclerView.setAdapter(adapter);

        presenter = new ExercisesListPresenter(this);
        presenter.getAllExercises();

        buildTypeSpinner();

        recoverState(savedInstanceState);

        return view;
    }

    private void recoverState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int selectedItem = savedInstanceState.getInt(CURRENT_ITEM);
            int currentType = savedInstanceState.getInt(CURRENT_TYPE);

            selectType(currentType);
            if (selectedItem >= 0) currentItem = selectedItem;

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exercise_search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                presenter.searchExercises(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.searchExercises(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void buildTypeSpinner() {
        List<String> types = new ArrayList<>();

        for (ExerciseType type : ExerciseType.values()) {
            types.add(getString(type.getName()));
        }

        types.add(0, getString(R.string.all_types));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);
        typeSpinner.setSelectedIndex(0);

        typeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectType(position);
            }
        });
    }

    private void selectType(int position) {
        if (position == 0) {
            presenter.getAllExercises();
        } else {
            ExerciseType typeSelected = ExerciseType.values()[position - 1];
            presenter.getExercisesByType(typeSelected);
        }
        
        if (isTablet) adapter.selectItem(0);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_ITEM, adapter.getCurrentItem());
        outState.putInt(CURRENT_TYPE, typeSpinner.getSelectedIndex());
        super.onSaveInstanceState(outState);
    }

    public void paintExercises(List<DetailedExercise> exercises) {
        progressBar.setVisibility(View.GONE);
        adapter.setExercises(exercises);

        if (!exercises.isEmpty()) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setVisibility(View.VISIBLE);
            messageTitle.setText(R.string.no_exercises_title);
            messageSubtitle.setText(R.string.no_exercises_subtitle);
        }

        if (isTablet) {
            adapter.selectItem(currentItem);
        }
    }

    public void showError() {
        // TODO: 31/1/18 SHOW ERROR MESSAGE
    }

}
