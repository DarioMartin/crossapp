package com.dariomartin.crossapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.view.listeners.LogoutListener;
import com.dariomartin.crossapp.view.presenters.StatsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class StatsFragment extends Fragment {

    @BindView(R.id.points_value)
    TextView pointsValue;
    @BindView(R.id.hours_value)
    TextView hoursValue;
    @BindView(R.id.sessions_value)
    TextView sessionsValue;
    @BindView(R.id.chart)
    LineChartView chart;


    private StatsPresenter presenter;
    private LogoutListener listener;
    private LineChartData lineChartData;
    private boolean isCubic = true;
    private Context context;

    public StatsFragment() {
    }

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new StatsPresenter(this);
        presenter.loadLastSessions(30);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LogoutListener) {
            listener = (LogoutListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LogoutListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.statistics_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                listener.logout();
                break;
        }
        return true;
    }

    public void paintSessions(int sessions) {
        sessionsValue.setText(String.valueOf(sessions));
    }

    public void paintHours(long hours) {
        hoursValue.setText(String.valueOf(hours));
    }

    public void paintPoints(int points) {
        pointsValue.setText(String.valueOf(points));
    }

    public void updateChart(List<Pair<Float, Float>> data) {
        lineChartData = new LineChartData(generateLineData(data));
        chart.setZoomEnabled(false);
        chart.setLineChartData(lineChartData);
        chart.setVisibility(View.VISIBLE);
    }

    private List<Line> generateLineData(List<Pair<Float, Float>> data) {

        List<Line> lines = new ArrayList<>();

        List<PointValue> values = new ArrayList<>();

        for (Pair<Float, Float> day : data) {
            values.add(new PointValue(day.first, day.second));
        }

        Line line = new Line(values);
        line.setColor(context.getResources().getColor(R.color.colorAccent));
        line.setCubic(isCubic);
        line.setHasLines(true);
        line.setAreaTransparency(125);
        line.setFilled(true);
        line.setHasPoints(false);

        lines.add(line);

        return lines;
    }

}
