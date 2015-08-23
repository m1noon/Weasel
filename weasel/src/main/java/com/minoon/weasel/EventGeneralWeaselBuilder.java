package com.minoon.weasel;

/**
 * Created by a13587 on 15/08/08.
 */
/* package */ class EventGeneralWeaselBuilder extends EventWeaselBuilder {
    private static final String TAG = EventGeneralWeaselBuilder.class.getSimpleName();

    ScrollableView mScrollableView;

    /* package */ EventGeneralWeaselBuilder(ScrollableView scrollableView) {
        if (scrollableView == null) {
            throw new NullPointerException("ScrollableView is null.");
        }
        mScrollableView = scrollableView;
    }

    @Override
    protected void addWeaselToScrollView(Weasel weasel) {
        weasel.addChaseView(mScrollableView);
    }
}
