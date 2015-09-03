package com.minoon.weasel;

/**
 * Created by a13587 on 15/08/08.
 */
/* package */ class EventGeneralWeaselBuilder<E extends Enum> extends EventWeaselBuilder {
    private static final String TAG = EventGeneralWeaselBuilder.class.getSimpleName();

    ScrollableView<E> mScrollableView;

    /* package */ EventGeneralWeaselBuilder(ScrollableView<E> scrollableView) {
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
