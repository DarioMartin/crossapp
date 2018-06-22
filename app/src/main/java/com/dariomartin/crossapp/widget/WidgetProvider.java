package com.dariomartin.crossapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dariomartin.crossapp.CrossApplication;
import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.Part;
import com.dariomartin.crossapp.util.Utils;
import com.dariomartin.crossapp.view.activities.FlavorMainActivity;
import com.dariomartin.crossapp.view.activities.TrainingActivity;

import java.util.ArrayList;

/**
 * Created by dariomartin on 30/10/17.
 */

public class WidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Part part, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_workout);

        String partName;

        if (part != null) {
            partName = part.getName();
            CrossApplication.widgetExercises = Utils.getExercisesDescription(part);

            //Set exercises pending intent
            Intent pendingIntent = new Intent(context, TrainingActivity.class);
            PendingIntent exercisesPendingIntent = PendingIntent.getActivity(context, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_header, exercisesPendingIntent);
        } else {
            partName = context.getString(R.string.app_name);
            CrossApplication.widgetExercises = new ArrayList<>();
            //Set title pending intent
            Intent mainIntent = new Intent(context, FlavorMainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_header, mainPendingIntent);
        }

        //Set title
        views.setTextViewText(R.id.part_name, partName);

        //Set remote views adapter
        Intent rmIntent = new Intent(context, WorkoutRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_list, rmIntent);
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        //Set empty view
        views.setEmptyView(R.id.widget_list, R.id.widget_empty);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, null, appWidgetId);
        }
    }

    public static void updateTrainingSession(Context context, AppWidgetManager appWidgetManager, Part part, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, part, appWidgetId);
        }
    }
}
