package com.shentu.sample;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 神荼 on 2017/10/26.
 */

public class NormalPagerAdapter extends PagerAdapter {

    public NormalPagerAdapter() {
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_mz, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        imageView.setImageResource(mImageList.get(position));
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private List<Integer> mImageList = Arrays.asList(
            R.drawable.ic_img_1,
            R.drawable.ic_img_2,
            R.drawable.ic_img_3,
            R.drawable.ic_img_4
    );
}
