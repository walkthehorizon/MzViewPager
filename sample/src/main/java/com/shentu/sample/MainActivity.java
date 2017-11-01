package com.shentu.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shentu.mzviewpager.MzViewPager;

public class MainActivity extends AppCompatActivity {

    private MzViewPager mViewPager;
    private MzViewPager mCoverViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (MzViewPager) findViewById(R.id.mz_pager);
        mCoverViewPager = (MzViewPager) findViewById(R.id.mz_pager_cover);
        NormalPagerAdapter pagerAdapter = new NormalPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mCoverViewPager.setAdapter(pagerAdapter);
    }

}
