package com.minoon.weasel.boundary;

import android.view.View;

/**
 * Created by a13587 on 15/06/27.
 */
public abstract class BoundaryMaker {
    private static final String TAG = BoundaryMaker.class.getSimpleName();

    private final View mView;
    private final View mParent;

    public BoundaryMaker(View mView, View mParent) {
        this.mView = mView;
        this.mParent = mParent;
    }

    protected View getView() {
        return mView;
    }

    protected View getParentView() {
        return mParent;
    }

    public  boolean isAtTop() {
        return mView.getTop() <= getTopBounds();
    }

    public boolean isAtBottom() {
        return mView.getTop() >= getBottomBounds();
    }

    public boolean isAtRight() {
        return mView.getLeft() >= getRightBounds();
    }

    public boolean isAtLeft() {
        return mView.getLeft() <= getLeftBounds();
    }

    public abstract int getTopBounds();

    public abstract int getBottomBounds();

    public abstract int getLeftBounds();

    public abstract int getRightBounds();
}
