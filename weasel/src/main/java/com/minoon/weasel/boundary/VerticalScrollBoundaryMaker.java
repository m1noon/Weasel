package com.minoon.weasel.boundary;

import android.view.View;

/**
 * Created by a13587 on 15/06/27.
 */
public class VerticalScrollBoundaryMaker extends BoundaryMaker {
    private static final String TAG = VerticalScrollBoundaryMaker.class.getSimpleName();

    private static int mTopOffset;

    public VerticalScrollBoundaryMaker(View view, View parent) {
        super(view, parent);
    }

    public void setTopOffset(int topOffset) {
        mTopOffset = topOffset;
    }

    @Override
    public int getTopBounds() {
        return 0;
    }

    @Override
    public int getBottomBounds() {
        return getParentView().getHeight() - mTopOffset;
    }

    @Override
    public int getLeftBounds() {
        return getView().getLeft();
    }

    @Override
    public int getRightBounds() {
        return getView().getLeft();
    }
}
