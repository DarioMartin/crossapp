package com.dariomartin.crossapp.model.enums;

import com.dariomartin.crossapp.R;

/**
 * Created by dariomartin on 6/9/17.
 */

public enum ExerciseType {
    RES(R.string.resistance, R.drawable.ic_type_resistance),
    STR(R.string.strength, R.drawable.ic_type_strength),
    OTHER(R.string.other, R.drawable.ic_type_other);

    private final int drawable;
    private int name;

    ExerciseType(int name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public int getIcon() {
        return drawable;
    }

    public int getName() {
        return name;
    }
}
