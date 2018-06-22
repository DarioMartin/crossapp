package com.dariomartin.crossapp.view.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dariomartin.crossapp.R;

import butterknife.ButterKnife;

/**
 * Created by dariomartin on 21/3/18.
 */

public class BaseActivity extends AppCompatActivity {

    protected boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = getBaseContext().getResources().getBoolean(R.bool.isTablet);
        setRequestedOrientation(isTablet ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
