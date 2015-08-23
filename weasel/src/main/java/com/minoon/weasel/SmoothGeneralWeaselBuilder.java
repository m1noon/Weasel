package com.minoon.weasel;

/**
 * Created by a13587 on 15/08/08.
 */
/* package */ class SmoothGeneralWeaselBuilder extends SmoothWeaselBuilder {
    private static final String TAG = SmoothGeneralWeaselBuilder.class.getSimpleName();

    ScrollableView mScrollableView;

    public SmoothGeneralWeaselBuilder(ScrollableView scrollableView) {
        mScrollableView = scrollableView;
    }

    @Override
    protected void addWeaselToScrollView(Weasel weasel) {
        weasel.addChaseView(mScrollableView);
    }
}
