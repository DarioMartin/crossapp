package com.dariomartin.crossapp.model.enums;

import com.dariomartin.crossapp.R;

/**
 * Created by dariomartin on 6/9/17.
 */

public enum Muscle {
    FULL_BODY(R.string.full_body, R.drawable.ic_muscle_body),
    SHOULDERS(R.string.shoulders, R.drawable.ic_muscle_arm),
    BICEPS(R.string.biceps, R.drawable.ic_muscle_arm),
    TRICEPS(R.string.triceps, R.drawable.ic_muscle_arm),
    TRAPEZIUS(R.string.trapezius, R.drawable.ic_muscle_back),
    CHEST(R.string.chest, R.drawable.ic_muscle_chest),
    DORSAL(R.string.dorsal, R.drawable.ic_muscle_back),
    LUMBARS(R.string.lumbars, R.drawable.ic_muscle_back),
    ABS(R.string.abs, R.drawable.ic_muscle_abs),
    GLUTES(R.string.glutes, R.drawable.ic_muscle_leg),
    HAMSTRINGS(R.string.hamstrings, R.drawable.ic_muscle_leg),
    QUADRICEPS(R.string.quadriceps, R.drawable.ic_muscle_leg),
    CALVES(R.string.calves, R.drawable.ic_muscle_leg);

    private final int name;
    private final int icon;

    Muscle(int name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
