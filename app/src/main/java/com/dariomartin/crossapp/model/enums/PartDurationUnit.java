package com.dariomartin.crossapp.model.enums;

import android.content.Context;

import com.dariomartin.crossapp.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dariomartin on 6/9/17.
 */

public enum PartDurationUnit {
    ROUNDS_FOR_TIME(R.string.rounds_for_time, R.string.rounds_for_time_representation),
    IN_MINUTES(R.string.in_minutes, R.string.in_minutes_representation),
    COUNTDOWN(R.string.countdown, R.string.countdown_representation),
    COUNT(R.string.count, R.string.count_representation),
    PROGRESSION_21_15_9(R.string.progression_21_15_9, R.string.progression_21_15_9_representation);

    private final int name;
    private final int representation;

    PartDurationUnit(int name, int representation) {
        this.name = name;
        this.representation = representation;
    }

    public String getName(Context context) {
        return context.getString(name);
    }

    public int getRepresentation(Context context) {
        return representation;
    }
}
