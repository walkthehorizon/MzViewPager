package com.shentu.mzviewpager.transformer;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class ScaleYTransformer implements ViewPager.PageTransformer {
    private final String TAG = ScaleYTransformer.class.getSimpleName();

    private static final float MIN_SCALE = 0.8F;
    private static final float MAX_SCALE = 1.0f;
    private static final float MAX_TRANSLATION_Z = 20;
    private static final float MIN_TRANSLATION_Z = 10;
    @Override
    public void transformPage(View page, float position) {
        if (position > -1 && position < 1) {
            float scale = Math.max(MIN_SCALE, MAX_SCALE - Math.abs(position) * (MAX_SCALE - MIN_SCALE));
            page.setScaleY(scale);
            page.setScaleX(scale);
            float transZ = Math.max(MIN_TRANSLATION_Z, MAX_TRANSLATION_Z - Math.abs(position)
                    * (MAX_TRANSLATION_Z - MIN_TRANSLATION_Z));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                page.setTranslationZ(transZ);
            }
            if (position == 0) {
                Log.e(TAG, "transformPage执行scale 被执行者：" + page.toString());
            }
        } else {
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                page.setTranslationZ(MIN_TRANSLATION_Z);
            }
        }
    }

}
