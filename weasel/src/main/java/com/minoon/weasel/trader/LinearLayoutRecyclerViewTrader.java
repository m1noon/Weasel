package com.minoon.weasel.trader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by a13587 on 15/06/27.
 */
public class LinearLayoutRecyclerViewTrader implements TouchEventTrader {
    private static final String TAG = LinearLayoutRecyclerViewTrader.class.getSimpleName();

    RecyclerView mRecyclerView;

    public LinearLayoutRecyclerViewTrader(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public boolean stealTouchEventForChild() {
        if(mRecyclerView == null) {
            return false;
        }
        LinearLayoutManager lm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        return lm.findFirstCompletelyVisibleItemPosition() != 0;
    }

    @Override
    public void scrollBy(int dx, int dy) {
        if (mRecyclerView == null) {
            return;
        }
        mRecyclerView.scrollBy(dx, dy);
    }
}
