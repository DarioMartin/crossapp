package com.dariomartin.crossapp.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.view.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dariomartin.crossapp.view.activities.FlavorMainActivity.CREATE_EXERCISE_FRAGMENT;

/**
 * Created by dariomartin on 25/10/17.
 */

public class AdminFragment extends Fragment {

    @BindView(R.id.create_exercise)
    TextView createExerciseButton;

    public AdminFragment() {
    }

    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment, container, false);
        ButterKnife.bind(this, view);

        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCreateExerciseView();
            }
        });

        return view;
    }


    private void loadCreateExerciseView() {
        if (getActivity() instanceof MainActivity) {

            MainActivity mainActivity = (MainActivity) getActivity();

            Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(CREATE_EXERCISE_FRAGMENT);

            if (fragment == null) {
                fragment = CreateExerciseFragment.newInstance();
            }
            mainActivity.loadFragment(fragment, CREATE_EXERCISE_FRAGMENT);
        }
    }


}
