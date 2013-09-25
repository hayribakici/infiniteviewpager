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

package com.thehayro.internal;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal model of a page
 * @param <T> the datatype of the {@link com.thehayro.view.InfinitePagerAdapter} indicator.
 */
public final class PageModel<T> {
    private T mIndicator;

    private ViewGroup mParentView;

    private List<View> mChildren;

    public PageModel(final ViewGroup parent, final T indicator) {
        mParentView = parent;
        mIndicator = indicator;
        final int size = parent.getChildCount();
        mChildren = new ArrayList<View>(size);

        for (int i = 0; i < size; i++) {
            mChildren.add(parent.getChildAt(i));
        }
    }

    /**
     *
     * @return {@code true} if the model has child views.
     */
    public boolean hasChildren() {
        return mChildren != null && mChildren.size() != 0;
    }


    private void emptyChildren() {
        if (hasChildren()) {
            mChildren.clear();
        }
    }

    public List<View> getChildren() {
        return mChildren;
    }

    public void removeAllChildren() {
        mParentView.removeAllViews();
        emptyChildren();
    }

    public void addChild(final View child) {
        addViewToParent(child);
        mChildren.add(child);
    }

    public void removeViewFromParent(final View view) {
        mParentView.removeView(view);
    }

    public void addViewToParent(final View view) {
        mParentView.addView(view);
    }

    public ViewGroup getParentView() {
        return mParentView;
    }

    public T getIndicator() {
        return mIndicator;
    }

    public void setIndicator(final T indicator) {
        mIndicator = indicator;
    }
}
