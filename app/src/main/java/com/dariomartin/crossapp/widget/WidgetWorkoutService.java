package com.dariomartin.crossapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import com.dariomartin.crossapp.model.Part;

/**
 * Created by dariomartin on 30/10/17.
 */

public class WidgetWorkoutService extends IntentService {
    public static final String ACTION_UPDATE_TRAINING_SESSION = "UPDATE_TRAINING_SESSION";
    public static final String PART = "PART";

    public WidgetWorkoutService() {
        super("WidgetWorkoutService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_TRAINING_SESSION.equals(action)) {
                Part part = intent.getExtras().getParcelable(PART);
                updateWorkoutWidget(part);
            }else{
                updateWorkoutWidget(null);
            }
        }
    }

    private void updateWorkoutWidget(Part part) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        WidgetProvider.updateTrainingSession(this, appWidgetManager, part, appWidgetIds);
    }

}
