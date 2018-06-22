package com.dariomartin.crossapp.view.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dariomartin.crossapp.R;
import com.dariomartin.crossapp.model.enums.Equipment;

/**
 * Created by dariomartin on 7/9/17.
 */

public class MaterialView extends LinearLayout {
    private TextView name;
    private ImageView icon;

    public MaterialView(Context context) {
        super(context);
        init();
    }

    public MaterialView(Context context, Equipment equipment){
        super(context);
        init();
        setMaterial(equipment);
    }

    public MaterialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.equipment_view, this, true);

        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
    }

    private void setMaterial(Equipment equipment) {
        icon.setImageDrawable(getResources().getDrawable(equipment.getIcon()));
        name.setText(equipment.getName());
    }


}
