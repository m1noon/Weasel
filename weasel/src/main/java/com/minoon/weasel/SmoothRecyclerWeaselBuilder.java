package com.minoon.weasel;

import android.support.v7.widget.RecyclerView;

/**
 * Created by a13587 on 15/08/08.
 */
/* package */ class SmoothRecyclerWeaselBuilder extends SmoothWeaselBuilder {
    private static final String TAG = SmoothRecyclerWeaselBuilder.class.getSimpleName();

    RecyclerView mRecyclerView;

    public SmoothRecyclerWeaselBuilder(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    protected void addWeaselToScrollView(Weasel weasel) {
        mRecyclerView.addOnScrollListener(new RecyclerWeaselConnector(weasel));
    }
}
