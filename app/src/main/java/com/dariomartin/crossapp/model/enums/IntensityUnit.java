package com.dariomartin.crossapp.model.enums;

import android.content.Context;

import com.dariomartin.crossapp.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dariomartin on 6/9/17.
 */

public enum IntensityUnit {
    KG(R.string.kg),
    MINS(R.string.mins),
    MTS(R.string.mts),
    CAL(R.string.cal);

    private final int name;

    IntensityUnit(int name) {
        this.name = name;
    }

    public String getName(Context contex) {
        return contex.getString(name);
    }
}
