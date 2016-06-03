package com.thehayro.view;

import android.support.annotation.NonNull;

/**
 * Created on 04.06.16.
 */
public abstract class ComparableInfiniteViewPgaerAdapter<C extends Comparable> extends InfinitePagerAdapter<C> {
    /**
     * Standard constructor.
     *
     * @param initValue the initial indicator value the ViewPager should start with.
     */
    public ComparableInfiniteViewPgaerAdapter(@NonNull  C initValue) {
        super(initValue);
    }

    @Override
    public boolean isLesserThanMinValue(@NonNull C value) {
        return value.compareTo(getMinValue()) < 0;
    }

    @Override
    public boolean isGreaterThanMaxValue(@NonNull C value) {
        return value.compareTo(getMaxValue()) > 0;
    }
}
