package com.minoon.weasel;

import android.support.v7.widget.RecyclerView;

/**
 * Created by a13587 on 15/08/08.
 */
/* package */ class EventRecyclerWeaselBuilder extends EventWeaselBuilder<Weasel.WeaselEvent> {
    private static final String TAG = EventRecyclerWeaselBuilder.class.getSimpleName();

    private RecyclerView mRecyclerView;

    /* package */ EventRecyclerWeaselBuilder(RecyclerView recyclerView) {
        super();
        if (recyclerView == null) {
            throw new NullPointerException("recycler view is null.");
        }
        mRecyclerView = recyclerView;
    }

    @Override
    protected void addWeaselToScrollView(Weasel weasel) {
        weasel.addChaseView(mRecyclerView);
    }
}
