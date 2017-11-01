package com.shentu.mzviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.shentu.mzviewpager.loopviewpager.LoopViewPager;
import com.shentu.mzviewpager.transformer.ScaleYTransformer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 神荼 on 2017/10/27.
 */

public class MzViewPager extends FrameLayout {
    public static final String TAG = MzViewPager.class.getSimpleName();

    private static final int MODE_MZ_PADDING = -1;
    private static final int MODE_MZ_CHILDREN = -1;
    private static final int DEFAULT_VP_PADDING = 28;
    private static final int DEFAULT_VP_MARGIN = 28;

    private Context mContext;

    private LoopViewPager mViewPager;
    private int mZMode = -1;//-1-->padding,1-->children
    private boolean mIsLoop = false;
    private boolean mAutoPlay = false;
    private boolean mIsCover = true;

    private int mVpPadding = 0;//viewPager padding
    private int mVpMargin = 0;//viewpager margin
    private int mPageMargin;//单个page之间的margin

    private Timer mTimer;

    public MzViewPager(@NonNull Context context) {
        this(context, null);
    }

    public MzViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MzViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleTypedArray(context, attrs);

        mVpMargin = dp2px(DEFAULT_VP_MARGIN);
        mVpPadding = dp2px(DEFAULT_VP_PADDING);

        mContext = context;
        initViewPager();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MzViewPager);
        mZMode = typedArray.getInteger(R.styleable.MzViewPager_mz_mode, -1);
        mIsLoop = typedArray.getBoolean(R.styleable.MzViewPager_is_loop, false);
        mAutoPlay = typedArray.getBoolean(R.styleable.MzViewPager_auto_play, false);
        mIsCover = typedArray.getBoolean(R.styleable.MzViewPager_is_cover, true);
        typedArray.recycle();
    }

    private void initViewPager() {
        mViewPager = new LoopViewPager(mContext);
        mViewPager.setBoundaryLooping(mIsLoop);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.MATCH_PARENT);
        if (mZMode == MODE_MZ_PADDING) {
            mViewPager.setClipToPadding(false);
            mViewPager.setPadding(mVpPadding, 0, mVpPadding, 0);
            mPageMargin = -dp2px(DEFAULT_VP_PADDING * (mIsCover ? 1.8f : 0.8f));
            mViewPager.setPageMargin(mPageMargin);
        } else {
            setClipChildren(false);
            lp.setMargins(mVpMargin, 0, mVpMargin, 0);
            mPageMargin = -dp2px(DEFAULT_VP_MARGIN * (mIsCover ? 1.8f : 0.8f));
            mViewPager.setPageMargin(mPageMargin);
        }
        mViewPager.setPageTransformer(false, new ScaleYTransformer());
        mViewPager.setOffscreenPageLimit(3);
        addView(mViewPager, lp);
    }

    private void initTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Timer子线程更新UI
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (mViewPager.getRealCurrentItem() == mViewPager.getRealAdapter().getCount() - 1) {
                            mViewPager.setCurrentItem(mViewPager.getItemFirstAppearPosition(0), false);
                        } else {
                            mViewPager.setCurrentItem(mViewPager.getRealCurrentItem() + 1, true);
                        }
                    }
                });
            }
        }, 0, 3000);
    }

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
        if (mAutoPlay && mViewPager.getAdapter() != null) {
            initTimer();
        }
    }

    public void setIsLoop(boolean isLoop) {
        mIsLoop = isLoop;
        initViewPager();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);
        if (mAutoPlay && mViewPager != null) {
            initTimer();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
