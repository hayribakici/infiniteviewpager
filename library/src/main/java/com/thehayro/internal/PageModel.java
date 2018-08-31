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

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal model of a page
 *
 * @param <T> the datatype of the {@link com.thehayro.view.InfinitePagerAdapter} indicator.
 */
public final class PageModel<T> {
    @NonNull
    private T indicator;

    @NonNull
    private ViewGroup parentView;

    @NonNull
    private List<View> children;

    public PageModel(@NonNull final ViewGroup parent, @NonNull final T indicator) {
        parentView = parent;
        this.indicator = indicator;
        final int size = parent.getChildCount();
        this.children = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            children.add(parent.getChildAt(i));
        }
    }

    private void emptyChildren() {
        if (!children.isEmpty()) {
            children.clear();
        }
    }

    @NonNull
    public List<View> getChildren() {
        return children;
    }

    public void removeAllChildren() {
        parentView.removeAllViews();
        emptyChildren();
    }

    public void addChild(final View child) {
        addViewToParent(child);
        children.add(child);
    }

    public void removeViewFromParent(final View view) {
        parentView.removeView(view);
    }

    private void addViewToParent(final View view) {
        parentView.addView(view);
    }

    @NonNull
    public ViewGroup getParentView() {
        return parentView;
    }

    @NonNull
    public T getIndicator() {
        return indicator;
    }

    public void setIndicator(@NonNull final T indicator) {
        this.indicator = indicator;
    }
}
