/*
 * Copyright (C) 2013 Onur-Hayri Bakici
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

package com.thehayro.view;

import static com.thehayro.internal.Constants.ADAPTER_STATE;
import static com.thehayro.internal.Constants.LOG_TAG;
import static com.thehayro.internal.Constants.PAGE_POSITION_CENTER;
import static com.thehayro.internal.Constants.PAGE_POSITION_LEFT;
import static com.thehayro.internal.Constants.PAGE_POSITION_RIGHT;
import static com.thehayro.internal.Constants.SUPER_STATE;

import com.thehayro.internal.Constants;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * ViewPager that allows infinite scrolling.
 */
public class InfiniteViewPager extends ViewPager {

    private int mCurrPosition;

    public InfiniteViewPager(Context context) {
        this(context, null);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final InfinitePagerAdapter adapter = (InfinitePagerAdapter) getAdapter();
        if (adapter == null) {
            Log.d(LOG_TAG, " onSaveInstanceState adapter == null");
            return super.onSaveInstanceState();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        bundle.putString(ADAPTER_STATE, adapter.getStringRepresentation(adapter.getCurrentIndicator()));

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        final InfinitePagerAdapter adapter = (InfinitePagerAdapter) getAdapter();
        if (adapter == null) {
            if (Constants.DEBUG) {
                Log.w(LOG_TAG, "onRestoreInstanceState adapter == null");
            }
            super.onRestoreInstanceState(state);
            return;
        }
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            final String representation = bundle.getString(ADAPTER_STATE);
            final Object c = adapter.convertToIndicator(representation);
            adapter.setCurrentIndicator(c);
            super.onRestoreInstanceState(bundle.getParcelable(SUPER_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void initInfiniteViewPager() {
        setCurrentItem(PAGE_POSITION_CENTER);
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrPosition = position;
                if (Constants.DEBUG) {
                    Log.d("InfiniteViewPager", "on page " + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                final InfinitePagerAdapter adapter = (InfinitePagerAdapter) getAdapter();
                if (adapter == null) {
                    return;
                }

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mCurrPosition == PAGE_POSITION_LEFT) {
                        adapter.movePageContents(PAGE_POSITION_CENTER, PAGE_POSITION_RIGHT);
                        adapter.movePageContents(PAGE_POSITION_LEFT, PAGE_POSITION_CENTER);
                        adapter.setCurrentIndicator(adapter.getPreviousIndicator());
                        adapter.fillPage(PAGE_POSITION_LEFT);
                    } else if (mCurrPosition == PAGE_POSITION_RIGHT) {
                        adapter.movePageContents(PAGE_POSITION_CENTER, PAGE_POSITION_LEFT);
                        adapter.movePageContents(PAGE_POSITION_RIGHT, PAGE_POSITION_CENTER);
                        adapter.setCurrentIndicator(adapter.getNextIndicator());
                        adapter.fillPage(PAGE_POSITION_RIGHT);
                    }
                    setCurrentItem(PAGE_POSITION_CENTER, false);
                }
            }
        });
    }

    @Override
    public final void setCurrentItem(final int item) {
        throw new RuntimeException("Cannot perform setCurrentItem on infiniteViewPager");
    }

    /**
     *
     * @param indicator
     */
    public final void setCurrentItem(final Object indicator) {
        final PagerAdapter adapter = getAdapter();
        if (adapter == null) {
            return;
        }
        final InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) adapter;
        final Object currentIndicator = infinitePagerAdapter.getCurrentIndicator();
        if (currentIndicator.getClass() != indicator.getClass()) {
            return;
        }
        infinitePagerAdapter.reset();
        infinitePagerAdapter.setCurrentIndicator(indicator);
        for (int i = 0; i < Constants.PAGE_COUNT; i++) {
            infinitePagerAdapter.fillPage(i);
        }

    }

    @Override
    public final void setOffscreenPageLimit(final int limit) {
        if (limit != getOffscreenPageLimit()) {
            throw new RuntimeException("OffscreenPageLimit cannot be changed.");
        }
        super.setOffscreenPageLimit(limit);
    }

    @Override
    public void setAdapter(final PagerAdapter adapter) {
        if (adapter instanceof InfinitePagerAdapter) {
            super.setAdapter(adapter);
            initInfiniteViewPager();
        } else {
            throw new IllegalArgumentException("Adapter should be an instance of InfinitePagerAdapter.");
        }
    }
}
