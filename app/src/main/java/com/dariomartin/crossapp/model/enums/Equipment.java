package com.dariomartin.crossapp.model.enums;

import com.dariomartin.crossapp.R;

/**
 * Created by dariomartin on 6/9/17.
 */

public enum Equipment {
    DUMBBELL(R.string.dumbbell, R.drawable.ic_equip_dumbbell),
    KETTLEBELL(R.string.kettlebell, R.drawable.ic_equip_kettlebell),
    BARBELL(R.string.barbell, R.drawable.ic_equip_wl_bar),
    HIGH_BAR(R.string.high_bar, R.drawable.ic_equip_high_bar),
    BOX(R.string.box, R.drawable.ic_equip_box),
    NONE(R.string.none, R.drawable.ic_equip_none);

    private final int drawable;
    private int name;

    Equipment(int name, int drawable) {
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
