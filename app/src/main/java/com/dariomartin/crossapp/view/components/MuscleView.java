package com.dariomartin.crossapp.view.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.Muscle;

/**
 * Created by dariomartin on 7/9/17.
 */

public class MuscleView extends LinearLayout {
    private TextView name;
    private ImageView icon;

    public MuscleView(Context context) {
        super(context);
        init();
    }

    public MuscleView(Context context, Muscle muscle) {
        super(context);
        init();
        setMuscle(muscle);
    }

    public MuscleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.muscle_view, this, true);

        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
    }

    private void setMuscle(Muscle muscle) {
        icon.setImageDrawable(getResources().getDrawable(muscle.getIcon()));
        name.setText(muscle.getName());
    }


}
