package com.thehayro.view;

import android.support.annotation.NonNull;

/**
 * Adapter class for easier value comparison.
 */
public abstract class ComparableInfiniteViewPagerAdapter<C extends Comparable> extends InfinitePagerAdapter<C> {
    /**
     * Standard constructor.
     *
     * @param initValue the initial indicator value the ViewPager should start with.
     */
    public ComparableInfiniteViewPagerAdapter(@NonNull C initValue) {
        super(initValue);
    }

    @Override
    public boolean isLesserThanMinValue(@NonNull C value) {
        C minValue = getMinValue();
        if (minValue != null) {
            return value.compareTo(minValue) < 0;
        }
        return false;
    }

    @Override
    public boolean isGreaterThanMaxValue(@NonNull C value) {
        C maxValue = getMaxValue();
        if (maxValue != null) {
            return value.compareTo(maxValue) > 0;
        }
        return false;
    }
}
