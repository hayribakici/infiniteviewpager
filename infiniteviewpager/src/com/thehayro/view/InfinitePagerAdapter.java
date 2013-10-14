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

import static com.thehayro.internal.Constants.PAGE_COUNT;

import com.thehayro.internal.Constants;
import com.thehayro.internal.PageModel;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base class providing the adapter to populate inside of a {@link com.thehayro.view.InfiniteViewPager}.
 * The indication for each page is up the implementation. Meaning that it is up to the implementation what the next
 * and previous page indication is. This is more generic than the regular PagerAdapter
 * implementation, where the pages are indicated by its index and accessed through
 * {@link android.support.v4.view.ViewPager#setCurrentItem(int, boolean)}.
 * <p></p>
 *
 * When you implement an adapter you must implement the following methods:
 * <ul>
 *     <li>{@link com.thehayro.view.InfinitePagerAdapter#instantiateItem(Object)}</li>
 *     <li>{@link InfinitePagerAdapter#getNextIndicator()}</li>
 *     <li>{@link InfinitePagerAdapter#getPreviousIndicator()}</li>
 * </ul>
 * @param <T> an indicator datatype to distinguish the pages.
 */
public abstract class InfinitePagerAdapter<T> extends PagerAdapter {

    private PageModel<T>[] mPageModels;

    private T mCurrentIndicator;

    /**
     * Standard constructor.
     * @param initValue the initial indicator value the ViewPager should start with.
     */
    public InfinitePagerAdapter(T initValue) {
        mCurrentIndicator = initValue;

        mPageModels = new PageModel[PAGE_COUNT];
    }

    /**
     * This method is only called, when this pagerAdapter is initialized.
     */
    @Override
    public final Object instantiateItem(final ViewGroup container, final int position) {
        if (Constants.DEBUG) {
            Log.i("InfiniteViewPager", String.format("instantiating position %s", position));
        }
        final PageModel<T> model = createPageModel(position);
        mPageModels[position] = model;
        container.addView(model.getParentView());
        return model;
    }

    /**
     * fills the page on index {@code position}.
     * @param position the page index to fill the page.
     */
    void fillPage(final int position) {
        if (Constants.DEBUG) {
            Log.d("InfiniteViewPager", "setup Page " + position);
            printPageModels("before newPage");
        }
        final PageModel<T> oldModel = mPageModels[position];
        final PageModel<T> newModel = createPageModel(position);
        if (oldModel == null || newModel == null) {
            Log.w(Constants.LOG_TAG, "fillPage no model found " + oldModel + " " + newModel);
            return;
        }
        // moving the new created views to the page of the viewpager
        oldModel.removeAllChildren();
        for (final View newChild : newModel.getChildren()) {
            newModel.removeViewFromParent(newChild);
            oldModel.addChild(newChild);
        }

        mPageModels[position].setIndicator(newModel.getIndicator());
    }

    /**
     * Creates the internal page model. This method calls the {@link #instantiateItem(Object)} method
     * that creates the page content.
     * @param pagePosition the position in the pageModel array between [0..2]
     * @return a new instance of a page model.
     */
    private PageModel<T> createPageModel(final int pagePosition) {
        final T indicator = getIndicatorFromPagePosition(pagePosition);
        final ViewGroup view = instantiateItem(indicator);

        return new PageModel<T>(view, indicator);
    }

    protected final T getCurrentIndicator() {
        return mCurrentIndicator;
    }

    private T getIndicatorFromPagePosition(final int pagePosition) {
        T indicator = null;
        switch (pagePosition) {
            case Constants.PAGE_POSITION_LEFT:
                indicator = getPreviousIndicator();
                break;
            case Constants.PAGE_POSITION_CENTER:
                indicator = getCurrentIndicator();
                break;
            case Constants.PAGE_POSITION_RIGHT:
                indicator = getNextIndicator();
                break;
        }
        return indicator;
    }

    /**
     * Package internal. Moves contents from page index {@code from} to page index {@code to}.
     * @param from page index to move contents from.
     * @param to page index to move contents to.
     */
    void movePageContents(final int from, final int to) {
        final PageModel<T> fromModel = mPageModels[from];
        final PageModel<T> toModel = mPageModels[to];
        if (fromModel == null || toModel == null) {
            Log.w(Constants.LOG_TAG, "fillPage no model found " + fromModel + " " + toModel);
            return;
        }
        if (Constants.DEBUG) {
            Log.d("InfiniteViewPager",
                String.format("Moving page %s to %s, indicator from %s to %s", from, to,
                fromModel.getIndicator(), toModel.getIndicator()));
            printPageModels("before");
        }


        toModel.removeAllChildren();
        for (View view : fromModel.getChildren()) {
            fromModel.removeViewFromParent(view);
            toModel.addChild(view);
        }

        if (Constants.DEBUG) {
            printPageModels("transfer");
        }
        mPageModels[to].setIndicator(fromModel.getIndicator());
        if (Constants.DEBUG) {
            printPageModels("after");
        }
    }

    void reset() {
        for (PageModel<T> pageModel : mPageModels) {
            pageModel.removeAllChildren();
        }
    }

    /**
     * Sets {@code indicator} as the current visible indicator.
     * @param indicator a indicator value.
     */
    void setCurrentIndicator(final T indicator) {
        mCurrentIndicator = indicator;
    }


    /**
     *
     * @return the next indicator.
     */
    public abstract T getNextIndicator();


    /**
     *
     * @return the previous indicator.
     */
    public abstract T getPreviousIndicator();

    /**
     * Instantiates a page.
     * @param indicator the indicator the page should be instantiated with.
     * @return a ViewGroup containing the page layout.
     */
    public abstract ViewGroup instantiateItem(T indicator);

    /**
     *
     * @param currentIndicator the current value of the indicator.
     * @return a string representation of the current indicator.
     * @see #convertToIndicator(String)
     */
    public String getStringRepresentation(final T currentIndicator) {
        return "";
    }

    /**
     * Convert the represented string back to its indicator
     * @param representation the string representation of the current indicator.
     * @return the indicator.
     */
    public T convertToIndicator(final String representation) {
        return getCurrentIndicator();
    }

    @Override
    public final int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        final PageModel model = (PageModel) object;
        container.removeView(model.getParentView());
    }

    @Override
    public final boolean isViewFromObject(final View view, final Object o) {
        return view == ((PageModel)o).getParentView();
    }

    // Debug related methods

    private void printPageModels(final String tag) {
        for (int i = 0; i < PAGE_COUNT; i++) {
            printPageModel(tag, mPageModels[i], i);
        }
    }

    private void printPageModel(final String tag, final PageModel model, int modelPos) {
        final String builder = String.format("%s: ModelPos %s, indicator %s, " +
            "Childcount %s viewChildCount %s tag %s",
            tag, modelPos,
            model.getIndicator(), model.getChildren().size(), model.getParentView().getChildCount(),
            model.getParentView().getTag());
        Log.d("InfiniteViewPager", builder);
    }


}
