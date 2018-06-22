package com.dariomartin.crossapp.widget;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.dariomartin.crossapp.CrossApplication;
import com.dariomartin.crossapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dariomartin on 30/10/17.
 */

public class WorkoutRemoteViewsService extends android.widget.RemoteViewsService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {

        return new RemoteViewsFactory() {

            public List<String> exercises;

            @Override
            public void onCreate() {
                exercises = new ArrayList<>();

            }

            @Override
            public void onDataSetChanged() {
                exercises = CrossApplication.widgetExercises;
                final long identityToken = Binder.clearCallingIdentity();
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                exercises.clear();
            }

            @Override
            public int getCount() {
                return exercises == null ? 0 : exercises.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {


                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item);
                views.setTextViewText(R.id.exercise, exercises.get(position));
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_item);
            }

            @Override
            public int getViewTypeCount() {
                return exercises.size();
            }

            @Override
            public long getItemId(int position) {
                return position;
            }


            @Override
            public boolean hasStableIds() {
                return true;
            }

        };
    }
}
