/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shentu.mzviewpager.loopviewpager;

import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class LoopPagerAdapterWrapper extends PagerAdapter {

    private final int MAX_COUNT = 400;

    private PagerAdapter mAdapter;

    private SparseArray<ToDestroy> mToDestroy = new SparseArray<>();

    private static final boolean DEFAULT_BOUNDARY_CASHING = false;
    private static final boolean DEFAULT_BOUNDARY_LOOPING = true;

    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    private boolean mBoundaryLooping = DEFAULT_BOUNDARY_LOOPING;

    void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
    }

    void setBoundaryLooping(boolean flag) {
        mBoundaryLooping = flag;
    }

    LoopPagerAdapterWrapper(PagerAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void notifyDataSetChanged() {
        mToDestroy = new SparseArray<>();
        super.notifyDataSetChanged();
    }

    /**
     * @param realPosition 外部适配器的位置
     * @return 对应的内部适配器的真实位置
     */
    public int toInnerPosition(int realPosition) {
        return realPosition % mAdapter.getCount();
    }

    @Override
    public int getCount() {
        return mBoundaryLooping ? MAX_COUNT : mAdapter.getCount();
    }

    public int getInnerCount() {
        return mAdapter.getCount();
    }

    public PagerAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int innerPosition = (mAdapter instanceof FragmentPagerAdapter
                || mAdapter instanceof FragmentStatePagerAdapter) ? position
                : toInnerPosition(position);

        if (mBoundaryCaching) {
            ToDestroy toDestroy = mToDestroy.get(position);
            if (toDestroy != null) {
                mToDestroy.remove(position);
                return toDestroy.object;
            }
        }
        return mAdapter.instantiateItem(container, innerPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int innerPosition = (mAdapter instanceof FragmentPagerAdapter
                || mAdapter instanceof FragmentStatePagerAdapter) ? position
                : toInnerPosition(position);

        if (mBoundaryCaching && mToDestroy.get(innerPosition) == null) {
            mToDestroy.put(position, new ToDestroy(container, innerPosition, object));
        } else {
            mAdapter.destroyItem(container, innerPosition, object);
        }
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
	 */

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

	/*
     * End delegation
	 */

    /**
     * Container class for caching the boundary views
     */
    static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        public ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }
}