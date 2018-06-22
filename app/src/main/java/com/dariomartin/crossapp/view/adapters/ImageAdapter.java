package com.dariomartin.crossapp.view.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dariomartin.crossapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dariomartin on 27/10/17.
 */

public class ImageAdapter extends PagerAdapter{
    private ArrayList<String> imageUrls = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;


    public ImageAdapter(Context context,ArrayList<String> imageUrls) {
        this.context = context;
        if (imageUrls != null) this.imageUrls.addAll(imageUrls);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_media_item, view, false);
        final ImageView imageView = imageLayout.findViewById(R.id.exercise_image);
        Picasso.get().load(imageUrls.get(position)).into(imageView);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
