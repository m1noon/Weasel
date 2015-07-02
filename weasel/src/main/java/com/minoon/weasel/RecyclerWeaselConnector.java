package com.minoon.weasel;

import android.support.v7.widget.RecyclerView;

import com.minoon.weasel.util.ScrollOrientationChangeHelper;

/**
 * {@link RecyclerView}のスクロールイベントを検知し、{@link Weasel}の適切な
 * 処理を呼び出すヘルパークラス。
 *
 * Created by hiroki-mino on 2015/07/02.
 */
public class RecyclerWeaselConnector extends RecyclerView.OnScrollListener implements
        ScrollOrientationChangeHelper.ScrollOrientationChangeListener {
    private static final String TAG = RecyclerWeaselConnector.class.getSimpleName();

    Weasel mWeasel;

    ScrollOrientationChangeHelper mScrollOrientationHelper;

    private int mCurrentPosition = 0;

    public RecyclerWeaselConnector(Weasel weasel) {
        mScrollOrientationHelper = new ScrollOrientationChangeHelper(this);
        mWeasel = weasel;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mScrollOrientationHelper.onScroll(dy);
        mCurrentPosition += dy;
        mWeasel.chase(mCurrentPosition);
    }

    @Override
    public void onOrientationChage(boolean up) {
        Event ev = up ? Event.START_SCROLL_UP : Event.START_SCROLL_DOWN;
        mWeasel.event(ev, mCurrentPosition);
    }
}
