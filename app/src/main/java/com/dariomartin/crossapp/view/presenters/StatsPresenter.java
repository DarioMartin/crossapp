package com.dariomartin.crossapp.view.presenters;

import android.os.AsyncTask;

import com.dariomartin.crossapp.controllers.DataBaseController;
import com.dariomartin.crossapp.controllers.IDataBaseController;
import com.dariomartin.crossapp.model.TrainingSession;
import com.dariomartin.crossapp.view.fragments.StatsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import android.util.Pair;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dariomartin on 19/9/17.
 */

public class StatsPresenter {

    private IDataBaseController dataBaseController;
    private StatsFragment view;
    private List<TrainingSession> sessions = new ArrayList<>();

    public StatsPresenter(StatsFragment view) {
        this.view = view;
        dataBaseController = DataBaseController.getInstance();
    }

    public void loadLastSessions(int days) {

        dataBaseController.getTrainingSessions(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sessions.clear();

                Calendar pastDate = Calendar.getInstance();
                pastDate.setTime(new Date());
                pastDate.add(Calendar.DATE, -30);

                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    TrainingSession session = noteSnapshot.getValue(TrainingSession.class);
                    if (session.getDate() >= pastDate.getTime().getTime()) sessions.add(session);
                }

                countPoints();
                countHours();
                countSessions();

                new MergeSessionsAsyncTask().execute(sessions);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void countSessions() {
        view.paintSessions(sessions.size());
    }

    private void countHours() {
        int milliseconds = 0;
        for (TrainingSession session : sessions) {
            milliseconds += session.getDuration();
        }
        view.paintHours(TimeUnit.MILLISECONDS.toHours(milliseconds));
    }

    private void countPoints() {
        int points = 0;
        for (TrainingSession session : sessions) {
            points += session.getPoints();
        }
        view.paintPoints(points);
    }

    private class MergeSessionsAsyncTask extends AsyncTask<List<TrainingSession>, Void, List<Pair<Float, Float>>> {
        @Override
        protected List<Pair<Float, Float>> doInBackground(List<TrainingSession>[] sessions) {

            // TODO: 22/4/18 in future versions make a deeper merge of the sessions

            List<Pair<Float, Float>> data = new ArrayList<>();

            for (int i = 30; i >= 0; i--) {
                Calendar referenceDate = Calendar.getInstance();
                referenceDate.setTime(new Date());
                referenceDate.add(Calendar.DATE, -i);
                int referenceDay = referenceDate.get(Calendar.DAY_OF_MONTH);
                int referenceMonth = referenceDate.get(Calendar.MONTH);
                float points = 0;

                for (TrainingSession session : sessions[0]) {
                    Calendar sessionDate = Calendar.getInstance();
                    sessionDate.setTime(new Date(session.getDate()));
                    int sessionDay = sessionDate.get(Calendar.DAY_OF_MONTH);
                    int sessionMonth = sessionDate.get(Calendar.MONTH);

                    if (sessionDay == referenceDay && sessionMonth == referenceMonth) {
                        points += session.getPoints();
                    }
                }

                data.add(new Pair<>((float) referenceDay, points));

            }

            return data;
        }

        @Override
        protected void onPostExecute(List<Pair<Float, Float>> data) {
            view.updateChart(data);
        }
    }

}
