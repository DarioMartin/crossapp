package com.dariomartin.crossapp.view.activities;

import android.support.v4.app.Fragment;
import android.view.View;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.view.fragments.AdminFragment;

/**
 * Created by dariomartin on 18/9/17.
 */

public class FlavorMainActivity extends MainActivity {

    private static final String ADMIN_FRAGMENT = "ADMIN_FRAGMENT";
    public static final String CREATE_EXERCISE_FRAGMENT = "CREATE_EXERCISE_FRAGMENT";

    @Override
    protected void updateFlavoredLayouts(int itemId) {
        if (isTablet) {
            switch (itemId) {
                case R.id.navigation_admin:
                    findViewById(R.id.content_detail).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected boolean loadFlavoredViews(int itemId) {
        switch (itemId) {
            case R.id.navigation_admin:
                loadAdminView();
                return true;
        }
        return false;
    }

    private void loadAdminView() {
        String tag = CREATE_EXERCISE_FRAGMENT;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CREATE_EXERCISE_FRAGMENT);

        if (fragment == null) {
            fragment = getSupportFragmentManager().findFragmentByTag(ADMIN_FRAGMENT);
            tag = ADMIN_FRAGMENT;
        }

        if (fragment == null) {
            fragment = AdminFragment.newInstance();
        }

        loadFragment(fragment, tag);
    }
}
